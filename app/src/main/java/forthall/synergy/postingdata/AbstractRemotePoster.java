package forthall.synergy.postingdata;

import android.os.Bundle;
import android.util.Log;

import forthall.synergy.finalfields.ServerConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by MY-HOMEDESKTOP on 3/14/2017.
 */
public abstract class AbstractRemotePoster implements Runnable {
    protected Bundle bundle;
    protected PostMetadata postMetadata;
    protected HttpURLConnection urlconnection;
    protected BufferedReader buffered;
    protected InputStreamReader inputStreamReader;
    public AbstractRemotePoster(PostMetadata metadata){
        this.postMetadata=metadata;
        bundle= new Bundle();
    }

    @Override
    public void run() {
        URL url= null;
        try {
            url = new URL(postMetadata.getUrl());
            doConnectToServerAt(url);

        } catch (MalformedURLException | SocketTimeoutException e) {
            if(e.toString().equals("java.net.SocketTimeoutException")){

                getResponsesFromServer(ServerConnection.TIME_OUT);

                urlconnection.disconnect();
            }
        }

    }
    protected  InputStreamReader  doConnectToServerAt(URL url) throws SocketTimeoutException {

        try {

            urlconnection = (HttpURLConnection) url.openConnection();
            urlconnection.setDoOutput(true);
            urlconnection.setDoInput(true);
            urlconnection.setRequestMethod("POST");
            urlconnection.setRequestProperty("Content-Type", "application/json");
            urlconnection.setConnectTimeout(10000);
            OutputStream outputStream= urlconnection.getOutputStream();
            outputStream.write(postMetadata.getJSONEDBags().getBytes());
            outputStream.flush();
            outputStream.close();

            if(urlconnection.getResponseMessage().equals("OK")){
                inputStreamReader = new InputStreamReader(urlconnection.getInputStream());
                buffered = new BufferedReader(inputStreamReader);
                String line="";
                while((line=buffered.readLine())!=null){
                    if("TRANSACTION_COMPLETE".equals(line)){
                        urlconnection.disconnect();
                        getResponsesFromServer(line);
                        urlconnection.disconnect();
                    }else if("TRANSACTION_INCOMPLETE".equals(line)){
                        getResponsesFromServer(line);
                        urlconnection.disconnect();
                    }
                }
            }else{
                Log.e("connection status",urlconnection.getResponseMessage());
                urlconnection.disconnect();
            }
            ////////////////////////response code//////////////////////////////
            //;

        }catch (SocketTimeoutException e) {
            Log.e("timeout","Server Timeout");

            throw new SocketTimeoutException();
        }
        catch (IOException e) {
            //e.printStackTrace();
        }

        urlconnection.disconnect();
        return inputStreamReader;
    }
    protected void getResponsesFromServer(String response){

    }
}
