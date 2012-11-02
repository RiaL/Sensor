package monitor;

import java.nio.channels.SocketChannel;

/**
 *
 * @author Krzysztof Kutt
 */
public class Subscription {
    SocketChannel channel;
    Sensor sensor;
    Monitor monitor;

    public Subscription(Sensor sensor, Monitor monitor) {
        this.sensor = sensor;
        this.monitor = monitor;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }
    
}
