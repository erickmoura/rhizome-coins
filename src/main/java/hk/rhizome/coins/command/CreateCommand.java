package hk.rhizome.coins.command;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.knowm.xchange.Exchange;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import hk.rhizome.coins.RhizomeCoinsApplication;
import hk.rhizome.coins.RhizomeCoinsConfiguration;
import hk.rhizome.coins.UserConfiguration;
import hk.rhizome.coins.db.DataSourceUtil;
import hk.rhizome.coins.db.DbProxyUtils;
import hk.rhizome.coins.db.ExchangesDAO;
import hk.rhizome.coins.db.ExchangesDAOProxy;
import hk.rhizome.coins.db.UserExchangesDAO;
import hk.rhizome.coins.db.UserExchangesDAOProxy;
import hk.rhizome.coins.db.UsersDAO;
import hk.rhizome.coins.db.UsersDAOProxy;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.logger.LoggerUtils;
import hk.rhizome.coins.model.Exchanges;
import hk.rhizome.coins.model.User;
import hk.rhizome.coins.model.UserExchanges;
import hk.rhizome.coins.utils.RhizomeCoinsUtil;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.cli.Command;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class CreateCommand extends EnvironmentCommand<RhizomeCoinsConfiguration> {
	
	private static final String COMMAND_KEY = "create";
	private static final String COMMAND_DESCRIPTION = "Command to insert the user data in the database.";

	private static final String USERCONFIG = "-f";
	private static final String DEST = "fileUserConfiguration";
	private static final String USERCONFIG_DESCRIPTION = "The file name with the user configuration";

	private HibernateBundle<RhizomeCoinsConfiguration> hibernate;

    public CreateCommand(Application<RhizomeCoinsConfiguration> application, HibernateBundle<RhizomeCoinsConfiguration> hibernate) {
		super(application, COMMAND_KEY, COMMAND_DESCRIPTION);
		this.hibernate = hibernate;
	}

	@Override
	public void configure(Subparser subparser) {
		super.configure(subparser);
		subparser.addArgument(USERCONFIG).dest(DEST).type(String.class).required(true).setDefault("user-config.yml").help(USERCONFIG_DESCRIPTION);
	}
	
	@Override
	protected void run(Environment arg0, Namespace arg1, RhizomeCoinsConfiguration arg2) throws Exception {
	
		AppLogger.initialize(LoggerUtils.getLoggerConfiguration(arg2.getLogging()));
		
		//reading user configuration file
		String fileName = arg1.getString(DEST);
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		UserConfiguration userConfiguration;
		AppLogger.getLogger().info("Reading configuration file");
        try {
            userConfiguration = mapper.readValue(new File(fileName), UserConfiguration.class);
        } catch (Exception e) {
			AppLogger.getLogger().error("Unable to read the file " + fileName);
			throw new RuntimeException("Unable to read the file " + fileName);
		}
		
		//setting db utils
		DataSourceFactory dataSourceFactory = DataSourceUtil.getDataSourceFactory(arg2.getDatabase());
		DbProxyUtils.initialize();
		UsersDAO usersDAO = new UsersDAO(hibernate.getSessionFactory());
        UsersDAOProxy usersDAOProxy = new UnitOfWorkAwareProxyFactory(hibernate).create(UsersDAOProxy.class, UsersDAO.class, usersDAO);
        UserExchangesDAO userExchangesDAO = new UserExchangesDAO(hibernate.getSessionFactory());
        UserExchangesDAOProxy userExchangesDAOProxy = new UnitOfWorkAwareProxyFactory(hibernate).create(UserExchangesDAOProxy.class, UserExchangesDAO.class, userExchangesDAO); 
		ExchangesDAO exchangesDAO = new ExchangesDAO(hibernate.getSessionFactory());
		ExchangesDAOProxy exchangeDAOProxy = new ExchangesDAOProxy(exchangesDAO);
		DbProxyUtils.getInstance().setUsersProxy(usersDAOProxy);
		DbProxyUtils.getInstance().setUserExchangesProxy(userExchangesDAOProxy);
		DbProxyUtils.getInstance().setExchangesProxy(exchangeDAOProxy);
		
		Session session = hibernate.getSessionFactory().openSession();
		ManagedSessionContext.bind(session);
		Transaction transaction = session.beginTransaction();


		//creating user
		AppLogger.getLogger().info("Creating the user");
		User user = new User();
		user.setName(userConfiguration.getUsername());
        try{
			usersDAO.update(user);
			transaction.commit();
		}
		catch(Exception ex){
			AppLogger.getLogger().error("Cannot create the user" + userConfiguration.getUsername());
			throw new RuntimeException("Cannot create the user" + userConfiguration.getUsername());
		}
		
		//saving exchanges
		AppLogger.getLogger().info("Saving exchanges");
		transaction = session.beginTransaction();
		List<Exchanges> exchanges = exchangeDAOProxy.getAllExchanges();
		Set<UserExchanges> userExchangesSet = new HashSet<UserExchanges>();
		for(String exchange : userConfiguration.getExchanges().keySet()){
			for(Exchanges dbExchanges : exchanges){
				if(dbExchanges.getExchangeName().equals(exchange)){
					String key = (String)((Map)userConfiguration.getExchanges().get(exchange)).get("key");
					String secret = (String)((Map)userConfiguration.getExchanges().get(exchange)).get("secret");
					UserExchanges ue = new UserExchanges(user, dbExchanges, null, key, secret, new Date(), new Date());
					userExchangesDAO.update(ue);
					userExchangesSet.add(ue);
				}
			}
		}
		transaction.commit();
		session.close();
		
		//stop the server
		try{
			//arg0.getApplicationContext().getServer().stop();
			System.exit(0);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
	}

	
}