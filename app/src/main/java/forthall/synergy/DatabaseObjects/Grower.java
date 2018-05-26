package forthall.synergy.DatabaseObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MY-HOMEDESKTOP on 2/25/2017.
 */
public class Grower {

        private String growerNo;
    private String Name="";

    private String centerId;
    private String Email;
    private String phoneNumber;
    public String getPhoneNumber(){
        return  phoneNumber;
    }
    public  void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }

    private List<Bag> Bags= new ArrayList<Bag>();;
        public List<Bag> getBags() {
            return Bags;
        }
        public void setBags(List<Bag> bags) {
            for(int i=Bags.size();i<Bags.size()+bags.size();i++){
                for(Bag bag:bags){
                    bag.setBagId(String.valueOf(i));
                    bags.add(bag);
                    break;
                }
            }

        }

    public Grower(String growerNo, String name) {
        this.growerNo = growerNo;
        Name = name;
    }

    public Grower() {
            // TODO Auto-generated constructor stub
        }
        public String getGrowerName() {
            return Name;
        }

        public String getGrowerNo() {
            return growerNo;
        }
        public void setGrowerNo(String growerNo) {
            this.growerNo = growerNo;
        }
        public void addBag(Bag bag){

            bag.setBagId(String.valueOf(Bags.size()+1));
            this.Bags.add(bag);

    }

    public String getGrowerId() {
        return growerNo;
    }

    public void setGrowerId(String growerId) {
        this.growerNo = growerId;
    }
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }




    public void setGrowerName(String Name) {
        this.Name=this.Name.concat(Name+" ");
    }
    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }
  /*  private String Email;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGrowerId() {
        return growerId;
    }

    public void setGrowerId(String growerId) {
        this.growerId = growerId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    private String firstName;
    private String growerId;
    private String lastName;
    private String middleName;
   private String centerId;

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }


    private String Growerid;


    public String getGrowerid() {

        return Growerid;
    }

    public void setGrowerid(String growerid) {
        Growerid = growerid;
    }


    private String growerName;
    private String growerNo;
    private List<Bag> Bags= new ArrayList<Bag>();;
    public List<Bag> getBags() {
        return Bags;
    }
    public void setBags(List<Bag> bags) {
        for(int i=Bags.size();i<Bags.size()+bags.size();i++){
            for(Bag bag:bags){
                bag.setBagId(String.valueOf(i));
                bags.add(bag);
                break;
            }
        }

    }
    public Grower(String growerName, String growerNo) {
        this.growerName = growerName;
        this.growerNo = growerNo;
    }
    public Grower() {
        // TODO Auto-generated constructor stub
    }
    public String getGrowerName() {
        return growerName;
    }
    public void setGrowerName(String growerName) {
        this.growerName = growerName;
    }
    public String getGrowerNo() {
        return growerNo;
    }

    public void addBag(Bag bag){

        bag.setBagId(String.valueOf(Bags.size()+1));
        this.Bags.add(bag);
    }*/
}
