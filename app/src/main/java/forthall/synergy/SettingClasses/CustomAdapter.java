package forthall.synergy.SettingClasses;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.forthall.dansonmbuthia.application.R;


/**
 * Created by MY-HOMEDESKTOP on 2/13/2017.
 */
public class CustomAdapter extends ArrayAdapter<Setting> {

    Context context;
    int id;
    Setting[] settings;
   public CustomAdapter(Context context,int id,Setting []resources){
        super(context,id,resources);
        this.context=context;
        this.id=id;
        this.settings=resources;
    }
    @Override
    public View getView(int posation,View convertView,ViewGroup viewGroup){
        View view=convertView;
        LayoutInflater layoutInflater;
        Holder holder = new Holder();
        if(view==null){
           layoutInflater=((Activity)context).getLayoutInflater();
            view=layoutInflater.inflate(id, viewGroup, false);
           holder.settingview=(TextView)view.findViewById(R.id.setting_text);

            holder.setting_state=(CheckBox)view.findViewById(R.id.setting_state);
            if(holder.setting_state==null){

            }

        }

       Setting setting=settings[posation];
        holder.settingview.setText(setting.getSettingString());
        holder.setting_state.setChecked(((CheckBox)setting.getSettingView()).isChecked());


       return view;

    }
    static class Holder{
        TextView settingview;
        CheckBox setting_state;
    }
}
