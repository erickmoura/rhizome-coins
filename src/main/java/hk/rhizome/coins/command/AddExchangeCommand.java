package hk.rhizome.coins.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import hk.rhizome.coins.ExchangesConfiguration;
import hk.rhizome.coins.RhizomeCoinsConfiguration;
import hk.rhizome.coins.UserConfiguration;
import hk.rhizome.coins.db.ExchangesDAO;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.logger.LoggerUtils;
import hk.rhizome.coins.model.Exchanges;
import io.dropwizard.Application;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Environment;
import java.io.File;
import java.util.Map;
import java.math.BigDecimal;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;


public class AddExchangeCommand extends EnvironmentCommand<RhizomeCoinsConfiguration> {

    private static final String COMMAND_KEY = "addexchange";
	private static final String COMMAND_DESCRIPTION = "Command to add exchange to the database";

	private static final String USERCONFIG = "-f";
	private static final String DEST = "fileUserConfiguration";
	private static final String EXCHNAGECONFIG_DESCRIPTION = "The file name with the exchanges configuration";

	private HibernateBundle<RhizomeCoinsConfiguration> hibernate;

    public AddExchangeCommand(Application<RhizomeCoinsConfiguration> application, HibernateBundle<RhizomeCoinsConfiguration> hibernate) {
        super(application,COMMAND_KEY, COMMAND_DESCRIPTION);
        this.hibernate = hibernate;
    }

    @Override
	public void configure(Subparser subparser) {
		super.configure(subparser);
		subparser.addArgument(USERCONFIG).dest(DEST).type(String.class).required(true).setDefault("exchange-config.yml").help(EXCHNAGECONFIG_DESCRIPTION);
	}
	
	@Override
	protected void run(Environment arg0, Namespace arg1, RhizomeCoinsConfiguration arg2) throws Exception {
        
        AppLogger.initialize(LoggerUtils.getLoggerConfiguration(arg2.getLogging()));
		
		//reading user configuration file
		String fileName = arg1.getString(DEST);
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		ExchangesConfiguration exchangeConfiguration;
		AppLogger.getLogger().info("Reading configuration file");
        try {
            exchangeConfiguration = mapper.readValue(new File(fileName), ExchangesConfiguration.class);
        } catch (Exception e) {
			AppLogger.getLogger().error("Unable to read the file " + fileName);
			throw new RuntimeException("Unable to read the file " + fileName);
        }
        
        Session session = hibernate.getSessionFactory().openSession();
		ManagedSessionContext.bind(session);
		Transaction transaction = session.beginTransaction();

        AppLogger.getLogger().info("Creating the exchangesbootstrap");
        ExchangesDAO exchangesDAO = new ExchangesDAO(hibernate.getSessionFactory());
		
		for(String exchangeName : exchangeConfiguration.getExchanges().keySet()){
            Exchanges exchange = new Exchanges();
            exchange.setExchangeName(exchangeName);
            exchange.setMaker(new BigDecimal((Double)((Map)exchangeConfiguration.getExchanges().get(exchangeName)).get("maker")));
            exchange.setTaker(new BigDecimal((Double)((Map)exchangeConfiguration.getExchanges().get(exchangeName)).get("taker")));
            exchange.setPollingRate(new Integer((Integer)((Map)exchangeConfiguration.getExchanges().get(exchangeName)).get("polling_rate")));
            exchange.setXchangeName((String)((Map)exchangeConfiguration.getExchanges().get(exchangeName)).get("namespace"));
            exchangesDAO.update(exchange);
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