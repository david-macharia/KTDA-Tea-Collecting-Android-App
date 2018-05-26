package forthall.synergy.DatabaseObjects;

/**
 * Created by MY-HOMEDESKTOP on 2/25/2017.
 */
public class Clerk {
    private String clerkName;
    private String clerkId;

    public Clerk(String clerkName, String clerkId) {
        this.clerkName = clerkName;
        this.clerkId = clerkId;
    }
    public Clerk() {
        // TODO Auto-generated constructor stub
    }
    public String getClerkName() {
        return clerkName;
    }
    public void setClerkName(String clerkName) {
        this.clerkName = clerkName;
    }
    public String getClerkId() {
        return clerkId;
    }
    public void setClerkId(String clerkId) {
        this.clerkId = clerkId;
    }

}
