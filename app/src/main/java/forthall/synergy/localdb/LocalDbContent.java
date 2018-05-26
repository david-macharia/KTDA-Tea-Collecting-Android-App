package forthall.synergy.localdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by MY-HOMEDESKTOP on 5/10/2017.
 */
public class LocalDbContent extends   ConstantSQLStatement {
    Context context;
private LocalDbHelper localDbHelper;

    public LocalDbContent(Context context){
        this.context=context;
        localDbHelper= new LocalDbHelper(context);
    }

   class LocalDbHelper extends SQLiteOpenHelper{
       LocalDbHelper(Context context){
           super(context,DATABASE_NAME,null,1);
       }
       @Override
       public void onCreate(SQLiteDatabase db) {

           db.rawQuery(CREATE_REGION_TABLE, null);
           Log.e("TABLE", "Region Table created");
           /*String CREATE_FACTORY_TABLE="CREATE TABLE "+FACTORY_TABLE_NAME+";";
           db.rawQuery(CREATE_FACTORY_TABLE,null);
           Log.e("TABLE", "Region Table created");*/
db.close();

       }

       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

       }
   }

}
