package forthall.synergy.backgroundthreadclass;

import android.os.Bundle;
import android.os.Message;

import forthall.synergy.dansonmbuthia.application.ktdainterfaces.LogIn;
import forthall.synergy.finalfields.ServerConnection;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by MY-HOMEDESKTOP on 2/23/2017.
 */
//run log in process on a background thread
public class KtdaLogin  implements Runnable {
private String log_url;
    private String username;
    private String password;
    private StringBuilder builder;
    private String post_data;
    private Bundle bundle;
    private Message message;
    protected URL url;

    protected HttpURLConnection urlconnection;
    protected BufferedReader buffered;
    protected InputStreamReader inputStreamReader;

    protected String RequestMethod="POST";
    protected int timeout=40000;
    public KtdaLogin(String url,String username,String password)  {

        this.log_url=url;
        this.username=username;
        this.password=password;
    }

    protected  void setDataReuestMethod(String method){
        this.RequestMethod=method;
    }
    protected  String getDataRequestMethod(){
        return this.RequestMethod;
    }
    protected  void setTimeout(int timeout){
        this.timeout=timeout;
    }
    protected int getTimeOut(){
        return timeout;
    }
    //start thread to query authenticity from remote factory server
    @Override
    public void run() {
        bundle= new Bundle();
        message= new Message();

        builder = new StringBuilder("");
        try {
            post_data = ";" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + ";" +
                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            url = new URL(log_url + post_data);
            doConnectToServerAt(new URL(log_url + post_data));

            String line = "";
            if (buffered != null) {
                while ((line = buffered.readLine()) != null) {
                    builder.append(line);
                }
                urlconnection.disconnect();
            }
           }catch (ConnectException e){
            bundle.putString("LinkBroken", ServerConnection.LINK_BROKEN);
            }catch(SocketTimeoutException e){
                bundle.putString("ServerConnectionTimeOut",ServerConnection.TIME_OUT);

            message.setData(bundle);
            LogIn.handler.sendMessage(message);
            return;

            }catch(IOException e){

                e.printStackTrace();
            }

        bundle.putString("Message", builder.toString());

            try {
                if(buffered!=null) {
                    buffered.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        message.setData(bundle);
       LogIn.handler.sendMessage(message);

        urlconnection.disconnect();

    }
    //specify the URL to remote site
    //read the response

    protected  InputStreamReader  doConnectToServerAt(URL url) throws SocketTimeoutException,ConnectException {
        this.url = url;
        try {

            urlconnection = (HttpURLConnection) url.openConnection();
            urlconnection.setDoOutput(true);
            urlconnection.setDoInput(true);
            urlconnection.setRequestMethod(RequestMethod);

            urlconnection.setConnectTimeout(timeout);


            ////////////////////////response code//////////////////////////////
            inputStreamReader = new InputStreamReader(urlconnection.getInputStream());
            buffered = new BufferedReader(inputStreamReader);

        }catch (SocketTimeoutException e){
            throw new SocketTimeoutException();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return inputStreamReader;
    }
}
