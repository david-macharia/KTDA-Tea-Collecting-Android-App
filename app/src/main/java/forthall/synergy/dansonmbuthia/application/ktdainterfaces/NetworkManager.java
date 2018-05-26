package forthall.synergy.dansonmbuthia.application.ktdainterfaces;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import forthall.synergy.finalfields.AppPreferences;

/**
 * Created by MY-HOMEDESKTOP on 5/9/2017.
 */
public class NetworkManager extends  Thread {
    Context context;
    Handler manager;
    Bundle bundle;
    protected String ipaddress;
    protected  String portaddress;

    String filename;
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    NetworkManager(Handler handler, Context context) {
        this.manager = handler;
        this.context = context;

    }
    public void setIpAndAddress(String ipaddress ,String portaddress){
        this.ipaddress=ipaddress;
        this.portaddress=portaddress;
    }

    @Override
    public void run() {

        try {
            isreachable(context);
        } catch (TimeOut timeOut) {
            bundle = new Bundle();
            bundle.putString("FILENAME",filename);
            bundle.putString("CONNECTION", "REQUEST_TIMED_OUT");
            Message message = new Message();
            message.setData(bundle);
            manager.sendMessage(message);

        }
    }

    public void isreachable(Context context) throws TimeOut {
        SharedPreferences sharedPreferences= context.getSharedPreferences(AppPreferences.PREFERENCE_NAME,Context.MODE_PRIVATE);
        HttpURLConnection httpURLConnection = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if ((info != null) && info.isConnected()) {
            URL url;
            try {

                if(sharedPreferences.getString("ip_address","0.0.0.0").equals("0.0.0.0")&&sharedPreferences.getString("port_number","8080").equals("8080")) {
                    url = new URL("http://"+ipaddress+":"+portaddress);
                    Log.i("URL", url.toString());
                }else{
                    url = new URL("http://"+sharedPreferences.getString("ip_address","0.0.0.0")+":"+sharedPreferences.getString("port_number","8080"));
                }
                //
                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.connect();

                if (httpURLConnection.getResponseCode() == 200) {
                    Log.e("Connection status:", "servers reachable");
                    bundle = new Bundle();
                    bundle.putString("CONNECTION", "REACHABLE");
                    bundle.putString("FILENAME",filename);
                    Message message = new Message();
                    message.setData(bundle);
                    manager.sendMessage(message);
                }
            } catch (SocketTimeoutException | ConnectException e) {
                throw new TimeOut(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                throw new TimeOut(e.getMessage());

            }
        } else {
            Log.e("Connection status:", "we cannot get the server");
            bundle = new Bundle();
            bundle.putString("CONNECTION", InternetConnection.NOT_CONNECTED);
            bundle.putString("FILENAME",filename);
            Message message = new Message();
            message.setData(bundle);
            manager.sendMessage(message);
        }
    }

}
