package forthall.synergy.postingdata;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import forthall.synergy.DatabaseObjects.Grower;

import forthall.synergy.JSONParsingModule.JSONFIleParsing;
import forthall.synergy.finalfields.AppPreferences;
import forthall.synergy.finalfields.ServerConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by MY-HOMEDESKTOP on 3/13/2017.
 */
public class BroadCaster extends BroadcastReceiver {
    protected ConnectivityManager connectivityManager;
    protected static   String previusNetwork;
    NetworkInfo networkInfo ;
     static boolean connected=false;
   static SharedPreferences sharedPreferences;
    public BroadCaster(){
        
    }
    public BroadCaster(Context context){
        sharedPreferences=context.getSharedPreferences(AppPreferences.PREFERENCE_NAME,Context.MODE_PRIVATE);
        Log.i("BroadCaster","received network state intent");
        isConnected(context);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences=context.getSharedPreferences(AppPreferences.PREFERENCE_NAME,Context.MODE_PRIVATE);
        Log.i("BroadCaster","received network state intent");
        Toast.makeText(context,"received broadcast",Toast.LENGTH_SHORT).show();
        isConnected(context);
    }
    public void isConnected(Context context){
        Log.i("called","This one");
        connectivityManager=(ConnectivityManager)context.getSystemService(Application.CONNECTIVITY_SERVICE);

        networkInfo=connectivityManager.getActiveNetworkInfo();
        if((networkInfo!=null)){
            Log.i("Status",networkInfo.getState().name());
            switch (networkInfo.getState()){
                //evalute only when connected to a network
                case CONNECTED: {
                    Log.i("Connection","connected");
               if(!connected){
                   connected=true;
                   Log.i("Status","connected here");
                   Log.i("loggedInfo", "we can test server since you aint connected");
                   Log.i("NetworkManager","Launched");
                   Log.i("ServiceTester","lauched consequently");
                   new  NetworkManager(new ServiceTester(),context).start();
               }else{
                   connected =false;
               }

                    break;
                }

                default:{
                    Log.i("flag","set to false");
              connected =false;
                    break;}






            }
        }else{
            Log.i("Status","you are not conneted");
            connected =false;
        }




    }



    static class ServiceTester extends Handler
    {
        PostMetadata postMetadata;
        int counter=0;
        String jsonfile;
        @Override
        public void handleMessage(Message msg) {
            Log.i("ServerTester","received a message"+msg.getData().getString("CONNECTION"));

            if(msg.getData().getString("CONNECTION")!=null){
            if( msg.getData().getString("CONNECTION").equals(ServerConnection.REACHABLE)){
                Log.e("Status", "you can now connect send data to the server");
                Log.i("KTDA_LOG", "Server can be reached");
                if(counter<1) {
                    ReadJSONBackUp backUp = new ReadJSONBackUp();
                    backUp.start();
                    counter++;
                }



            }}else{
                Log.e("BroadCastHandle:", msg.getData().getString("poster_message"));
                Log.e("BroadCastHandle says:", msg.getData().getString("file_name")+"should be deleted");
                FileDeleter.deleteAFile(msg.getData().getString("file_name"));

            }
            super.handleMessage(msg);
        }
static class FileDeleter{
    public static boolean deleteAFile(String fileName){
        File file= checkAvailability();
       String stringfilepath= file.getAbsoluteFile()+"//"+fileName;
        File filepath= new File(stringfilepath);
        if(filepath.delete()){
           Log.i("BroadCaster","Deleted the file"+fileName) ;
            return true;
        }else{
            Log.i("BroadCaster","Could not delete the file"+fileName) ;
            Log.i("BroadCaster","Filepath "+filepath.getAbsolutePath());
            return  false;
        }
    }

}
        class ReadJSONBackUp extends Thread{
            Grower info;
       String JSonFile;
            public boolean lock=true;
            @Override
            public void run() {
                File file=checkAvailability();
                if(file!=null) {

                        for (File single_file : file.listFiles()) {
                            Log.e("File available:", "reading started..");
                            Log.e("Reading",single_file.getName());
                            JSonFile=readJsonFile( single_file);

                            JSONFIleParsing jsonfIleParsing=  new JSONFIleParsing(JSonFile, new String[]{"growerId","driverId","weight","centerId","date"});

                            HashMap<String,String> data=jsonfIleParsing.getHashedData();
                           String single_data=data.get("growerId");


            postMetadata = new PostMetadata("http://"+sharedPreferences.getString("ip_address","0.0.0.0")+":"+sharedPreferences.getString("port_number","8080")+"/KTDARestservice/ktda_api/grower/bags", JSonFile,single_data);
                            postMetadata.setFileName(single_file.getName());
               new Thread( new PostBagToRemote(postMetadata,ServiceTester.this)).start();


                        }

                }

                super.run();

            }
            public String getJsonFile(){

                    return JSonFile;


            }
            //read json from a ceratin file and reaturn a sring formarted data of the file
            private String readJsonFile(File file){
                String line;
                try {
                    InputStreamReader inputStreamReader= new InputStreamReader( new FileInputStream(file));
                    BufferedReader reader= new BufferedReader(inputStreamReader);
                    StringBuilder builder= new StringBuilder();

                    while((line=reader.readLine())!=null){
                        builder.append(line+"\n");
                    }
                    return builder.toString();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "";
            }



    }
        //test if external storage is mounted or exists
        private static boolean isExternalStorageMounted() {
            String state = Environment.getExternalStorageState();
            if ((state.equals(Environment.MEDIA_MOUNTED)) || (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))) {
                return true;
            }
            return false;
        }
        private static File checkAvailability() {
            if (isExternalStorageMounted()) {
                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                if (file.exists()) {

                    File file_PendingGrowersData = new File(Environment.getExternalStorageDirectory() + "/" + file.getName() + "/.PendingGrowersData");

                    if (file_PendingGrowersData.exists()) {
                        //do something on that directory is it exist
                        return  file_PendingGrowersData;
                    } else {
                        // make a pending factory data if it does not exist
                        if (file_PendingGrowersData.mkdir()) {
                            return  file_PendingGrowersData;
                        } else {
                            Log.e("Directory", "Could not make file_pendingdirectory");
                            return null;
                        }
                    }
                } else {
                    if (file.mkdir()) {
                        File file_PendingGrowersData = new File(Environment.getExternalStorageDirectory() + "/" + file.getName() + "/.PendingGrowersData");

                        if (file_PendingGrowersData.exists()) {
                            //do something on that directory is it exist
                            return  file_PendingGrowersData;
                        } else {
                            // make a pending factory data if it does not exist
                            if (file_PendingGrowersData.mkdir()) {
                                Log.e("file_PendingFactoryData", "directory made");
                                return  file_PendingGrowersData;

                            } else {
                                Log.e("Directory", "Could not make file_pendingdirectory");
                                return  null;

                            }
                        }
                    } else {
                        Log.e("Directory", "Could not make Doc directory");
                        return  null;
                    }
                }
            } else {
                Log.e("Error", "Could not write!! strorage problem");
                return  null;

            }

        }
    }
    class NetworkManager extends  Thread{
       Context context;
        Handler manager;
        Bundle bundle;
        NetworkManager(Handler handler,Context con){
            this.manager=handler;
            context =con;

        }
        public void isreachable(Context context)  {
            HttpURLConnection httpURLConnection=null;
            ConnectivityManager connectivityManager= (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info=connectivityManager.getActiveNetworkInfo();
            if((info!=null)&&info.isConnected()){
                Log.i("preogresslog", "network ailable");
                try {
                    URL url = new URL("http://"+sharedPreferences.getString("ip_address","0.0.0.0")+":"+sharedPreferences.getString("port_number","8080"));
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    Log.i("preogresslog","openned the port");
                    httpURLConnection.setConnectTimeout(10000);
                    httpURLConnection.connect();

                    if (httpURLConnection.getResponseCode() == 200) {
                        bundle = new Bundle();
                        bundle.putString("CONNECTION", "REACHABLE");
                        Message message = new Message();
                        message.setData(bundle);
                        manager.sendMessage(message);
                    }
                }catch (SocketTimeoutException |ConnectException e) {
                    bundle = new Bundle();
                    bundle.putString("CONNECTION","TIME_OUT");
                    Message message = new Message();
                    message.setData(bundle);
                    manager.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                bundle = new Bundle();
                bundle.putString("CONNECTION","not connected");
                Message message = new Message();
                message.setData(bundle);
                manager.sendMessage(message);
            }
        }

        @Override
        public  void run(){


                isreachable(context);


            }
        }

    }



