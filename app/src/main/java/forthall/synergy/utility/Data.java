package forthall.synergy.utility;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import forthall.synergy.DatabaseObjects.BuyingCenter;
import forthall.synergy.DatabaseObjects.Center;
import forthall.synergy.DatabaseObjects.Factory;
import forthall.synergy.DatabaseObjects.Grower;
import forthall.synergy.XmlParsingModule.XMLPersingModel;
import forthall.synergy.XmlParsingModule.XmlCenterParser;
import forthall.synergy.XmlParsingModule.XmlFactoryParser;
import forthall.synergy.XmlParsingModule.XmlGrowerParser;
import forthall.synergy.postingdata.VolleySingletone;
import forthall.synergy.problem.DataError;

/**
 * Created by MY-HOMEDESKTOP on 5/10/2017.
 */
public class Data {
    private ArrayAdapter<String> dataRetrived;
    private String RemoteUrl;
    private DataRequestType DataTagName;
    private Handler handler;
    private XMLPersingModel model;
    private Context context;
    private LocalDbUtility localDbUtility;
    public Data(Context context) {
        this.model=model;
        this.context=context;
        localDbUtility= new LocalDbUtility( new LocalDbContent(context));
    }

    public Data(Context context,String remoteUrl, DataRequestType dataTagName) {
        this.model=model;
        this.context=context;
        RemoteUrl = remoteUrl;
        DataTagName = dataTagName;
        localDbUtility= new LocalDbUtility( new LocalDbContent(context));
    }
public LocalDbUtility getLocalDbUtility(){
    return localDbUtility;
}
public void setContext(Context context){
    this.context=context;
}



    public String getRemoteUrl() {
        return RemoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        RemoteUrl = remoteUrl;
    }

    public DataRequestType getDataTagName() {
        return DataTagName;
    }

    public void setDataTagName(DataRequestType dataTagName) {
        DataTagName = dataTagName;
    }
    //called when we want to send data back
    public void onDataReceveid(ArrayAdapter<String> dataReceived,DataRequestType dataRequestType){

    }
    public void onDataErrorReceived(DataError dataerror){

    }
    public void onJSONReceived(JSONArray jsonArrayRequest){

    }
    public void onListReceived(List<?> data){

    }
    synchronized public void runDataSync() {

        switch (DataTagName) {
            case FACTORY: {

                List<Factory> factories;
                    if (localDbUtility.getAllFactories() != null) {
                        Log.e(" Local db service","loading factories from local db");
                        factories = localDbUtility.getAllFactories();
                        onListReceived(factories);
                        onDataReceveid(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, getStringFactoryItem(factories)), DataRequestType.FACTORY);


                } else {
                        Log.e("Internet db service", "loading factories from remote database");
                        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(RemoteUrl, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                List<Factory>  factories= new ArrayList<>();
                              for(int i=0;i<response.length();i++){
                                  try {
                                      JSONObject jsonObject=response.getJSONObject(i)  ;
                                      Factory factory= new Factory();
                                      factory.setFactoryId(jsonObject.getString("factoryId"));
                                      factory.setFactoryName(jsonObject.getString("factoryName"));
                                      factory.setnumberOfCenters(jsonObject.getInt("noOfCenters"));
                                      factories.add(factory)    ;

                                  } catch (JSONException e) {
                                      e.printStackTrace();
                                  }
                              }
                                localDbUtility.cleanLocalBuffer();
                                localDbUtility.insertAllFactories(factories);
                                onListReceived(factories);
                                onDataReceveid(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, getStringFactoryItem(factories)), DataRequestType.FACTORY);


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                onDataErrorReceived(new DataError(error.getMessage()));
                            }
                        });
                       VolleySingletone.getVolleyInstance(context.getApplicationContext()).add(jsonArrayRequest)  ;
                }
            }
            break;
            case CENTER: {
                   if(localDbUtility.getAllCenters()!=null){
                       Log.e(" Local db service","loading centers from local db");
                        List<BuyingCenter> buyingCenterList= localDbUtility.getAllCenters();

                        onListReceived(buyingCenterList);
                       onDataReceveid(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, getStringCenterItem(buyingCenterList)), DataRequestType.CENTER);

                        } else {
                       Log.e("Internet db service", "loading centers from remote database");
                       Log.e("@URL",getRemoteUrl());
                       final List<BuyingCenter> buyingCenterList= new ArrayList<>();
                       JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(RemoteUrl, new Response.Listener<JSONArray>() {
                           @Override
                           public void onResponse(JSONArray response) {

                               for(int i=0;i<response.length();i++){
                                   try {
                                       JSONObject jsonObject=response.getJSONObject(i);
                                       BuyingCenter buyingCenter= new BuyingCenter();
                                       buyingCenter.setNumberOfGrowers(jsonObject.getInt("noOfGrowers"));
                                       buyingCenter.setFactoryId(jsonObject.getString("factoryId"));
                                       buyingCenter.setCenterId(jsonObject.getString("centerId"));
                                       buyingCenter.setCenterName(jsonObject.getString("centerName"));
                                       buyingCenterList.add(buyingCenter);


                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }
                               }
                               localDbUtility.insertAllCenters(buyingCenterList);
                               onListReceived(buyingCenterList);
                               onDataReceveid(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, getStringCenterItem(buyingCenterList)), DataRequestType.CENTER);
                           }
                       }, new Response.ErrorListener() {
                           @Override
                           public void onErrorResponse(VolleyError error) {
                               onDataErrorReceived(new DataError(error.getMessage()));
                           }
                       });
                       VolleySingletone.getVolleyInstance(context.getApplicationContext()).add(jsonArrayRequest)  ;


                        }



            }
            break;
            case GROWER:

                if(localDbUtility.getAllGrowers()!=null){
                    List<Grower> growerList=localDbUtility.getAllGrowers();
                    onListReceived(growerList);
                    onDataReceveid(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, getStringGrowerItem(growerList)), DataRequestType.GROWER);
                }else{
                    Log.e("Internet db service", "loading growers from remote database");
                    Log.e("@URL",RemoteUrl);

                    JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(RemoteUrl, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            List<Grower> growerList= new ArrayList<>();
                            for(int i=0;i<response.length();i++){
                                try {
                                    JSONObject jsonObject=response.getJSONObject(i);
                                    Grower grower= new Grower();
                                    grower.setGrowerName(jsonObject.getString("firstName")+" "+jsonObject.getString("middleName")+" "+jsonObject.getString("lastName"));
                                    grower.setPhoneNumber(jsonObject.getString("phoneNumber"));
                                    grower.setCenterId(jsonObject.getString("centerId"));
                                    grower.setEmail(jsonObject.getString("email"));
                                    grower.setGrowerId(jsonObject.getString("growerId"));
                                    growerList.add(grower);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            localDbUtility.insertAllGrowers(growerList);

                            onListReceived(growerList);
                            onDataReceveid(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, getStringGrowerItem(growerList)), DataRequestType.GROWER);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            onDataErrorReceived(new DataError(error.getMessage()));
                        }
                    });
                    VolleySingletone.getVolleyInstance(context.getApplicationContext()).add(jsonArrayRequest)  ;

                }




                break;
            default:
                    JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(getRemoteUrl(),new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray jsonArray) {
                            onJSONReceived(jsonArray);
                        }
                    },new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });
                VolleySingletone.getVolleyInstance(context.getApplicationContext()).add(jsonArrayRequest)  ;

                break;


        }


    }

    public static   List<String> getStringFactoryItem(List<Factory> factories) {

        List<String> factories_names = new ArrayList<String>();
        for (Factory factory : factories) {
            factories_names.add(factory.getFactoryName());
        }
        return factories_names;
    }

    public static  List<String> getStringCenterItem(List<BuyingCenter> buyingCenters) {

        List<String> center_names = new ArrayList<String>();
        for (BuyingCenter buyingCenter : buyingCenters) {
            center_names.add(buyingCenter.getCenterName());
        }
        return center_names;
    }

    public static  List<String> getStringGrowerItem(List<Grower> growers) {

        List<String> grower_names = new ArrayList<String>();
        for (Grower grower : growers) {
            grower_names.add(grower.getGrowerName());
        }
        return grower_names;
    }

}
