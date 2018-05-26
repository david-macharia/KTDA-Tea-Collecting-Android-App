package forthall.synergy.dansonmbuthia.application.ktdainterfaces;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.forthall.dansonmbuthia.application.R;

import forthall.synergy.DatabaseObjects.Bag;
import forthall.synergy.DatabaseObjects.BuyingCenter;
import forthall.synergy.DatabaseObjects.Clerk;
import forthall.synergy.DatabaseObjects.Factory;
import forthall.synergy.DatabaseObjects.Grower;
import forthall.synergy.JSONParsingModule.JSONTransformerUtil;
import forthall.synergy.finalfields.AppPreferences;

import forthall.synergy.postingdata.VolleySingletone;
import forthall.synergy.recept.Receipt;
import forthall.synergy.utility.ColumnCostants;
import forthall.synergy.utility.LocalDbContent;
import forthall.synergy.utility.LocalDbUtility;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class Collecting extends AppCompatActivity {
    static int bufferCounter = 0;
    private TextView factoryView, centerView;
    private static TextView growerView;
    private TableLayout tableLayout;
    private Button addBug, buytea;
    TextView rowtext;
    private ImageButton deleteimage;
    TableRow tableRow;
    private CheckBox setStardardWeight;
    static ArrayList<Bag> bags = new ArrayList<>();
    private static Bundle bundle;
    private static Context context;
    private Button printbackup;
    private ScrollView scrollingView;
    static HashMap<Integer, ArrayList<Bag>> quequedbags = new HashMap<>();
    ActionBar actionBar;
    static SharedPreferences sharedPreferences;
    private Button cancelbutton;
    //  public static NetWorkClient.RemoterposterHandle  remoterposterHandle;
    CustomerPdfViewerDialog customerPdfViewerDialog;
    NetWorkClient netWorkClient;
    BuyTeaObserver buyTeaObserver;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collecting);

        factoryView = (TextView) findViewById(R.id.factory_selected_text);
        centerView = (TextView) findViewById(R.id.center_selected_text);
        growerView = (TextView) findViewById(R.id.buyer_selected_text);
        scrollingView=(ScrollView)findViewById(R.id.scroller);
        bundle = getIntent().getExtras();
        factoryView.setText(bundle.getString("factory"));
        centerView.setText(bundle.getString("center"));
        growerView.setText(bundle.getString("buyer"));
        Set<String> set = new ArraySet<String>();
        set.add("null");
        context = getApplicationContext();
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);

        tableLayout = (TableLayout) findViewById(R.id.table_layout_to_add_row);
        buytea = (Button) findViewById(R.id.buytea);
        addBug = (Button) findViewById(R.id.add_bag_to_the_row_button);
        addBug.setOnClickListener(new AddBag());
        buytea.setOnClickListener(buyTeaObserver = new BuyTeaObserver(tableLayout, this));
        buytea.setEnabled(false);
        Bundle bundle= new Bundle();
        Intent intent= new Intent();
        bundle.putBoolean("returned?", true);
        intent.putExtras(bundle);
        setIntent(intent);
        sharedPreferences = getSharedPreferences(AppPreferences.PREFERENCE_NAME, MODE_PRIVATE);


    }

    @Override
    protected void onDestroy() {
        Log.e("destroyed","activity destroyed");


        super.onDestroy();
    }

    //create options fot this activity from the menulayout file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    public String getPhoneFromLocal(String growerid){
        String sqlStatement="SELECT "+ ColumnCostants.PHONE_NUMBER+" FROM "+ColumnCostants.GROWER_TABLE_NAME+" WHERE "+ColumnCostants.GROWEER_ID+" ='"+growerid+"'";
        Log.e("phone statement",sqlStatement);

        LocalDbUtility localDbUtility= new LocalDbUtility(new LocalDbContent(getApplicationContext()));
        Cursor cursor= localDbUtility.getGenericLocalData(sqlStatement);
       if(cursor.moveToFirst()){
           do{
               Log.e("Phone",cursor.getString(0));
            return cursor.getString(0);
           }while (cursor.moveToNext());
       }
        return " ";
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      if(item.getItemId()==R.id.Update_Farmer){
          Bundle registerbundle= new Bundle();
          bundle.putString("Phone",getPhoneFromLocal(bundle.getString("growerId")));
          registerbundle.putAll(bundle);
        Intent intent= new Intent(getApplicationContext(),Register.class) ;
          intent.putExtra("registerInfo",registerbundle);
          startActivity(intent);
      }else if(android.R.id.home==item.getItemId()){
          ChooseBuyer.iscollection=true;
      }
        return false;
    }

    // on click add a bag to the row of the table
    class AddBag implements View.OnClickListener {

        int counter = 1;
        BagCache bagCache;

        public BagCache getBagCache() {
            return bagCache;
        }

        @Override
        public void onClick(View v) {
            buytea.setEnabled(true);
            LayoutInflater inflater = getLayoutInflater();
            tableRow = (TableRow) inflater.inflate(R.layout.bag, null);
            deleteimage = (ImageButton) tableRow.findViewById(R.id.delete_row_imageButton);
            deleteimage.setOnClickListener(new deleteRow());
            deleteimage.setTag(tableRow);
            rowtext = (TextView) tableRow.findViewById(R.id.bag_textview);
            setStardardWeight = (CheckBox) tableRow.getChildAt(3);
            setStardardWeight.setOnCheckedChangeListener(new StandardWeight());
            bagCache = new BagCache(tableRow, rowtext.getText().toString());
            setStardardWeight.setTag(bagCache);
            tableLayout.addView(tableRow);
            if (tableLayout.getChildCount() == 0) {
                rowtext.setText(String.valueOf(counter));

                counter++;
            } else {

                counter = tableLayout.getChildCount();
                rowtext.setText(String.valueOf(counter));

            }
            if (tableLayout.getChildCount() == 0) {

                buytea.setEnabled(false);
            } else {
                buytea.setEnabled(true);
            }


        }

        //delete a certain row when clicked
//always arrage the row in continous number formart
        class deleteRow implements View.OnClickListener {

            @Override
            public void onClick(View v) {


                tableLayout.removeView((TableRow) v.getTag());
                for (int i = 0; i < tableLayout.getChildCount(); i++) {
                    TableRow row = (TableRow) tableLayout.getChildAt(i);
                    RelativeLayout relativeLayout = ((RelativeLayout) row.getChildAt(1));
                    EditText editText = (EditText) row.getChildAt(2);

                    TextView textView = (TextView) relativeLayout.getChildAt(0);
                    textView.setText(String.valueOf(i + 1));
                }
                if (tableLayout.getChildCount() == 0) {

                    buytea.setEnabled(false);
                } else {
                    buytea.setEnabled(true);
                }
            }
        }

        //cache the table row with the weight if  to  replace the standard weight on uncheck
        class BagCache {
            public TableRow tableRow;
            public String Weight;

            BagCache(TableRow tableRow, String Weight) {
                this.tableRow = tableRow;
                this.Weight = Weight;
            }

        }

        //set the standard weight to the fild
        //the difult is twelve
        class StandardWeight implements CompoundButton.OnCheckedChangeListener {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CheckBox checkBox = (CheckBox) buttonView;
                if (checkBox.isChecked()) {
                    BagCache bagCache = (BagCache) checkBox.getTag();

                    TableRow row = (TableRow) bagCache.tableRow;

                    EditText editText = (EditText) row.getChildAt(2);
                    bagCache.Weight = editText.getText().toString();
                    editText.setText(new String("12"));
                    editText.setEnabled(false);

                } else {
                    BagCache bagCache = (BagCache) checkBox.getTag();
                    ;
                    TableRow row = (TableRow) bagCache.tableRow;

                    EditText editText = (EditText) row.getChildAt(2);

                    editText.setText(bagCache.Weight);
                    editText.setEnabled(true);
                }
            }
        }
    }

    // loop throught the table layout and create bag for each row with a value
    //discard empty rows
    //put the rows in an arraylist of bags
    class BuyTeaObserver implements View.OnClickListener {
        TableLayout tableLayout;
        private boolean aresubmit;
        private ArrayList<Bag> bagcache = new ArrayList<Bag>();
        Context context;

        public ArrayList<Bag> getBagcache() {
            return bagcache;
        }

        BuyTeaObserver(TableLayout layout, Context context) {
            this.context = context;
            this.tableLayout = layout;
        }

        @Override
        public void onClick(View v) {
            Log.i("KTDA_LOG", "Buy tea....");
            int counter = 0;

            TableRow tableRow = null;
            bags = new ArrayList<Bag>();
            Bag bag;
            Log.i("KTDA_LOG", "Loop through bag rows");
            for (int i = 0; i < tableLayout.getChildCount(); i++) {
                bag = new Bag();
                bag.setCenterId(bundle.getString("centerId"));
                bag.setGrowerId(bundle.getString("growerId"));

                bag.setDriverId(bundle.getString("driver_name"));

                tableRow = (TableRow) tableLayout.getChildAt(i);
                EditText editText = (EditText) tableRow.getChildAt(2);
                if (editText.getText().toString().equals("")) {
                    counter++;
                    bag = null;

                } else {
                    bag.setWeightInKg(Double.valueOf(editText.getText().toString()));
                    bags.add(bag);
                }


            }
            Log.i("KTDA_LOG", "Finished looping the bags rows...");
            Log.i("KTDA_LOG", "Testing if the bag cached are zero...");

            Log.i("KTDA_LOG", "Comfirmed the bag cached are zero...");
            Log.i("KTDA_LOG", "Test if the current bag in the list if are greater than zero");
            if (bags.size() > 0) {
                Log.i("KTDA_LOG", "Comfirmed the current bag are greater  zero...");
                bagcache.addAll(bags);
                Log.i("KTDA_LOG", "Added bags the bag cache...");
                Log.i("KTDA_LOG", "Preparing a PDFViewer dialog for display");
                if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
                    Log.i("KTDA_LOG", "Starting writting json to sd card");
                    VolleyIntergrationPoster volleyIntergrationPoster= new VolleyIntergrationPoster(context);
                    try {
                        volleyIntergrationPoster.setUrl(new URL("http://"+sharedPreferences.getString("ip_address","0.0.0.0")+":"+sharedPreferences.getString("port_number","8080")+"/KTDARestservice/ktda_api/grower/bags"));
                        String jsonData= JSONTransformerUtil.getJSONDdata(bags);
                        String filename=NetWorkClient.writeJsonBackUpToSd(jsonData);
                        volleyIntergrationPoster.setFileName(filename);
                        volleyIntergrationPoster.setContent(jsonData);
                        volleyIntergrationPoster.send();
                        Log.i("KTDA_LOG", "Successifully written file to sd card");
                        Toast.makeText(getApplicationContext(),"Transaction Completed",Toast.LENGTH_LONG).show();
                        tableLayout.removeAllViewsInLayout();
                        bags.clear();
                        buytea.setEnabled(false);


                        Toast.makeText(getApplicationContext(), "Transaction for: " + growerView.getText().toString() + "Successful", Toast.LENGTH_SHORT).show();

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else {
                    customerPdfViewerDialog = new CustomerPdfViewerDialog(context);
                    Log.i("KTDA_LOG", "Preparing receipt for writing....");
                    Receipt receipt = new Receipt(getApplicationContext(), customerPdfViewerDialog.getImageView());
                    Log.i("KTDA_LOG", "Passing dispatch data to receipt writer");
                    receipt.dispatchReceipt(bags, new Clerk(bundle.getString("driver_name").split("@")[0], bundle.getString("driver_name")),
                            new Grower(bundle.getString("growerId"), bundle.getString("buyer")),
                            new BuyingCenter(bundle.getString("centerId"), bundle.getString("center")),
                            new Factory(bundle.getString("factory"), bundle.getString("factoryId")));
                    Log.i("KTDA_LOG", "Success! passed the data to receipt writer");
                    customerPdfViewerDialog.setTitle(bundle.getString("buyer"));
                    customerPdfViewerDialog.show();
                    Log.i("KTDA_LOG", "Published the pdfview to the user");
                }
            } else if (bagcache.isEmpty() && bags.isEmpty() && tableLayout.getChildCount() == 0) {
                Log.i("KTDA_LOG", "Cache is empty,bags is empty,no rows");
                Toast.makeText(getApplicationContext(), "Please press add bag", Toast.LENGTH_SHORT).show();
                Log.i("KTDA_LOG", "propted user user to add bags ");
            } else {
                Log.i("KTDA_LOG", " Prompted user to fill the weight");
                Toast.makeText(getApplicationContext(), "Please fill in the weight(s)", Toast.LENGTH_LONG).show();
            }
        }

    }


       



    static String filename;
 static class NetWorkClient extends ServerManager {

private int  bagKey;
     NetWorkClient() {
     }

public  void setBagKey(int key){
    this.bagKey=key;
}

     @Override
     public void handleMessage(Message msg) {
         filename=msg.getData().getString("");
         if (msg.getData().getString("CONNECTION").equals("REACHABLE")) {
                Log.i("Collecting","The server is reachable");
             //remoterposterHandle = new RemoterposterHandle(context);
         } else if (msg.getData().getString("CONNECTION").equals("REQUEST_TIMED_OUT")) {
             Log.i("Collecting","The request time out");
             Toast.makeText(context,"No Connection:Saved Local",Toast.LENGTH_LONG).show();

             Log.i("Backup", " Saved to sd card");
         } else if (msg.getData().getString("CONNECTION").equals(InternetConnection.NOT_CONNECTED)) {
             Log.i("Collecting","seam like link is broken");
             try {
                 if(!quequedbags.isEmpty()) {
                     Toast.makeText(context, "No Connection:Saved Local", Toast.LENGTH_LONG).show();
                     writeJsonBackUpToSd(JSONTransformerUtil.getJSONDdata(quequedbags.get(bagKey)));
                     Log.i("Backup", "Problem Saving to sd card");
                 }
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
         super.handleMessage(msg);
     }



     //write json Backup to sdcard
     static synchronized public String writeJsonBackUpToSd(String JSONFile) {

         FileOutputStream outputStream;
         try {
             File file = createAniqueFile(bundle.getString("growerId"));
             outputStream= new FileOutputStream(file) ;
             outputStream.write(JSONFile.getBytes());
             outputStream.close();

             return file.getName();
         } catch (FileNotFoundException e) {
             e.printStackTrace();
             return null;
         } catch (IOException e) {
             e.printStackTrace();
             return null;
         }

        
     }

     //test if external storage is mounted or exists
     static private boolean isExternalStorageMounted() {
         String state = Environment.getExternalStorageState();
         if ((state.equals(Environment.MEDIA_MOUNTED)) || (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))) {
             return true;
         }
         return false;
     }


     static public File checkAvailability() {
         if (isExternalStorageMounted()) {
             File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
             if (file.exists()) {

                 File file_PendingGrowersData = new File(Environment.getExternalStorageDirectory() + "/" + file.getName() + "/.PendingGrowersData");

                 if (file_PendingGrowersData.exists()) {
                     //do something on that directory is it exist
                            return  file_PendingGrowersData;
                 } else {
                     // make a pending factory data if it does not exist
                     if (file_PendingGrowersData.mkdir()) {
                         return  file_PendingGrowersData;
                     } else {
                         Log.e("Directory", "Could not make file_pendingdirectory");
                         return null;
                     }
                 }
             } else {
                 if (file.mkdir()) {
                     File file_PendingGrowersData = new File(Environment.getExternalStorageDirectory() + "/" + file.getName() + "/.PendingGrowersData");

                     if (file_PendingGrowersData.exists()) {
                         //do something on that directory is it exist
                         return  file_PendingGrowersData;
                     } else {
                         // make a pending factory data if it does not exist
                         if (file_PendingGrowersData.mkdir()) {
                             Log.e("file_PendingFactoryData", "directory made");
                             return  file_PendingGrowersData;

                         } else {
                             Log.e("Directory", "Could not make file_pendingdirectory");
                             return  null;

                         }
                     }
                 } else {
                     Log.e("Directory", "Could not make Doc directory");
                     return  null;
                 }
             }
         } else {
             Log.e("Error", "Could not write!! strorage problem");
             return  null;

         }

     }
     static private File createAniqueFile(String growerNumber) {
         new Date().getTime();
         File filetocreate = new File(checkAvailability(), growerNumber + new Date().getTime()+".txt");
         try {
             if (filetocreate.createNewFile()) {
                 return filetocreate.getAbsoluteFile();
             } else {
                 return null;
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
         return null;
     }

 }
class CustomerPdfViewerDialog extends Dialog{
 private CheckBox checkBox;
    private Context context;
private ImageView imageView;
    View views;
    public CustomerPdfViewerDialog(final Context context) {
        super(context);
        LayoutInflater layoutInflater=getLayoutInflater();
      views=layoutInflater.inflate(R.layout.growerpdfviewer,null);
        checkBox=(CheckBox)views.findViewById(R.id.showPdf);
        imageView=(ImageView)views.findViewById(R.id.pdfImageView);
        printbackup=(Button)views.findViewById(R.id.accept_pdf_dialog);
        printbackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i("KTDA_LOG", "user accepted replication");
                    Log.i("KTDA_LOG", "Starting writting json to sd card");
                    VolleyIntergrationPoster volleyIntergrationPoster= new VolleyIntergrationPoster(context);
                    volleyIntergrationPoster.setUrl(new URL("http://"+sharedPreferences.getString("ip_address","0.0.0.0")+":"+sharedPreferences.getString("port_number","8080")+"/KTDARestservice/ktda_api/grower/bags"));
                    String jsonData=JSONTransformerUtil.getJSONDdata(bags);
                    String filename=NetWorkClient.writeJsonBackUpToSd(jsonData);
                    volleyIntergrationPoster.setFileName(filename);
                    volleyIntergrationPoster.setContent(jsonData);
                    volleyIntergrationPoster.send();
                             Log.i("KTDA_LOG", "Successifully written file to sd card");
                      Toast.makeText(getApplicationContext(),"Transaction Completed",Toast.LENGTH_LONG).show();
                    tableLayout.removeAllViewsInLayout();
                    tableLayout.computeScroll();
                    scrollingView.computeScroll();

                    bags.clear();
                    buytea.setEnabled(false);
                    customerPdfViewerDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Transaction for: " + growerView.getText().toString() + "Successful", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        checkBox.setOnCheckedChangeListener(new SeePdf());
        setContentView(views);
        cancelbutton=(Button)views.findViewById(R.id.cancel_pdf_dialog);
        this.setCancelable(false);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("KTDA_LOG", "Then user declined the pdfpreview");
                buyTeaObserver.getBagcache().clear();
                customerPdfViewerDialog.dismiss();
            }
        });





    }


    public ImageView getImageView(){
        return imageView;
    }
}
    static class FileDeleter{
        public static boolean deleteAFile(String fileName){
            File file= checkAvailability();
            String stringfilepath= file.getAbsoluteFile()+"//"+fileName;
            File filepath= new File(stringfilepath);
            if(filepath.delete()){
                Log.i("BroadCaster","Deleted the file"+fileName) ;
                return true;
            }else{
                Log.i("BroadCaster","Could not delete the file"+fileName) ;
                Log.i("BroadCaster","Filepath "+filepath.getAbsolutePath());
                return  false;
            }
        }
        //test if external storage is mounted or exists
        private static boolean isExternalStorageMounted() {
            String state = Environment.getExternalStorageState();
            if ((state.equals(Environment.MEDIA_MOUNTED)) || (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))) {
                return true;
            }
            return false;
        }
        private static File checkAvailability() {
            if (isExternalStorageMounted()) {
                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                if (file.exists()) {

                    File file_PendingGrowersData = new File(Environment.getExternalStorageDirectory() + "/" + file.getName() + "/.PendingGrowersData");

                    if (file_PendingGrowersData.exists()) {
                        //do something on that directory is it exist
                        return  file_PendingGrowersData;
                    } else {
                        // make a pending factory data if it does not exist
                        if (file_PendingGrowersData.mkdir()) {
                            return  file_PendingGrowersData;
                        } else {
                            Log.e("Directory", "Could not make file_pendingdirectory");
                            return null;
                        }
                    }
                } else {
                    if (file.mkdir()) {
                        File file_PendingGrowersData = new File(Environment.getExternalStorageDirectory() + "/" + file.getName() + "/.PendingGrowersData");

                        if (file_PendingGrowersData.exists()) {
                            //do something on that directory is it exist
                            return  file_PendingGrowersData;
                        } else {
                            // make a pending factory data if it does not exist
                            if (file_PendingGrowersData.mkdir()) {
                                Log.e("file_PendingFactoryData", "directory made");
                                return  file_PendingGrowersData;

                            } else {
                                Log.e("Directory", "Could not make file_pendingdirectory");
                                return  null;

                            }
                        }
                    } else {
                        Log.e("Directory", "Could not make Doc directory");
                        return  null;
                    }
                }
            } else {
                Log.e("Error", "Could not write!! strorage problem");
                return  null;

            }

        }
    }
    class SeePdf implements CompoundButton.OnCheckedChangeListener{
   SharedPreferences sharedPreferences;
        SeePdf(){
            sharedPreferences= getSharedPreferences(AppPreferences.PREFERENCE_NAME,MODE_PRIVATE);
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferences.Editor editer=sharedPreferences.edit();
            editer.putBoolean("VIEW_PDF",isChecked);
            editer.apply();
        }
    }
    //send bags to remote using volley lib
    class VolleyIntergrationPoster{
        Context context;
        VolleySingletone volleySingletone;
        StringRequest stringRequest;
        private String content;
        private String fileName;
        public void setFileName(String fileName){
            this.fileName=fileName;
        }
        public String getFileName(){
            return fileName;
        }
        VolleyIntergrationPoster(Context context){
            this.context=context;
            volleySingletone= new VolleySingletone(context);
        }
        public void setContent(String content){
            this.content=content;
        }
        public String getContent(){
            return content;
        }
        public void setUrl(URL url){
            volleySingletone.setUrl(url);

        }
        public void send(){
            stringRequest= new StringRequest(Request.Method.POST,volleySingletone.getUrl().toString(), new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    if(response.equalsIgnoreCase("TRANSACTION_COMPLETE")){
                        FileDeleter.deleteAFile(getFileName())   ;
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                }

            }){
                @Override
                public String getBodyContentType() {
                    return "application/json;charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return content.getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

            };
            VolleySingletone.getVolleyInstance(context.getApplicationContext()).add(stringRequest);
        }
    }
}
