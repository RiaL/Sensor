package monitor;

/**
 *
 * @author Krzysztof Kutt
 */
public class Sensor {
    String resourceId;
    String metric;
    
    Sensor(String resourceId, String metric){
        this.resourceId = resourceId;
        this.metric = metric;
    }

    public String getMetric() {
        return metric;
    }

    public String getResourceId() {
        return resourceId;
    }
    
    @Override
    public boolean equals(Object o) {
	if (!(o instanceof Sensor))
            return false;
	Sensor s = (Sensor) o;
	if (this.resourceId.equals(s.resourceId) && this.metric.equals(s.metric) )
            return true;
	return false;
    }
    
}
