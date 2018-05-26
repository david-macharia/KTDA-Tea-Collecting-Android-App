package forthall.synergy.dansonmbuthia.application.ktdainterfaces;

import android.app.ProgressDialog;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.forthall.dansonmbuthia.application.R;
import forthall.synergy.DatabaseObjects.BuyingCenter;
import forthall.synergy.DatabaseObjects.Factory;
import forthall.synergy.DatabaseObjects.Grower;


import forthall.synergy.XmlParsingModule.XmlCenterParser;
import forthall.synergy.XmlParsingModule.XmlFactoryParser;
import forthall.synergy.XmlParsingModule.XmlGrowerParser;
import forthall.synergy.finalfields.AppPreferences;
import forthall.synergy.problem.DataError;
import forthall.synergy.utility.ColumnCostants;
import forthall.synergy.utility.Data;
import forthall.synergy.utility.DataRequestType;
import forthall.synergy.utility.DatabaseProxy;
import forthall.synergy.utility.LocalDbContent;
import forthall.synergy.utility.LocalDbUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
// you choose the name of the grower based on
//the factory
//the buying center
//and the grower id number

public class ChooseBuyer extends AppCompatActivity {
    private Spinner factoryspinner;
    private ArrayAdapter<String> factorysString;
    private ArrayAdapter<String> centersString;
    private ArrayAdapter<String> growerString;
    private Spinner centerspinner;
private DatabaseProxy databaseProxy;
    private Spinner buyerspinner;
    private Button go_to_buying;
    public static Handler factory_handler, center_handler, buyer_handler;
    private Factory selectedFactory;
    private XmlFactoryParser xmlFactoryparser;
    private XmlGrowerParser xmlGrowerParser;
    private BuyingCenter selectedBuyingCenter;
    private TextView result;
public static boolean iscollection=false;
    private ProgressDialog loading;

    //****************************************************************
SharedPreferences preferences;
    private List<Factory> allFactories;
    private List<BuyingCenter> allBuyingCenters;
    private  List<Grower> someGrowersInCenter ;
private Data data;
    //****************************************************************
    String center;
             Bundle bundle;
    private XmlCenterParser xmlCenterParser;
private LocalDbUtility localDbUtility;
//Remember the intent have the driver details from logging
    //so don't fetch them again

    @Override
    public void onCreate(Bundle savedinstancebudles) {
        super.onCreate(savedinstancebudles);
        setContentView(R.layout.choosebuyer);
        factoryspinner = (Spinner) findViewById(R.id.factoryspinner);
        factoryspinner.setOnItemSelectedListener(new FactorySelectedListener());
        databaseProxy=new DatabaseProxy(getApplicationContext());
        localDbUtility= new LocalDbUtility( new LocalDbContent(getApplicationContext()));
        centerspinner = (Spinner) findViewById(R.id.centerspinner);
        centerspinner.setOnItemSelectedListener(new CenterSelectedListener());
        result = (TextView) findViewById(R.id.result);
        result.setTextColor(Color.rgb(0, 0, 102));
        bundle= new Bundle();
         preferences=getSharedPreferences(AppPreferences.PREFERENCE_NAME,MODE_PRIVATE);
//        bundle.putBoolean("returned?",getIntent().getExtras().getBoolean("returned?"));
        bundle.putString("driver_name", preferences.getString("driver_name", "Anonymous"));

        buyerspinner = (Spinner) findViewById(R.id.buyerspinner);
        buyerspinner.setOnItemSelectedListener(new GrowerSelectedListener());
        go_to_buying = (Button) findViewById(R.id.continue_to_buying);
        go_to_buying.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                bundle.putString("factory",factoryspinner.getSelectedItem().toString());
                                                bundle.putString("center",centerspinner.getSelectedItem().toString());
                                                bundle.putString("buyer",buyerspinner.getSelectedItem().toString());
///////////////////////////////////////////////////////////////////
                                                Intent intent = new Intent(getApplicationContext(), Collecting.class);
                                               intent.putExtras(bundle);
                                                startActivity(intent);


                                            }
                                        }


        );
        final String factory_url ="http://"+preferences.getString("ip_address","0.0.0.0")+":"+preferences.getString("port_number","8080")+"/KTDARestservice/ktda_api/factories/";
        loading = ProgressDialog.show(this, "Fetching data", "Collecting data from server", true,false);

        data= new Data(getApplicationContext()){
            @Override
            public void onDataReceveid(ArrayAdapter<String> dataReceived,DataRequestType requestType) {
                if(requestType.equals(DataRequestType.FACTORY)) {
                    dataReceived.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                    factoryspinner.setAdapter(dataReceived);

                    super.onDataReceveid(dataReceived, requestType);
                }
            }

            @Override
            public void onListReceived(List<?> data) {
                allFactories= (List<Factory>) data;




                super.onListReceived(data);
            }
            @Override
            public void onDataErrorReceived(DataError dataerror) {
                localDbUtility.cleanBufferedCenter();
                localDbUtility.cleanBufferedGrower();
                loading.dismiss();
                centersString = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{"Empty"});

                centersString.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                centerspinner.setEnabled(false);
                centerspinner.setAdapter(centersString);
                buyerspinner.setEnabled(false);

                growerString = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{"Empty"});

                growerString.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                result.setText("");
                buyerspinner.setAdapter(growerString);
                selectedChoices();
                Toast.makeText(getApplicationContext(),"Unable to finish your request",Toast.LENGTH_LONG).show();
                super.onDataErrorReceived(dataerror);
            }
        };
        data.setRemoteUrl(factory_url);
        data.setDataTagName(DataRequestType.FACTORY);

        databaseProxy.enqueueDataReceiver(data);



    }


    class FactorySelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(loading.isShowing()){
                loading.setTitle("Fetching Centers...");
            }else{
                loading.setTitle("Fetching Centers...");
                loading.show();
            }

            selectedFactory= data.getLocalDbUtility().getAllFactories().get(position);
            String sqlStatement="SELECT * FROM "+ ColumnCostants.CENTER_TABLE_NAME +" WHERE "+ColumnCostants.FACTORY_ID+" ='"+selectedFactory.getFactoryId()+"'";
           Log.e("URL",sqlStatement);
            Cursor cursor=data.getLocalDbUtility().getGenericLocalData(sqlStatement);
            if(cursor.moveToFirst()){
                Log.e("Local Cache","loading from local cache");
            }else{
                Log.e("Local Cache","Found nothing from local getting to remote site....");
               data.getLocalDbUtility().cleanBufferedCenter();
                data.getLocalDbUtility().cleanBufferedGrower();
            }

            String centeruri = "http://"+preferences.getString("ip_address","0.0.0.0")+":"+preferences.getString("port_number","8080")+"/KTDARestservice/ktda_api/buying_centers/factory/" + selectedFactory.getFactoryId();
            result.setText("");

            Data data= new Data(getApplicationContext()){
                @Override
                public void onDataReceveid(ArrayAdapter<String> dataReceived,DataRequestType requestType) {
                    if(requestType.equals(DataRequestType.CENTER)) {
                        if(dataReceived.isEmpty()){
                            dataReceived.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            dataReceived.addAll("Empty");
                            centerspinner.setAdapter(dataReceived);
                        }else{
                            dataReceived.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            centerspinner.setAdapter(dataReceived);
                            centerspinner.setEnabled(true);
                        }



                        super.onDataReceveid(dataReceived, requestType);




                    }
                }

                @Override
                public void onListReceived(List<?> data) {
                    allBuyingCenters = (List<BuyingCenter>) data;
                    if (allBuyingCenters.isEmpty()) {
                        loading.dismiss();
                        centersString = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{"Empty"});

                        centersString.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                        centerspinner.setEnabled(false);
                        centerspinner.setAdapter(centersString);
                        buyerspinner.setEnabled(false);

                        growerString = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{"Empty"});

                        growerString.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                        result.setText("");
                        buyerspinner.setAdapter(growerString);
                        super.onListReceived(data);
                    }else{

                    }
                }

                @Override
                public void onDataErrorReceived(DataError dataerror) {
                    localDbUtility.cleanBufferedCenter();
                    localDbUtility.cleanBufferedGrower();
                    loading.dismiss();
                    centersString = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{"Empty"});

                    centersString.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                    centerspinner.setEnabled(false);
                    centerspinner.setAdapter(centersString);
                    buyerspinner.setEnabled(false);

                    growerString = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{"Empty"});

                    growerString.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                    result.setText("");
                    buyerspinner.setAdapter(growerString);
                    selectedChoices();
                    Toast.makeText(getApplicationContext(),"Unable to finish your request",Toast.LENGTH_LONG).show();
                    super.onDataErrorReceived(dataerror);
                }
            };
            data.setRemoteUrl(centeruri);
            data.setDataTagName(DataRequestType.CENTER);

            databaseProxy.enqueueDataReceiver(data);


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class CenterSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(loading.isShowing()){
                loading.setMessage("Loading centers...");
            }else{
                loading.setMessage("Loading centers...");
                loading.show();
            }

Log.e("Selected Center", centerspinner.getSelectedItem().toString());
            selectedBuyingCenter=data.getLocalDbUtility().getCenter(centerspinner.getSelectedItem().toString());
            if (selectedBuyingCenter != null && !selectedBuyingCenter.getCenterName().equals("Empty")) {
                center = "http://"+preferences.getString("ip_address","0.0.0.0")+":"+preferences.getString("port_number","8080")+"/KTDARestservice/ktda_api/growers/center/" + selectedBuyingCenter.getCenterId();
             //   Data data= databaseProxy.getRequestedData(DataRequestType.FACTORY,center);
                final Data data= new Data(getApplicationContext()){
                    @Override
                    public void onDataReceveid(ArrayAdapter<String> dataReceived,DataRequestType requestType) {

                        if(requestType.equals(DataRequestType.GROWER)) {
                            LocalDbUtility   localDbUtility= new LocalDbUtility( new LocalDbContent(getApplicationContext()));
                            List<Grower> someGrowersInCenter = localDbUtility.getAllGrowersInCenter(selectedBuyingCenter.getCenterId());
                            if(someGrowersInCenter==null){
                                loading.dismiss();
                                growerString = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{"Empty"});
                                growerString.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                                result.setText("");
                                buyerspinner.setAdapter(growerString);
                            }else {
                                ArrayAdapter<String> growers = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Data.getStringGrowerItem(someGrowersInCenter));
                                buyerspinner.setAdapter(growers);
                                loading.dismiss();
                            }
                            super.onDataReceveid(dataReceived, requestType);
                        }
                    }

                    @Override
                    public void onListReceived(List<?> data) {
                        LocalDbUtility   localDbUtility= new LocalDbUtility( new LocalDbContent(getApplicationContext()));
                       someGrowersInCenter = localDbUtility.getAllGrowersInCenter(selectedBuyingCenter.getCenterId());
                        if (someGrowersInCenter==null) {

                            buyerspinner.setEnabled(false);
                            growerString = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{"Empty"});
                            growerString.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            result.setText("");
                            buyerspinner.setAdapter(growerString);
                        }else{
                            buyerspinner.setEnabled(true);
                            someGrowersInCenter = localDbUtility.getAllGrowersInCenter(selectedBuyingCenter.getCenterId());
                            ArrayAdapter<String> growers= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,Data.getStringGrowerItem(someGrowersInCenter));
                            growers.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            buyerspinner.setAdapter(growers);
                        }
                        super.onListReceived(data);
                    }
                    @Override
                    public void onDataErrorReceived(DataError dataerror) {
                        localDbUtility.cleanBufferedCenter();
                        localDbUtility.cleanBufferedGrower();
                        loading.dismiss();
                        centersString = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{"Empty"});

                        centersString.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                        centerspinner.setEnabled(false);
                        centerspinner.setAdapter(centersString);
                        buyerspinner.setEnabled(false);

                        growerString = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{"Empty"});

                        growerString.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                        result.setText("");
                        buyerspinner.setAdapter(growerString);
                        selectedChoices();
                        Toast.makeText(getApplicationContext(),"Unable to finish your request",Toast.LENGTH_LONG).show();
                        super.onDataErrorReceived(dataerror);
                    }
                };
                data.setRemoteUrl(center);
                data.setDataTagName(DataRequestType.GROWER);

                databaseProxy.enqueueDataReceiver(data);

            }else{
                result.setText("");
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    private class GrowerSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            loading.dismiss();
            result.setText("");
           selectedChoices();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choosebuyer_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings: {
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);

                break;
            }


        }
        return super.onOptionsItemSelected(item);
    }





    private void selectedChoices() {



        result.append("********" + factoryspinner.getSelectedItem() + " Tea Factory************\n");
        result.append("BuyingCenter    :" + centerspinner.getSelectedItem() + "\n");
        result.append("Grower Name:" + buyerspinner.getSelectedItem() + "\n");
       if(factoryspinner.getSelectedItem().equals("Empty")|centerspinner.getSelectedItem().equals("Empty") |buyerspinner.getSelectedItem().equals("Empty"))
        {
            go_to_buying.setEnabled(false);
        }else {
            getIds(allFactories,allBuyingCenters,someGrowersInCenter);

            go_to_buying.setEnabled(true);
        }
    }

    private void getIds(List<Factory> factories,List<BuyingCenter>buyingCenters,List<Grower> growers){
        ArrayList<String> ids= new ArrayList<>();

        for (BuyingCenter buyingCenter:buyingCenters){
            if(buyingCenter.getCenterName().equals(centerspinner.getSelectedItem().toString())){
                ids.add(buyingCenter.getFactoryId());
                ids.add(buyingCenter.getCenterId());
                break;
            }
        }
        for (Grower grower:growers){
            if(grower.getGrowerName().equals(buyerspinner.getSelectedItem().toString())){
                ids.add(grower.getGrowerId());
                break;
            }
        }

        bundle.putString("factoryId", String.valueOf(ids.get(0)));
        bundle.putString("centerId", String.valueOf(ids.get(1)));
        bundle.putString("growerId", String.valueOf(ids.get(2)));

    }


}