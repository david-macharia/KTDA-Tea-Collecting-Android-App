package forthall.synergy.dansonmbuthia.application.ktdainterfaces;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.forthall.dansonmbuthia.application.R;
import forthall.synergy.finalfields.AppPreferences;
import forthall.synergy.postingdata.VolleySingletone;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A login screen that offers login via email/password.
 */
public class Register extends AppCompatActivity  {

Bundle receiveBundle;
TextView userName,growerNumber,clerkName,contryCode;
EditText  email;
    ProgressDialog progressDialog;
    EditText phoneNumber;
    Button button;
    static SharedPreferences sharedPreferences;
    //code to validate email
   private boolean isEmailValid(String email){
        if(TextUtils.isEmpty(email))  {
            Toast.makeText(getApplicationContext(), "Email Empty", Toast.LENGTH_SHORT).show();
            return  false;
        }else if(!email.contains("@")){
            Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!email.contains(".")){
            Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
            return false;
        }else if((isADigit(String.valueOf(email.indexOf("@")+1)))){
            Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();

        }
        return  true;
    }
    //check if is a digit
    private boolean isADigit(String digit){
        try {
            Integer.parseInt(digit);
        }catch (NumberFormatException e){
            return  false;
        }
        return  true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        receiveBundle=getIntent().getBundleExtra("registerInfo");
        userName=(TextView)findViewById(R.id.growerName);
        userName.setText(receiveBundle.getString("buyer").toUpperCase());
        growerNumber=(TextView)findViewById(R.id.growerNumber);
                growerNumber.setText(receiveBundle.getString("growerId").toUpperCase());
        clerkName=(TextView)findViewById(R.id.clerkName);
        clerkName.setText(receiveBundle.getString("driver_name").split("@")[0].toUpperCase());
        email=(EditText)findViewById(R.id.growerEmail);

        phoneNumber=(EditText)findViewById(R.id.phoneNumber);

        phoneNumber.setText(processPhone(receiveBundle.getString("Phone")));
        button=(Button)findViewById(R.id.Update_Farmer_Records);
        contryCode=(TextView)findViewById(R.id.countryCodeTextView);
        sharedPreferences = getSharedPreferences(AppPreferences.PREFERENCE_NAME, MODE_PRIVATE);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isEmailValid(email.getText().toString())) {

                } else {
                    if ((TextUtils.isEmpty(email.getText().toString())) && TextUtils.isEmpty(phoneNumber.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Nothing to update", Toast.LENGTH_SHORT).show();
                    } else if (!TextUtils.isEmpty(email.getText().toString()) && (TextUtils.isEmpty(phoneNumber.getText().toString()))) {
                        Toast.makeText(getApplicationContext(), "updating Phone contact..", Toast.LENGTH_SHORT).show();
                        progressDialog = ProgressDialog.show(Register.this, "Updating...", "Please wait..", false);
                        progressDialog.show();
                        updateUserAccount(true, false);
                    } else if (TextUtils.isEmpty(email.getText().toString()) && (!TextUtils.isEmpty(phoneNumber.getText().toString()))) {
                        Toast.makeText(getApplicationContext(), "updating email ", Toast.LENGTH_SHORT).show();
                        progressDialog = ProgressDialog.show(Register.this, "Updating...", "Please wait..", false);
                        progressDialog.show();
                        updateUserAccount(false, true);
                    } else {
                        Toast.makeText(getApplicationContext(), "updating contact ", Toast.LENGTH_SHORT).show();
                        progressDialog = ProgressDialog.show(Register.this, "Updating...", "Please wait..", false);
                        progressDialog.show();
                        updateUserAccount(true, true);
                    }

                }
            }
        });

        ActionBar actionBar = getSupportActionBar();

        actionBar.setLogo(R.drawable.settings);
     actionBar.show();


    }
    private String processPhone(String phone){
        if(phone.startsWith("+")){
            return phone.substring(4);
        }else{
            return phone;
        }
    }
public  void updateUserAccount(boolean emailState,boolean phoneState){
    VolleySingletone volleySingletone= new VolleySingletone(getApplicationContext());
    try {

        volleySingletone.setUrl(new URL("http://"+sharedPreferences.getString("ip_address","0.0.0.0")+":"+sharedPreferences.getString("port_number","8080")+"/KTDARestservice/ktda_api/growers/update"));
        JSONObject data = new JSONObject();
        data.put("growerId", receiveBundle.getString("growerId"));
        if (emailState){
            Log.i("email","loading email");

            data.put("email", email.getText().toString());

        }
        if(phoneState){
            Log.i("contact","loading contact");
            data.put("phoneNumber", phoneNumber.getText().toString());
        }

        final String stringData=data.toString();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, volleySingletone.getUrl().toString(),
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"could not Update try again",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json;charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                if(stringData!=null){
                    try {
                        return stringData.getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
        Log.e("Net Request", "Adding reguest to network");
        VolleySingletone.getVolleyInstance(getApplicationContext()).add(stringRequest);
    } catch (MalformedURLException | JSONException e) {
        e.printStackTrace();
    }
}




}

