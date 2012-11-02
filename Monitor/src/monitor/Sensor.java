package monitor;

/**
 *
 * @author Krzysztof Kutt
 */
public class Sensor {
    private String resourceId;
    private String metric;
    
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

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Sensor otherSensor = (Sensor) o;
        if (metric == null) {
            if (otherSensor.metric != null) {
                return false;
            }
        } else if (!metric.equals(otherSensor.metric)) {
            return false;
        }
        if (resourceId == null) {
            if (otherSensor.resourceId != null) {
                return false;
            }
        } else if (!resourceId.equals(otherSensor.resourceId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((metric == null) ? 0 : metric.hashCode());
        result = prime * result + ((resourceId == null) ? 0 : resourceId.hashCode());
        return result;
    }
}
