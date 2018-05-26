package forthall.synergy.dansonmbuthia.application.ktdainterfaces;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by MY-HOMEDESKTOP on 3/17/2017.
 */
public class ServerConnectionValidater extends AsyncTask<ImageView [],Integer,Integer> {
    ImageView views[];
    private  boolean validating=true;
    int i=0;
    ServerConnectionValidater(ImageView [] view){
        this.views=view;
    }
    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }
public void stopAsynThread(){
    validating= false;
}
    @Override
    protected Integer doInBackground(ImageView[]... params) {
        while(validating){
            for(int i=0;i<params[0].length;i++){
                try {
                    Thread.sleep(300);
                    publishProgress(i);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        return 0;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
       for(ImageView view:views){
           view.setVisibility(View.INVISIBLE);
       }
        for(int i=0;i<views.length;i++){
            if(i==values[0]){
                views[values[0]] .setVisibility(View.VISIBLE);
            }else{

            }
        }


        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }
}

