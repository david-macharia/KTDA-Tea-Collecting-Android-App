package forthall.synergy.JSONParsingModule;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by MY-HOMEDESKTOP on 3/14/2017.
 */

public class JSONFIleParsing {
    String Jsonfile;
   JSONObject object;
    JSONArray jsonArray;

    HashMap<String,String> dataFound;
   public  JSONFIleParsing(String jsonfile,String [] key_to_data){
      this.Jsonfile=jsonfile;
        try {
            dataFound = new HashMap<>();
            Log.e("Number of key:", key_to_data[0]);
            Log.e("File to parse", jsonfile.toString());
                jsonArray = new JSONArray(jsonfile);

            object=jsonArray.getJSONObject(0);
                for (int i = 0; i < 1; i++) {
                    String data = object.getString(key_to_data[i]);
                    Log.e("File to parse", data);
                    dataFound.put(key_to_data[i], data);
                }
            } catch (JSONException e) {
                 return;

            }
        }

    public HashMap<String,String> getHashedData(){
        return  dataFound;
    }
}

