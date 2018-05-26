package forthall.synergy.DatabaseObjects;

/**
 * Created by MY-HOMEDESKTOP on 2/25/2017.
 */
public class Bag implements  Cloneable {
   private String   growerId;
    private String driverId;

    private String centerId;
    private String bagId;
    private Double weightInKg;
    private Double tareWeight=Transaction.BAG_TARE_WEIGHT;
    private Double netWeight;

    public Bag(double bagWeight, String centerId, String driverId, String growerId) {
        this.weightInKg = bagWeight;
        this.centerId = centerId;
        this.driverId = driverId;
        this.growerId = growerId;
        bagId=String.valueOf(Math.random())+centerId+driverId+growerId;
    }

    public String getBagId() {
        return bagId;
    }

    public Bag() {
    }



    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getGrowerId() {
        return growerId;
    }

    public void setGrowerId(String growerId) {
        this.growerId = growerId;
    }


    public Bag(String bagId, Double weightInKg) {
        this.bagId = bagId;
        this.weightInKg = weightInKg;


    }

    void setBagId(String bagId) {
        this.bagId = bagId;
    }
    public Double getWeightInKg() {
        return weightInKg;
    }
    public void setWeightInKg(Double weightInKg) {
        this.weightInKg = weightInKg;
        setNetWeight();
    }
    public Double getTareWeight() {
        return tareWeight;
    }

    public Double getNetWeight() {
        return netWeight;
    }
    private void setNetWeight() {
        this.netWeight = weightInKg-this.tareWeight;
    }

}
