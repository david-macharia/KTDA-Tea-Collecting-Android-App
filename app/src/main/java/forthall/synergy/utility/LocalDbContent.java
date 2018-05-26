package forthall.synergy.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by MY-HOMEDESKTOP on 5/10/2017.
 */
public class LocalDbContent extends   ConstantSQLStatement {
    private Context context;
private LocalDbHelper localDbHelper;
private SQLiteDatabase sqLiteDatabase;
    public LocalDbContent(Context context){
        this.context=context;
        localDbHelper= new LocalDbHelper(context);
        sqLiteDatabase= localDbHelper.getWritableDatabase();
    }
    public Context getContext(){
        return context;
    }
public boolean isFactoryDatabaseEmpty(){
   Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+GROWER_TABLE_NAME, null);
    if(cursor.moveToFirst()){
     return false;
    }else{
       return true;
    }
     }
    public SQLiteDatabase getSqliteDb(){
        return sqLiteDatabase;
    }

   class LocalDbHelper extends SQLiteOpenHelper{
       LocalDbHelper(Context context){
           super(context, DATABASE_NAME,null,1);
       }
       @Override
       public void onCreate(SQLiteDatabase db) {
           Log.e("TABLE", " about to create table Region ");
           db.execSQL(CREATE_REGION_TABLE);
           Log.e("TABLE", CREATE_REGION_TABLE);

           db.execSQL(CREATE_TABLE_FACTORIES);
           Log.e("TABLE", CREATE_TABLE_FACTORIES);
           db.execSQL(CREATE_TABLE_CENTERS);

           db.execSQL(CREATE_TABLE_GROWERS);
           Log.e("TABLE", CREATE_TABLE_GROWERS);



       }

       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

       }
   }

}
