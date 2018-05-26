package forthall.synergy.XmlParsingModule;

import forthall.synergy.DatabaseObjects.Grower;
import forthall.synergy.dansonmbuthia.application.ktdainterfaces.ChooseBuyer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MY-HOMEDESKTOP on 2/26/2017.
 */
public class XmlGrowerParser extends XMLPersingModel implements XMLPersingModelInterface {
    List<Grower> growers;
    public XmlGrowerParser(String url) {
        super(url);

        growers= new ArrayList<Grower>();
    }


    @Override
    public List<?> startParsing() {
        Grower grower = null;
        String name="null";
        String text="";
        String growerFullName="";
        int eventtype= 0;
        if(xmlfile==null) {
            return  growers;
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
                        if (name.equalsIgnoreCase("grower")) {
                            grower = new Grower();
                            growers.add(grower);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = xmlPullParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equalsIgnoreCase("centerId")) {
                          grower.setCenterId(text);

                        } else if (name.equalsIgnoreCase("email")) {
                            grower.setEmail(text);
                        } else if (name.equalsIgnoreCase("firstName")) {

                            grower.setGrowerName(text);
                        } else if (name.equalsIgnoreCase("middleName")) {
                            grower.setGrowerName(text);

                            break;

                        } else if (name.equalsIgnoreCase("growerId")) {
                            grower.setGrowerNo(text);

                        }else if (name.equalsIgnoreCase("lastName")) {
                            grower.setGrowerName(text);
                        }

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

            return growers;
        }

    }

    @Override
    public void run() {
        super.run();
        bundle.putStringArrayList("grower", (ArrayList<String>) startParsing());
        message.setData(bundle);
        getMessageHandler().sendMessage(message);
    }
    public Grower getGrowers(String name){
        for(Grower grower:growers){
            if(grower.getGrowerName().equalsIgnoreCase(name)){
                return grower;
            }
        }
        return null;
    }
}
