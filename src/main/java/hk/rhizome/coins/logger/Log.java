package hk.rhizome.coins.logger;

import java.util.Date;

public class Log {

    private String message;
    private String level;
    private Date timestamp;

    public Log(String level, String message, Date timestamp){
        this.message = message;
        this.level = level;
        this.timestamp = timestamp;
    }

    public String getMessage(){
        return message;
    }

    public String getLevel(){
        return level;
    }

    public Date getTimestamp(){
        return timestamp;
    }
}