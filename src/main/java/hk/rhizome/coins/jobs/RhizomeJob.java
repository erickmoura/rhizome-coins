package hk.rhizome.coins.jobs;

import org.quartz.Job;

import hk.rhizome.coins.KinesisGateway;
import hk.rhizome.coins.logger.AppLogger;

public abstract class RhizomeJob implements Job {
        
    protected static KinesisGateway kinesisGateway = new KinesisGateway();

    protected RhizomeJob(){
        try {
			kinesisGateway.validateStream();
		} catch (Exception e) {
			AppLogger.getLogger().error("Could not validate KinesisStream in RhizomeJob");
		}
    }
}