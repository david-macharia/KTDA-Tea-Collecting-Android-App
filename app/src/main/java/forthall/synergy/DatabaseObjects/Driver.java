package forthall.synergy.DatabaseObjects;

/**
 * Created by MY-HOMEDESKTOP on 2/25/2017.
 */
public class Driver {
    private String driverName;
    private String  driverId;

    public Driver(String driverId, String driverName) {
        this.driverId = driverId;
        this.driverName = driverName;
    }

    public Driver() {
    }

    public String getDriverId() {

        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
}
