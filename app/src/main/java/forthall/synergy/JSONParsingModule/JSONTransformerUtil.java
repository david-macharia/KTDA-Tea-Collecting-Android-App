package forthall.synergy.JSONParsingModule;

import android.util.Log;

import forthall.synergy.DatabaseObjects.Bag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by MY-HOMEDESKTOP on 3/7/2017.
 */
public class JSONTransformerUtil {

    public static String getJSONDdata(List<Bag> bags) throws JSONException {
        Log.i("KTDA_LOG", "Starting to pass bags to JSON");
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        JSONArray bagsarray= new JSONArray();
        for(Bag bag:bags){
            JSONObject jsonbag= new JSONObject();
                    jsonbag.put("growerId",bag.getGrowerId());
                    jsonbag.put("driverId",bag.getDriverId());
            jsonbag.put("weight",bag.getNetWeight());
            jsonbag.put("centerId",bag.getCenterId());
            jsonbag.put("date",dateFormat.format(new Date()));
            bagsarray.put(jsonbag);
        }
return bagsarray.toString();
    }
}
