package hk.rhizome.coins.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import hk.rhizome.coins.logger.AppLogger;
import org.apache.commons.io.IOUtils; 

public class RhizomeCoinsUtil {

    public File getSqlFile(String name) throws Exception {
		File sqlFile = null;
		try {
			InputStream sqlStream = ClassLoader.getSystemResourceAsStream(name);
			System.out.println(sqlStream);
			sqlFile = convertStreamToSqlFile(sqlStream,"query");
		} catch (Exception e) {
			AppLogger.getLogger().error("Exception in RhizomeCoinsUtil in getSqlFile");
			throw e;
		}
		return sqlFile;
    }
    
    public File convertStreamToSqlFile(InputStream is, String name) throws Exception {
		File tempFile = File.createTempFile(name, ".sql");
		try {
			tempFile.deleteOnExit();
			FileOutputStream out = new FileOutputStream(tempFile);
			if(is == null){
				System.out.println("Input stream is null");
			}
			IOUtils.copy(is, out);
		} catch (Exception e) {
			AppLogger.getLogger().error("Exception in RhizomeCoinsUtil convertStreamToSqlFile");
			throw e;
		}
		return tempFile;
    }
    
    public static String getSqlString(File sqlFile) {
		String content = null;
		try {
			content = new String(Files.readAllBytes(sqlFile.toPath()));
		} catch (IOException e) {
			AppLogger.getLogger().error("Exception in RhizomeCoinsUtil getSqlString");
		}
		return content;
	}

	/**
	 * Calculates how frequent the jobs should make a request to specific exchange.
	 * For safety purposes left a margin 10% of the requests permited in a minute.
	 */
	public static int calculatePollingRate(int requestsPerMinute){
		//return (int) (60/(0.9*requestsPerMinute));
		return 1;
	}
}