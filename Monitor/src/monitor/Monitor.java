package monitor;

import java.nio.channels.Selector;
import java.util.HashMap;

/**
 *
 * @author Krzysztof Kutt
 */
public class Monitor implements Runnable {
    
    int sensorPort;
    int subscriptionStartPort;
    HashMap<Integer, Sensor> subscriptions;  //numer portu, na który wysyłać dane z podanego sensora
    
    Selector selector;  //TODO: selector musi nasłuchiwać odpowiednie porty

    public Monitor(int sensorPort, int subscriptPort) {
        this.sensorPort = sensorPort;
        this.subscriptionStartPort = subscriptPort;
    }

    @Override
    public void run() {
        //TODO
        
    }
    
    public void addSensor(Sensor s){
        HTTPServer.register.put(s, this);
        
        //TODO: dodać go do monitora!
    }
    
    @Override
    public boolean equals(Object o) {
	if (!(o instanceof Monitor))
            return false;
	Monitor s = (Monitor) o;
	if ( (this.sensorPort == s.sensorPort) && (this.subscriptionStartPort == s.subscriptionStartPort) )
            return true;
	return false;
    }
    
    
}
