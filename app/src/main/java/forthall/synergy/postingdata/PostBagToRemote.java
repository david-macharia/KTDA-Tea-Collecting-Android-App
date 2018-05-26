package forthall.synergy.postingdata;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by MY-HOMEDESKTOP on 3/8/2017.
 */
public class PostBagToRemote extends AbstractRemotePoster {
    PostMetadata metadata;

    protected HttpURLConnection urlconnection;
    protected BufferedReader buffered;
    protected InputStreamReader inputStreamReader;
    Handler handler;
    public PostBagToRemote(PostMetadata metadata,Handler handler) {
        super(metadata);
        this.handler=handler;
        this.metadata=metadata;
    }

    @Override
    protected void getResponsesFromServer(String response) {
   if(handler!=null){
       Log.i("error", "Got response from the server");
       Bundle bundle = new Bundle();
       bundle.putString("poster_message", response);
       Log.i("File","File being worked on"+postMetadata.getFileName());
      if(postMetadata.getFileName()!=null){
          bundle.putString("file_name",postMetadata.getFileName());

      }

       Message message= new Message();
       message.setData(bundle);
        handler.sendMessage(message);

   }else{
       Log.e("Server Resposes", response);
       super.getResponsesFromServer(response);
   }
    }
}
