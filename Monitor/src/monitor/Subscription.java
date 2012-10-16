package monitor;

/**
 *
 * @author Krzysztof Kutt
 */
public class Subscription {
    int clientPort;
    Sensor sensor;
    Monitor monitor;

    public Subscription(int clientPort, Sensor sensor, Monitor monitor) {
        this.clientPort = clientPort;
        this.sensor = sensor;
        this.monitor = monitor;
    }

    public int getClientPort() {
        return clientPort;
    }

    public Sensor getSensor() {
        return sensor;
    }
    
}
