package forthall.synergy.dansonmbuthia.application.ktdainterfaces;

import forthall.synergy.FactoryErrorHandler.IPError;
import forthall.synergy.FactoryErrorHandler.IPSequenceException;

import java.util.regex.Pattern;

/**
 * Created by MY-HOMEDESKTOP on 2/21/2017.
 */
public class InternetConnection {

        public  static   final String CONNECTED = "CONNECTED";
          public  static   final String NOT_CONNECTED = "NOT_CONNECTED";
    private static String serverIpAddress="192.168.0.100";
    public static String getServeripAddress(){
        return serverIpAddress;
    }
    public static void   setServerIpAdress(String serverIpAddress) throws IPSequenceException{
        IPError error = null;
        String dotNotation[]=serverIpAddress.split(Pattern.quote("."));
        if(dotNotation.length<4||dotNotation.length>4){
            error= new IPError();
            error.setIpAdress(serverIpAddress);
            error.setErrorMessage("IP address is a four dotted decimal notion");
            throw new IPSequenceException(error);
        }else{
            error= new IPError();
            String value ="";

            for(int a=0;a<dotNotation.length;a++){

                try{
                    if(Integer.parseInt(dotNotation[a])<0|Integer.parseInt(dotNotation[a])>255){
                        error.setErrorMessage("ip not in the range of 0 to 255");
                        error.setErroFound("Out of range");
                        error.setIpAdress(serverIpAddress);
                        throw new IPSequenceException(error);
                    }

                }catch(NumberFormatException e){
                    error.setErroFound(dotNotation[a]);
                    error.setErrorMessage("only numbers are allowed");
                    error.setIpAdress(serverIpAddress);
                    throw new IPSequenceException(error);
                }



                value=value.concat(dotNotation[a]);
            }

            for(int i=0;i<value.length();i++){
                try{
                    Integer.parseInt(String.valueOf(value.charAt(i)));
                }catch(NumberFormatException e){
                    error.setErrorMessage("Only Numbers are allowed");
                    error.setIpAdress(serverIpAddress);
                    error.setErroFound(String.valueOf(value.charAt(i)));
                    throw new IPSequenceException(error);
                }
            }
        }





    }

}
