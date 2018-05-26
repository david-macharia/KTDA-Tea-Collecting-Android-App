package forthall.synergy.dansonmbuthia.application.ktdainterfaces;

/**
 * Created by MY-HOMEDESKTOP on 2/23/2017.
 */
public class TimeOut extends Exception {
    String message;
    public TimeOut(String mesg){
      message=mesg;
    }
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    private int time;
    private String Host;

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }
    public String getMessage(){
        return message;
    }
}
