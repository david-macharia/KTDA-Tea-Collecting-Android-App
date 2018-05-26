package forthall.synergy.dansonmbuthia.application.ktdainterfaces;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.forthall.dansonmbuthia.application.R;

import forthall.synergy.backgroundthreadclass.KtdaLogin;
import forthall.synergy.finalfields.AppPreferences;

import forthall.synergy.finalfields.ServerConnection;
import forthall.synergy.postingdata.PostBagToRemoteOnNetworkAvailability;
import forthall.synergy.problemlauncher.ProblemLauncher;
import forthall.synergy.utility.Data;
import forthall.synergy.utility.LocalDbContent;
import forthall.synergy.utility.LocalDbUtility;


public class LogIn extends Activity implements ServerListener{
  private Button button;
    private ProgressDialog loginprogress;
    private EditText username,password;
  private TextView forgortview;
    String userName;
    private boolean user_existence=false;
    public static Handler handler;
    public Context context=getBaseContext();
    Bundle bundle;
    ImageView viewpassword;
    ImageView view=null,view1,view2,view3=null;
    public  SharedPreferences sharedPreferences;
    RelativeLayout relativeLayout;
    SharedPreferences.Editor editor;
    private EditText iptext1,iptext2,iptext3,iptext4,porttext;
    private int color=Color.WHITE;
    private String port_address;
    private String ipaddress;
private TextView dotView1,dotView2,dotView3,dotView4;
    NetworkConnectivity connectivity;
private ServerConnectionValidater serverConnectionValidater;

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getSharedPreferences(AppPreferences.PREFERENCE_NAME,MODE_PRIVATE);
        editor= sharedPreferences.edit();

        if(sharedPreferences.getString("ip_address","null").equals("null")){

            setContentView(R.layout.ip_port);
            view=(ImageView)findViewById(R.id.imageView1);
            view.setVisibility(View.INVISIBLE);
             view1=(ImageView)findViewById(R.id.imageView2);
            view1.setVisibility(View.INVISIBLE);
             view2=(ImageView)findViewById(R.id.imageView3);
            view2.setVisibility(View.INVISIBLE);
           view3=(ImageView)findViewById(R.id.imageView4);
            view3.setVisibility(View.INVISIBLE);
             button= (Button)findViewById(R.id.connect);
            button.setEnabled(false);
            iptext1=(EditText)findViewById(R.id.iptext1);
            dotView1=(TextView)findViewById(R.id.dot1);
            dotView1.setGravity(Gravity.CENTER_VERTICAL);
            dotView2=(TextView)findViewById(R.id.dot2);
            dotView2.setGravity(Gravity.CENTER_VERTICAL);
            dotView3=(TextView)findViewById(R.id.dot3);
            dotView3.setGravity(Gravity.CENTER_VERTICAL);
            iptext1.setGravity(Gravity.CENTER);
            iptext2=(EditText)findViewById(R.id.iptext2);
            iptext2.setGravity(Gravity.CENTER);

            iptext3=(EditText)findViewById(R.id.iptext3);
            iptext3.setGravity(Gravity.CENTER);
            iptext4=(EditText)findViewById(R.id.iptext4);
            iptext4.setGravity(Gravity.CENTER);
            porttext=(EditText)findViewById(R.id.port_number);
            porttext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validatePortAddress(porttext);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            iptext1.addTextChangedListener( new IPValidater(iptext1));
            iptext2.addTextChangedListener( new IPValidater(iptext2));
            iptext3.addTextChangedListener( new IPValidater(iptext3));
            iptext4.addTextChangedListener( new IPValidater(iptext4));
            relativeLayout=(RelativeLayout)findViewById(R.id.progresslayout);

            relativeLayout.setVisibility(View.INVISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                        connectivity= new NetworkConnectivity(getApplicationContext());
                     if(connectivity.getNetworkState().equals(InternetConnection.CONNECTED)){

                         relativeLayout.setVisibility(View.VISIBLE);
                         ImageView views[] = new ImageView[]{view, view1, view2, view3};
                        serverConnectionValidater= new ServerConnectionValidater(views);
                         serverConnectionValidater.execute(views);
                          connectivity.getConnectivityManager(new  HandleServers(),getApplicationContext(),ipaddress,port_address).start();

                     }else{
                         Toast.makeText(getApplicationContext(),"Not connected",Toast.LENGTH_SHORT).show();
                     }




                }
            });

        }else{
            setContentView(R.layout.login);
            button=(Button)findViewById(R.id.login);
            username=(EditText)findViewById(R.id.usernametext);
            password=(EditText)findViewById(R.id.passwordtext);
            password.addTextChangedListener(new TextFieldManager(this));
            username.addTextChangedListener(new TextFieldManager(this));
            button.setEnabled(false);
            viewpassword=(ImageView)findViewById(R.id.viewpassword);
            viewpassword.setBackgroundColor(Color.TRANSPARENT);
            forgortview=(TextView)findViewById(R.id.forgot);

            viewpassword.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_DOWN){
                        password.setTransformationMethod(null);

                        return true;
                    }else if(event.getAction()==MotionEvent.ACTION_UP){

                        password.setTransformationMethod(new PasswordTransformationMethod());
                        return true;
                    }
                    return false;
                }

            });
            forgortview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(getApplicationContext(), ProblemLauncher.class);
                    Bundle extra= new Bundle();
                    extra.putString("Email",username.getText().toString());
                    extra.putString("Problem","Problem Log in");
                    intent.putExtras(extra);
                    startActivity(intent);
                    finish();
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if ( new NetworkConnectivity(getApplicationContext()).getNetworkState().equals(InternetConnection.CONNECTED)) {

                        onLogin(v);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Connect to network!!", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }




    }
    public void jumpToNextTextField(EditText editText){

        editText.setGravity(Gravity.CENTER);
        editText.requestFocus();
           InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
           inputMethodManager.showSoftInput(editText,InputMethodManager.SHOW_IMPLICIT);
          this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }
    class HandleServers extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle=msg.getData();

            if(bundle.getString("CONNECTION").equals("REACHABLE")){
                Log.i("Connectivity", "Received a bundle from remote server");
                    editor.putString("ip_address",ipaddress);
                    editor.putString("port_number",port_address);
                    editor.apply();
                     recreate();

            }else{
                serverConnectionValidater.stopAsynThread();
                relativeLayout.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"we cannot get the server",Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);
        }
    }
    class IPValidater implements TextWatcher {
        EditText text;

        IPValidater(EditText text) {
            this.text = text;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() != 0) {
                int value = Integer.valueOf(String.valueOf(s));
                if (value > 255) {
                    Toast.makeText(getApplicationContext(), "Invalid ip:Exceded 255", Toast.LENGTH_LONG).show();
                    text.setBackgroundColor(Color.RED);
                    activateConnect();

                } else {
                    text.setBackgroundColor(Color.WHITE);
                    activateConnect();
                    if(!iptext1.getText().toString().equals("")&&!iptext2.getText().toString().equals("")&&
                    !iptext3.getText().toString().equals("")&&!iptext1.getText().toString().equals("")&&
                            !iptext4.getText().toString().equals("")&&(!porttext.getText().equals(""))){

                        ipaddress=iptext1.getText().toString()+"."+iptext2.getText().toString()+"."+
                                iptext3.getText().toString()+"."+iptext4.getText().toString();
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        void activateConnect() {

            if ((!iptext1.getText().toString().equals("")) && ((Integer.valueOf(iptext1.getText().toString())) <= 255)&&iptext1.isFocused()) {
                if((iptext1.getText().toString().length())==3){
                    Log.e("Textbox","jumped to 2");
                    jumpToNextTextField(iptext2);
                }

            }else if((!iptext2.getText().toString().equals(""))&&((Integer.valueOf(iptext2.getText().toString()))<=255)&&iptext2.isFocused()){
                if((iptext2.getText().toString().length())==3){
                    Log.e("Textbox","jumped to 3");
                    jumpToNextTextField(iptext3);
                }
            }else if((!iptext3.getText().toString().equals(""))&&((Integer.valueOf(iptext3.getText().toString()))<=255)&&iptext3.isFocused()){
                if((iptext3.getText().toString().length())==3){
                    Log.e("Textbox","jumped to 4");
                    jumpToNextTextField(iptext4);
                }
            }

        }
    }
   public void validatePortAddress(EditText editText){

        port_address=editText.getText().toString();
        if(!port_address.equals("")){
            button.setEnabled(true);
        }else{
            button.setEnabled(false);
        }
    }
public void onLogin(View view){
 userName=username.getText().toString();


String pass_word=password.getText().toString();
    loginprogress=ProgressDialog.show(this,"Connecting...","Please wait...",true,true);
    handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
           Bundle bundle = msg.getData();

            if(bundle.getString("ServerConnectionTimeOut")!=null&&bundle.getString("ServerConnectionTimeOut").equals(ServerConnection.TIME_OUT)){
                loginprogress.dismiss();
                Toast.makeText(getApplicationContext(),"connection time out",Toast.LENGTH_LONG).show();

            }
            else if(bundle.getString("Message")!=null&&bundle.getString("Message").equals("USER_EXIST")){
             LocalDbUtility   localDbUtility= new LocalDbUtility( new LocalDbContent(getApplicationContext()));
                localDbUtility.cleanLocalBuffer();
                continueToCollecting();
              //  Data data= new Data(getApplicationContext(),)
                getApplicationContext().registerReceiver(new PostBagToRemoteOnNetworkAvailability(),
                        new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                loginprogress.dismiss();
            }else if(bundle.getString("Message")!=null&&bundle.getString("Message").equals("USER_DOES_NOT_EXIST")){
                loginprogress.dismiss();
                Toast.makeText(getApplicationContext(),"User Name or password \n incorrect!!",Toast.LENGTH_LONG).show();
            }else if(bundle.getString("LinkBroken")!=null&&bundle.getString("LinkBroken").equals(ServerConnection.LINK_BROKEN)) {
                loginprogress.dismiss();
                Toast.makeText(getApplicationContext(),"Problem encountered",Toast.LENGTH_LONG).show();

                user_existence=false;
            }else{
                loginprogress.dismiss();
                Toast.makeText(getApplicationContext(),"Problem encountered",Toast.LENGTH_LONG).show();

                user_existence=false;
            }

            super.handleMessage(msg);
        }
    };
    Thread thread= null;

     String url="http://"+sharedPreferences.getString("ip_address","0.0.0.0")+":"+sharedPreferences.getString("port_number","8080")+"/KTDARestservice/ktda_api/login/";
    thread = new Thread(new KtdaLogin( url,userName, pass_word));
    thread.start();

}
    public void continueToCollecting(){
       // MediaPlayer meadia=MediaPlayer.create(this,R.raw.logged_in);

      //  meadia.start();
            this.provideNotifacation("Welcome " + userName, 0, null);

            Intent intent= new Intent(this,ChooseBuyer.class);
        intent.putExtra("Tag","INTENT_LOG_IN");
        editor.putString("driver_name",userName);
        editor.apply();
            startActivity(intent);
    //    meadia.stop();
        finish();

    }

public void provideNotifacation(String messages,int messageid,String subtitle){
    Notifacation notify= new Notifacation(getApplicationContext());
    if(subtitle==null){
        notify.setTittle(" KTDA  ");
    }else {
        notify.setTittle(" KTDA  " + subtitle);
    }
    notify.setMessage(messages);

   notify.setNotiIcon(R.drawable.noticon);
    notify.notifyTheManager();
}

    @Override
    public boolean hasTimedOut(TimeOut timeOut) {

        Toast.makeText(getApplicationContext(),"Failed To reach Servers",Toast.LENGTH_LONG).show();
        return false;
    }


    class TextFieldManager implements TextWatcher{
        private Context context;
        TextFieldManager(Context context){
            this.context=context;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(username.getText().length()==0||password.getText().length()==0){
                button.setEnabled(false);
            }else{
                button.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
