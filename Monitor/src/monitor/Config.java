package monitor;

/**
 *
 * @author Krzysztof Kutt
 */
public class Config {
    
    /* HTTP Server configuration */
    public static final int HTTP_SERVER_PORT = 8000;
    public static final String HTTP_SUBSCRIPTIONS_PATH = "/subscriptions";
    
    /* Monitor 1 configuration (adres to 127.0.0.1) */
    public static final int M1_SENSOR_PORT = 8001;
    public static final int M1_CLIENT_PORT = 9001;
    
    /* Monitor 2 configuration (adres to 127.0.0.1) */
    public static final int M2_SENSOR_PORT = 8002;
    public static final int M2_CLIENT_PORT = 9002;
    
    /* Buffer for data from sensor */
    public static final int MAX_BUFFER_CAPACITY = 4096;
    
    /* Values will be read from sensors each 5 sec. */
    public static final int TIME_INTERVAL = 5000;
    
}
