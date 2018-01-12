package hk.rhizome.coins.logger;

import java.util.Collections;
import java.util.Map;

import org.json.simple.JSONObject;

public class LoggerFormatter {
	
    public String formatLogMsg(Object message, LoggerUtils.Level level) {
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

