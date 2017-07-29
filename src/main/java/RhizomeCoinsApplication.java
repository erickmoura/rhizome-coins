/**
 * Created by erickmoura on 28/7/2017.
 */


import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.poloniex.ExchangeUtils;
import resources.HelloWorldResource;
import health.TemplateHealthCheck;

public class RhizomeCoinsApplication extends Application<RhizomeCoinsConfiguration> {
    public static void main(String[] args) throws Exception {
        new RhizomeCoinsApplication().run(args);
    }

    @Override
    public String getName() {
        return "config";
    }

    @Override
    public void initialize(Bootstrap<RhizomeCoinsConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(RhizomeCoinsConfiguration configuration,
                    Environment environment) {


        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);

        ExchangeUtils.getInstance().setExchangeMap(configuration.getExchanges());

        //Start Collection Bots...
        MarketDataManager m = new MarketDataManager();
        m.startDataMarketThreads();
    }

}
