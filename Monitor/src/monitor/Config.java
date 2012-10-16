package monitor;

/**
 *
 * @author Krzysztof Kutt
 */
public class Config {
    
    /* HTTP Server configuration */
    public static final int HTTPServerPort = 8000;
    public static final String HTTPSubscriptionsResourceName = "/subscriptions";
    
    /* Monitor 1 configuration (adres to 127.0.0.1) */
    public static final int Monitor1SensorPort = 8001;
    public static final int Monitor1SubsPorts = 11000;
    
    /* Monitor 2 configuration (adres to 127.0.0.1) */
    public static final int Monitor2SensorPort = 8002;
    public static final int Monitor2SubsPorts = 12000;
       
    
}
