package forthall.synergy.dansonmbuthia.application.ktdainterfaces;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Created by MY-HOMEDESKTOP on 2/13/2017.
 */
public class Notifacation {
    NotificationManager notifacationManager;
    NotificationCompat.Builder biulder;
    int notid=100;
    Notifacation(Context context) {
         biulder =  new NotificationCompat.Builder(context);
        notifacationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    public void setNotiIcon(int  iconid){
        biulder.setSmallIcon(iconid);
    }
    public void setMessage(String message){
        biulder.setContentText(message);
    }
    public void setTittle(String tittle){
        biulder.setContentTitle(tittle);
    }
    public void notifyTheManager(){
        notifacationManager.notify(100,biulder.build());
    }
    public int getNoId(){return notid;}
    public void setNotId(int notid){this.notid=notid;}
}
