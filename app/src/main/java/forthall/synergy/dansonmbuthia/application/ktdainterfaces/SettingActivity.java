package forthall.synergy.dansonmbuthia.application.ktdainterfaces;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import com.forthall.dansonmbuthia.application.R;

import forthall.synergy.SettingClasses.CustomAdapter;
import forthall.synergy.SettingClasses.Setting;


/**
 * Created by MY-HOMEDESKTOP on 2/13/2017.
 */
public class SettingActivity extends AppCompatActivity {
private CustomAdapter adapter;
    private RecyclerView recycler;
    Setting settings;
    @Override
    public void onCreate(Bundle savedinstancebundles){
        super.onCreate(savedinstancebundles);
        setContentView(R.layout.settings);
        Setting [] settings={
                new Setting("Always use current date",new CheckBox(this)),
                new Setting("Log off  after exit",new CheckBox(this)),
                new Setting("Connection setting",new CheckBox(this)),
                new Setting("Always use current date",new CheckBox(this)),
                new Setting("Always use current date",new CheckBox(this)),
                new Setting("Always use current date",new CheckBox(this))};
        adapter= new CustomAdapter(this, R.layout.setting_row,settings);
        recycler=(RecyclerView) findViewById(R.id.setting_listview);
        recycler.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);


    }
}
