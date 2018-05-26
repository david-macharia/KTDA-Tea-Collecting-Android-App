package forthall.synergy.XmlParsingModule;

import forthall.synergy.DatabaseObjects.BuyingCenter;
import forthall.synergy.dansonmbuthia.application.ktdainterfaces.ChooseBuyer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by MY-HOMEDESKTOP on 2/25/2017.
 */
public class XmlCenterParser extends XMLPersingModel implements XMLPersingModelInterface {
     List<BuyingCenter> buyingCenters;
    public XmlCenterParser(String url) {
        super(url);
        buyingCenters = new ArrayList<BuyingCenter>();
    }

    @Override
    public List<?> startParsing() {
    BuyingCenter buyingCenter = null;
        String name="null";
        String text="";
        int eventtype= 0;
        if(xmlfile==null) {
         return buyingCenters;
        }else {
            try {
                eventtype = xmlPullParser.getEventType();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            while (eventtype != XmlPullParser.END_DOCUMENT) {
                name = xmlPullParser.getName();
                switch (eventtype) {
                    case XmlPullParser.START_TAG:
                        if (name.equalsIgnoreCase("buyingCenter")) {
                            buyingCenter = new BuyingCenter();
                            buyingCenters.add(buyingCenter);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = xmlPullParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equalsIgnoreCase("centerId")) {
                            buyingCenter.setCenterId(text);
                        } else if (name.equalsIgnoreCase("centerName")) {
                            buyingCenter.setCenterName(text);
                        } else if (name.equalsIgnoreCase("factoryId")) {
                            buyingCenter.setFactoryId(text);
                        } else if (name.equalsIgnoreCase("noOfGrowers")) {
                            buyingCenter.setNumberOfGrowers(Integer.valueOf(text));
                        }
                        break;

                    default: {
                        break;
                    }
                }
                try {
                    eventtype = xmlPullParser.next();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return buyingCenters;
        }

    }

    @Override
    public void run() {
        super.run();
       bundle.putStringArrayList("buyingCenters", (ArrayList<String>) startParsing());
        message.setData(bundle);
        getMessageHandler().sendMessage(message);
    }
    public BuyingCenter getCenter(String name){
        for(BuyingCenter buyingCenter : buyingCenters){
            if(buyingCenter.getCenterName().equalsIgnoreCase(name)){
                return buyingCenter;
            }
        }
        return null;
    }
    public int getAllCenters(){
        return buyingCenters.size();
    }
}
