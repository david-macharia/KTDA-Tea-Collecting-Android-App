package forthall.synergy.SettingClasses;

import android.view.View;

/**
 * Created by MY-HOMEDESKTOP on 2/13/2017.
 */
public class Setting {
    private String setting_string;
    private View view;
    public Setting(String setting ,View view){
        this.view=view;
        this.setting_string=setting;
    }
    public void setSettingString(String setting_message){
        this.setting_string=setting_message;
    }
    public void setView(View view){
        this.view=view;
    }
    public View getSettingView(){
        return view;
    }
    public String getSettingString(){
        return setting_string;
    }
}
