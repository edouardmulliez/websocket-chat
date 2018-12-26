package websocket.chat.connection;


import java.io.IOException;
import java.util.Properties;

/**
 * Class to read the properties in mysql.properties file.
 */
public class Config
{
    private static Config instance = null;
    private Properties configFile;

    private Config()
    {
        configFile = new Properties();
        try {
            configFile.load(this.getClass().getClassLoader().
                    getResourceAsStream("mysql.properties"));
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private String getValue(String key) {
        return configFile.getProperty(key);
    }

    public static String getProperty(String key)
    {
        if (instance == null){
            instance = new Config();
        }
        return instance.getValue(key);
    }
}