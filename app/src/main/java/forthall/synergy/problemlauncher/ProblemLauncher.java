package forthall.synergy.problemlauncher;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.forthall.dansonmbuthia.application.R;


/**
 * Created by MY-HOMEDESKTOP on 2/23/2017.
 */
public class ProblemLauncher extends Activity {
     private TextView user_email;
    private TextView problem;
    Bundle data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.login_problem_launcher);

        data=getIntent().getExtras();
        user_email=(TextView)findViewById(R.id.sender_email);
        problem=(TextView)findViewById(R.id.problem_textView);
        user_email.setText(data.getString("Email"));
       problem.setText(data.getString("Problem"));

        super.onCreate(savedInstanceState);
    }

}
