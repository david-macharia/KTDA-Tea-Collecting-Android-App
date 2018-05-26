package forthall.synergy.XmlParsingModule;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;


public  class  XMLPersingModel implements Runnable {

    protected Bundle bundle;
    protected Message message;
    private StringBuilder builder;

    protected URL url;

    protected HttpURLConnection urlconnection;
    protected BufferedReader buffered;
    protected InputStreamReader inputStreamReader;
    String xmlfile;
    protected String RequestMethod = "GET";
    protected int timeout = 2;
    protected XmlParser parser;
    public XmlPullParserFactory xmlPullParserFactory;
    public XmlPullParser xmlPullParser;
    public ArrayList<Object> names = new ArrayList<Object>();
  private Handler handler;
    public void setMessageHandler(Handler handler){
        this.handler=handler;
    }
    public Handler getMessageHandler(){
        return handler;
    }
    public XMLPersingModel(String url) {

        try {
            this.url = new URL(url);
            bundle = new Bundle();
            message = new Message();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }


    protected class XmlParser {


        XmlParser() {
            try {

                xmlPullParserFactory = XmlPullParserFactory.newInstance();
                xmlPullParser = xmlPullParserFactory.newPullParser();
                if ((xmlfile = buffered.readLine()) != null) {
                    BufferedReader bufferedReader = new BufferedReader(new StringReader(xmlfile));

                    xmlPullParser.setInput(bufferedReader);

                }else{

                    xmlPullParser.setInput(null);
                }

            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }


        }
    }

    protected InputStreamReader doConnectToServerAt(URL url) throws SocketTimeoutException {

        try {
            urlconnection = (HttpURLConnection) url.openConnection();
            urlconnection.setDoOutput(true);
            urlconnection.setDoInput(true);
            urlconnection.setRequestMethod(RequestMethod);

            urlconnection.setConnectTimeout(timeout);


            ////////////////////////response code//////////////////////////////
            inputStreamReader = new InputStreamReader(url.openStream());
            buffered = new BufferedReader(inputStreamReader);

        } catch (SocketTimeoutException e) {
            throw new SocketTimeoutException();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStreamReader;
    }

    @Override
    public void run() {
        try {
            doConnectToServerAt(this.url);
            parser = new XmlParser();
            urlconnection.disconnect();

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }


    }

}