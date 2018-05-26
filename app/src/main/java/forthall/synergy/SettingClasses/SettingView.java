package forthall.synergy.SettingClasses;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class SettingView  extends AppCompatActivity {


    class SettingAdapter extends RecyclerView.Adapter<CustomSettingViewHolder>

    {
       String [] data={"change day","keep Logged in"};
        @Override
        public CustomSettingViewHolder onCreateViewHolder(ViewGroup viewGroup,int i) {

            return null;
        }

        @Override
        public void onBindViewHolder(CustomSettingViewHolder customSettingViewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return data.length;
        }
    }
    class CustomSettingViewHolder extends RecyclerView.ViewHolder{

        public CustomSettingViewHolder(View itemView) {
            super(itemView);
        }
    }

}
