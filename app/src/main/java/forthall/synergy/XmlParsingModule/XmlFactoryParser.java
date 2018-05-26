package forthall.synergy.XmlParsingModule;

import android.util.Log;

import forthall.synergy.DatabaseObjects.Factory;
import forthall.synergy.dansonmbuthia.application.ktdainterfaces.ChooseBuyer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MY-HOMEDESKTOP on 2/25/2017.
 */
public class XmlFactoryParser extends  XMLPersingModel implements Runnable ,XMLPersingModelInterface{
List<Factory> factories= new ArrayList<Factory>();

    public XmlFactoryParser(String url ) {
        super(url);


    }

    @Override
    public List<?> startParsing() {
        String name="null";
        String text="";
        int eventtype= 0;
        Factory factory = null;
        try {
            eventtype = xmlPullParser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        while(eventtype!= XmlPullParser.END_DOCUMENT){
            name =xmlPullParser.getName();
            switch (eventtype){
                case XmlPullParser.START_TAG:
                if(name.equalsIgnoreCase("factory")){
                    factory= new Factory();
                    factories.add(factory);
                }
                    break;
                case XmlPullParser.TEXT:
                    text=xmlPullParser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    if(name.equalsIgnoreCase("factoryId")){
                        factory.setFactoryId(text);
                    }
                    else if(name.equalsIgnoreCase("factoryName")){
                        factory.setFactoryName(text);
                    }else if(name.equalsIgnoreCase("noOfCenters")){
                        factory.setnumberOfCenters(Integer.valueOf(text));
                    }
                    break;
                default:
                {
                    break;
                }
            }
           try {
                eventtype=xmlPullParser.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
               e.printStackTrace();
           }
        }


        return factories;
    }
public  Factory getFactory(String name){
    for(Factory factory:factories){
        if(factory.getFactoryName().equalsIgnoreCase(name)){
            return factory;
        }
    }


    return null;
}

    @Override
    public void run() {

       super.run();


        ///////////////////processing the xml file passed to us///////////////////////

        bundle.putStringArrayList("factories", (ArrayList<String>) startParsing());

        message.setData(bundle);
        getMessageHandler().sendMessage(message);


    }

}
