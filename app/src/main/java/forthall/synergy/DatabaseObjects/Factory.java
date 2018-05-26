package forthall.synergy.DatabaseObjects;

import com.itextpdf.text.Image;

/**
 * Created by MY-HOMEDESKTOP on 2/25/2017.
 */
public class Factory {
    private String factoryId;
    private String Name;
    private int numberOfCenters;
    private Image image;
  /*  public Factory(String factoryId, String factoryName, int numberOfCenters) {
        this.factoryId = factoryId;
        this.factoryName = factoryName;
        this.numberOfCenters = numberOfCenters;
    }*/
  public Factory(String name, String factoryId,int numberOfCenters) {

      Name = name;
      this.factoryId = factoryId;
      this.numberOfCenters=numberOfCenters;

  }
    public Factory(String name, String factoryId, Image image) {

        Name = name;
        this.factoryId = factoryId;
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;
    }
    public Factory() {
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryName() {
        return Name;
    }

    public void setFactoryName(String factoryName) {
        this.Name = factoryName;
    }

    public int getnumberOfCenters() {
        return numberOfCenters;
    }

    public void setnumberOfCenters(int numberOfGrowers) {
        this.numberOfCenters = numberOfGrowers;
    }



    public Factory(String name, String factoryId) {
        Name = name;
        this.factoryId = factoryId;
    }

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }


}
