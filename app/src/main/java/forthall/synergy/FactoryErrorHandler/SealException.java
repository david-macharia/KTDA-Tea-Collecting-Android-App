package forthall.synergy.FactoryErrorHandler;

/**
 * Created by MY-HOMEDESKTOP on 2/27/2017.
 */
public class SealException extends Exception {
    public SealException(){
        super("ERROR:Attempt to add item to a sealed parcel");
    }
}
