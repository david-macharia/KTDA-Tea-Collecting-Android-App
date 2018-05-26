package forthall.synergy.DatabaseObjects;

/**
 * Created by MY-HOMEDESKTOP on 2/25/2017.
 */
public class BuyingCenter {
    private  String centerName;
    private String centerId;
  private int numberOfGrowers;
    private String factoryId;

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public int getNumberOfGrowers() {
        return numberOfGrowers;
    }

    public void setNumberOfGrowers(int numberOfGrowers) {
        this.numberOfGrowers = numberOfGrowers;
    }

    public BuyingCenter(String centerId, String centerName) {
        this.centerId = centerId;
        this.centerName = centerName;
    }

    public BuyingCenter() {
    }

    public String getCenterId() {
        return centerId;

    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }
}
