package forthall.synergy.dansonmbuthia.application.ktdainterfaces;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import forthall.synergy.finalfields.AppPreferences;


/**
 * Created by MY-HOMEDESKTOP on 3/8/2017.
 */
public class NetworkConnectivity {

    protected ConnectivityManager connectivityManager;
    protected static String networkstate = "";
    NetworkInfo networkInfo;

    public NetworkConnectivity(Context context) {
         SharedPreferences sharedPreferences= context.getSharedPreferences(AppPreferences.PREFERENCE_NAME,Context.MODE_PRIVATE);
        connectivityManager = (ConnectivityManager) context.getSystemService(Application.CONNECTIVITY_SERVICE);
        //noinspection deprecation
        networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean state = (networkInfo != null) && networkInfo.isConnectedOrConnecting();
        if (state) {
            networkstate = InternetConnection.CONNECTED;
        } else {
            networkstate = InternetConnection.NOT_CONNECTED;
        }
    }

    public String getNetworkState() {


        return networkstate;
    }

    public NetworkManager getConnectivityManager(Handler handler, Context context,String ipaddress,String portnumber) {
        NetworkManager networkManager=new NetworkManager(handler,context);
        networkManager.setIpAndAddress(ipaddress,portnumber);
        return networkManager;
    }

}
