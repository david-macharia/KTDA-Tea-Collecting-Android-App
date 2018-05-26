package forthall.synergy.WorkingWithUr;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by MY-HOMEDESKTOP on 2/1/2017.
 */
public class urls  extends AsyncTask<String,Void,String>{
    private Context context;
    private URL url;
    private String data,data2;
    private URLConnection connections;

    public urls(Context context
    ){
        try{
          url  = new URL("http://192.168.100/log_in.php");

            connections=url.openConnection();
            connections.setDoInput(true);
            connections.setDoOutput(true);
            this.context=context;

        }catch(IOException e){
e.printStackTrace();
        }
    }
public void sendInformationToServer(String username ,String password){
    String  dataencoded="";
    OutputStreamWriter streamwriter;

    try
    {
        streamwriter= new OutputStreamWriter(connections.getOutputStream());
        data= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
        data2=URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
        dataencoded=data+data2;
        streamwriter.write(dataencoded);
        streamwriter.flush();


    }catch(IOException e){
        e.printStackTrace();
    }
    }
    public String receiveInformationFromTheServer(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connections.getInputStream()));
           String info="";
            while((info+=bufferedReader.readLine())!=null){
                return info;
            }

            return "failed";
        }catch(IOException e){
                e.printStackTrace();
        }
        return "nothing";
    }


    @Override
    protected String doInBackground(String... params) {
        this.sendInformationToServer(params[0],params[1]);
        return "result";
    }
@Override
    protected void onPostExecute(String result){

}

}
