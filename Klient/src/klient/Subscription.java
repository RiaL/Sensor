package klient;

/**
 *
 * @author Krzysztof Kutt
 */
public class Subscription {
    String resourceId;
    String metric;
    String location;
    String host;
    String port;
    
    public Subscription(){
        this.resourceId = "";
        this.metric = "";
        this.location = "";
        this.host = "";
        this.port = "";
    }

    public String getHost() {
        return host;
    }

    public String getLocation() {
        return location;
    }

    public String getMetric() {
        return metric;
    }

    public String getPort() {
        return port;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
