package forthall.synergy.postingdata;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.URL;

public class VolleySingletone {
    private static RequestQueue requestQueue;
    private URL resourceurl;
    private static Context context;
    public VolleySingletone(Context ctx){
        context=ctx;
    }
    public static synchronized RequestQueue getVolleyInstance(Context context) {
        if (requestQueue == null) {
            return requestQueue = Volley.newRequestQueue(context);
        } else {
            return requestQueue;
        }
    }
    public void setUrl(URL url){
        resourceurl=url;

    }
    public URL getUrl(){
        return resourceurl;
    }
}
