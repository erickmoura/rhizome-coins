package hk.rhizome.coins.logger;

import java.util.Collections;
import java.util.Map;

import org.json.simple.JSONObject;

public class LoggerFormatter {
	
	private final static String SPACE = " ";

    public String formatLogMsg(String message, LoggerUtils.Level level) {
        return formatLogMap(Collections.singletonMap("message", message), level);
    }
   
    public String formatLogMap(Map<String, Object> msgs, LoggerUtils.Level level) {
    		JSONObject result = new JSONObject();
    		for (Map.Entry<String, Object> entry : msgs.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
    		}
        return result.toJSONString();
    }
}

