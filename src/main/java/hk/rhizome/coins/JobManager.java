package hk.rhizome.coins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import hk.rhizome.coins.db.DbProxyUtils;
import hk.rhizome.coins.db.ExchangesDAOProxy;
import hk.rhizome.coins.jobs.BalancesJob;
import hk.rhizome.coins.jobs.CoinMarketCapJob;
import hk.rhizome.coins.jobs.OrdersJob;
import hk.rhizome.coins.jobs.UserTradesJob;
import hk.rhizome.coins.jobs.XChangeJob;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.CoinsSetService;
import hk.rhizome.coins.model.Coins;
import hk.rhizome.coins.model.Exchanges;
import hk.rhizome.coins.model.User;
import hk.rhizome.coins.model.UserExchanges;
import hk.rhizome.coins.utils.Constants;
import hk.rhizome.coins.utils.RhizomeCoinsUtil;

public class JobManager {

    private Map<String, JobDetail> jobs;
    private Map<String, Trigger> triggers;

    private static final String EXCHANGES_JOB_GROUP_NAME = "ExchangesPoller";
    private static final String USERTRADES_JOB_GROUP_NAME = "UserTradesPoller";
    private static final String BALANCES_JOB_GROUP_NAME = "BalancesPoller";
    private static final String ORDERS_JOB_GROUP_NAME = "OrdersPoller";

    public JobManager() {
        this.jobs = new HashMap<String, JobDetail>();
        this.triggers = new HashMap<String, Trigger>();
    }

    public void initializeJobs() {
        try {
            createXChangeJobs();
            createCoinMarketCapJob();
            createJob(UserTradesJob.class, USERTRADES_JOB_GROUP_NAME, Constants.USERTRADES_POLLING_PERIODICITY, "UserTrades");
            createJob(BalancesJob.class, BALANCES_JOB_GROUP_NAME, Constants.BALANCES_POLLING_PERIODICITY, "Balances");
            createJob(OrdersJob.class, ORDERS_JOB_GROUP_NAME, Constants.ORDERS_POLLING_PERIODICITY, "Orders");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void runJobs() {
        Scheduler scheduler;
        try {
            scheduler = new StdSchedulerFactory().getScheduler();

            AppLogger.getLogger().info("Deleting existing jobs and triggers");
            scheduler.clear();

            AppLogger.getLogger().info("Adding new jobs and triggers");
            scheduler.start();
            for (String jobName : jobs.keySet()) {
                scheduler.scheduleJob(jobs.get(jobName), triggers.get(jobName));
            }

        } catch (SchedulerException e) {
            AppLogger.getLogger().error("Error in JobManager in runJobs : " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private void createXChangeJobs() {
        AppLogger.getLogger().debug("Creating XchangeJob in JobManager");

        ExchangesDAOProxy proxy = DbProxyUtils.getInstance().getExchangesProxy();
        CoinsSetService coinsService = CoinsSetService.getInstance();
        List<Coins> coins = coinsService.getCoins();

        for (Exchanges exchanges : proxy.getAllExchanges()) {
            ExchangeSpecification exSpec = new ExchangeSpecification(exchanges.getXchangeName());
            exSpec.setApiKey("key");
            exSpec.setSecretKey("secret");
            Exchange e = ExchangeFactory.INSTANCE.createExchange(exSpec);

            List<CurrencyPair> currencyPairs = e.getExchangeSymbols();
            for (CurrencyPair c : currencyPairs) {
                if (filterCoin(coins, c.base.getDisplayName()) && filterCoin(coins, c.counter.getDisplayName())) {
                    StringBuilder jobName = new StringBuilder();
                    jobName.append(exchanges.getExchangeName()).append("_").append(c.toString()).append("_")
                            .append("Job");
                    StringBuilder triggerName = new StringBuilder();
                    triggerName.append(exchanges.getExchangeName()).append("_").append(c.toString()).append("_")
                            .append("Trigger");

                    JobDataMap map = new JobDataMap();
                    map.put("exchangeID", exchanges.getXchangeName());
                    map.put("currencyPair", c.toString());
                    JobDetail job = JobBuilder.newJob(XChangeJob.class)
                            .withIdentity(jobName.toString(), EXCHANGES_JOB_GROUP_NAME).usingJobData(map).build();

                    int intervalSeconds = RhizomeCoinsUtil.calculatePollingRate(exchanges.getPollingRate());

                    Trigger trigger = TriggerBuilder.newTrigger().startNow()
                            .withIdentity(triggerName.toString(), EXCHANGES_JOB_GROUP_NAME)
                            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(intervalSeconds)
                                    .repeatForever())
                            .build();

                    jobs.put(jobName.toString(), job);
                    triggers.put(jobName.toString(), trigger);
                }
            }
        }

        AppLogger.getLogger().debug("Finished CoinMarketCapJob in JobManager");

    }

    private boolean filterCoin(List<Coins> coins, String name) {
        for (Coins c : coins) {
            if (c.getName().equals(name))
                return true;
        }
        return false;
    }

    private void createCoinMarketCapJob() {
        AppLogger.getLogger().debug("Creating CoinMarketCapJob in JobManager");

        String jobName = "CoinMarketCapJob";
        String triggerName = "CoinMarketCapTrigger";

        //createJobTriggers(CoinMarketCapJob.class,jobName, triggerName, JOB_GROUP_NAME, Constants.COINMARKETCAP_POLLING_PERIODICITY);
        JobDetail job = JobBuilder.newJob(CoinMarketCapJob.class).withIdentity(jobName, EXCHANGES_JOB_GROUP_NAME)
                .build();

        int intervalSeconds = RhizomeCoinsUtil.calculatePollingRate(Constants.COINMARKETCAP_POLLING_PERIODICITY);
        Trigger trigger = TriggerBuilder.newTrigger().startNow().withIdentity(triggerName, EXCHANGES_JOB_GROUP_NAME)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(intervalSeconds).repeatForever())
                .build();

        jobs.put(jobName, job);
        triggers.put(jobName, trigger);

        AppLogger.getLogger().debug("Finished CoinMarketCapJob in JobManager");

    }

    private void createJob(Class<? extends Job> jobClass, String groupName, int pollingRate, String initialJobName) {

        AppLogger.getLogger().debug("Creating " + jobClass.getSimpleName() + " in JobManager");

        List<UserExchanges> userexchanges = DbProxyUtils.getInstance().getUserExchangesProxy().getAllUserExchanges();
        for (UserExchanges ue : userexchanges) {

            Exchanges exchange = ue.getExchange();
            User user = ue.getUser();

            StringBuilder jobName = new StringBuilder();
            jobName.append(initialJobName).append("_").append(user.getID()).append("_").append(exchange.getID())
                    .append("Job");
            StringBuilder triggerName = new StringBuilder();
            triggerName.append(initialJobName).append("_").append(user.getID()).append("_").append(exchange.getID())
                    .append("Trigger");

            JobDataMap map = new JobDataMap();
            map.put("exchangeId", exchange.getID());
            map.put("userId", user.getID());
            JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobName.toString(), groupName)
                    .usingJobData(map).build();

            int intervalSeconds = Constants.USERTRADES_POLLING_PERIODICITY;

            Trigger trigger = TriggerBuilder.newTrigger().startNow().withIdentity(triggerName.toString(), groupName)
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(intervalSeconds)
                            .repeatForever())
                    .build();

            jobs.put(jobName.toString(), job);
            triggers.put(jobName.toString(), trigger);
        }

        AppLogger.getLogger().debug("Finished " + jobClass.getSimpleName() + " in JobManager");
    }

}