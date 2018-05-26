package forthall.synergy.utility;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import forthall.synergy.DatabaseObjects.BuyingCenter;
import forthall.synergy.DatabaseObjects.Factory;
import forthall.synergy.DatabaseObjects.Grower;
import forthall.synergy.XmlParsingModule.XMLPersingModel;
import forthall.synergy.XmlParsingModule.XmlCenterParser;
import forthall.synergy.XmlParsingModule.XmlFactoryParser;
import forthall.synergy.XmlParsingModule.XmlGrowerParser;

/**
 * Created by MY-HOMEDESKTOP on 5/10/2017.
 */
public class DatabaseProxy {
    private Context context;
    private  List<Data> datalist= new ArrayList<>();


    public DatabaseProxy(Context context){
        this.context=context;



    }

    public void enqueueDataReceiver(Data data) {
        datalist.add(data);
        data.setContext(context);
      data.runDataSync();

    }


}
