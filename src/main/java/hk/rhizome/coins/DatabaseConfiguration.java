package hk.rhizome.coins;

public class DatabaseConfiguration {

   
    private String username;
    private String password;
    private String driverClass;
    private String url;
       
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getDriverClass(){
        return this.driverClass;
    }

    public void setDriverClass(String driverClass){
        this.driverClass = driverClass;
    }

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String url){
        this.url = url;
    }
   
}