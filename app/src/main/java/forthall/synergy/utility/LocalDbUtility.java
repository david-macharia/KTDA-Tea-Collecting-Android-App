package forthall.synergy.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import forthall.synergy.DatabaseObjects.BuyingCenter;
import forthall.synergy.DatabaseObjects.Center;
import forthall.synergy.DatabaseObjects.Factory;
import forthall.synergy.DatabaseObjects.Grower;

/**
 * Created by MY-HOMEDESKTOP on 5/12/2017.
 */
public class LocalDbUtility extends  ConstantSQLStatement {
    private SQLiteDatabase sqLiteDatabase;
    private Context context;
    private boolean isDbDeleted;
    private boolean isDataBuffered;
    public boolean isDbDeleted(){
        return  isDbDeleted;
    }
    public  void setDbDeleted(boolean state){
        this.isDbDeleted=state;
    }
    public LocalDbUtility(LocalDbContent LocalDbContent){
        this.sqLiteDatabase=LocalDbContent.getSqliteDb();
        this.context=LocalDbContent.getContext();
    }
    public void setContext(Context context){
        this.context=context;
    }
    public void insertFactory(Factory factory){
        ContentValues contentValues= new ContentValues();
         if(factory!=null){
             if(factory.getFactoryName()!=null){
                 contentValues.put(FACTORY_NAME,factory.getFactoryName());
             }
             if(factory.getFactoryId()!=null){
                 contentValues.put(FACTORY_ID,factory.getFactoryId());
             }
             contentValues.put(NO_OF_CENTERS,factory.getnumberOfCenters());
             sqLiteDatabase.insert(FACTORY_TABLE_NAME,null,contentValues);
         }
    }
    public void insertAllFactories(List<Factory> factoryList){
        for(Factory factory:factoryList){
            insertFactory( factory)  ;
        }
    }
    public List<Factory> getAllFactories(){
        List<Factory> factoryList= new ArrayList<>();
        String sqlStatement="SELECT * FROM "+ ColumnCostants.FACTORY_TABLE_NAME;
        Cursor cursor=sqLiteDatabase.rawQuery(sqlStatement, null);
        if(cursor.moveToFirst()){
            do{
                factoryList.add(new Factory(cursor.getString(0), cursor.getString(1), cursor.getInt(2)));

            }while(cursor.moveToNext());
            return factoryList;
        }else{
            return null;
        }

    }
    public  Factory getFactory(String factoryName){
        String sqlStatement="SELECT * FROM "+ ColumnCostants.FACTORY_TABLE_NAME+" WHERE "+FACTORY_NAME+"='"+factoryName+"'";
        Cursor cursor=sqLiteDatabase.rawQuery(sqlStatement, null);
        Log.e("Execued",sqlStatement);
        if(cursor.moveToFirst()){
            do{

                return new Factory(cursor.getString(0), cursor.getString(1), cursor.getInt(2));

            }while(cursor.moveToNext());
        }
        return null;
    }
    public void insertCenter(BuyingCenter center) {

        if (center != null) {
            ContentValues contentValues= new ContentValues();
            contentValues.put(CENTER_NAME,center.getCenterName());
            contentValues.put(CENTER_ID,center.getCenterId());
            contentValues.put(FACTORY_ID,center.getFactoryId());
            contentValues.put(NO_OF_GROWERS,center.getNumberOfGrowers());
            sqLiteDatabase.insert(CENTER_TABLE_NAME,null,contentValues);

        }
    }
    public void insertAllCenters(List<BuyingCenter> centers){
        if (centers != null) {
        for(BuyingCenter center:centers) {

                ContentValues contentValues = new ContentValues();
                contentValues.put(CENTER_NAME, center.getCenterName());
                contentValues.put(CENTER_ID, center.getCenterId());
                contentValues.put(FACTORY_ID, center.getFactoryId());
                contentValues.put(NO_OF_GROWERS, center.getNumberOfGrowers());
                sqLiteDatabase.insert(CENTER_TABLE_NAME, null, contentValues);

            }
        }
    }
    public BuyingCenter getCenter(String centerName){
        String sqlStatement="SELECT * FROM "+CENTER_TABLE_NAME+" WHERE "+CENTER_NAME+" ='"+centerName+"'";
        Cursor cursor=sqLiteDatabase.rawQuery(sqlStatement,null);
        if(cursor.moveToFirst()){
            do{

                BuyingCenter center= new BuyingCenter();
                center.setCenterName(cursor.getString(0));
                center.setCenterId(cursor.getString(1));
                center.setFactoryId(cursor.getString(2));
                center.setNumberOfGrowers(Integer.valueOf(cursor.getString(3)));
                return  center;
            }while (cursor.moveToNext());
        }
        return null;
    }
    public List<BuyingCenter> getAllCenters(){
        String sqlStatement="SELECT * FROM "+CENTER_TABLE_NAME;
        List<BuyingCenter> centerList= new ArrayList<>();
        Cursor cursor=sqLiteDatabase.rawQuery(sqlStatement,null);
        if(cursor.moveToFirst()){
            do{

                BuyingCenter center= new BuyingCenter();
                center.setCenterName(cursor.getString(0));
                center.setCenterId(cursor.getString(1));
                center.setFactoryId(cursor.getString(2));
                center.setNumberOfGrowers(Integer.valueOf(cursor.getString(3)));
                centerList.add(center) ;
            }while (cursor.moveToNext());
            return  centerList;
        }
        return  null;
    }
    public Grower getGrower(String growerNumber){
        String sqlStatement="SELECT * FROM "+GROWER_TABLE_NAME+" WHERE "+GROWEER_ID+" ='"+growerNumber+"'";
        Cursor cursor=sqLiteDatabase.rawQuery(sqlStatement,null);
        if(cursor.moveToFirst()){
            do{

                Grower grower= new Grower();
                grower.setGrowerName(cursor.getString(0));
                grower.setGrowerId(cursor.getString(1));
                grower.setCenterId(cursor.getString(2));
                grower.setEmail(cursor.getString(3));

                return  grower;
            }while (cursor.moveToNext());
        }

        return  null;
    }
    public void  insertGrower(Grower grower){
        if(grower!=null){
            ContentValues contentValues= new ContentValues();
            contentValues.put(GROWER_NAME,grower.getGrowerName());
            contentValues.put(GROWEER_ID,grower.getGrowerId());
            contentValues.put(CENTER_ID,grower.getCenterId());
            contentValues.put(EMAIL,grower.getEmail());
            contentValues.put(PHONE_NUMBER,grower.getPhoneNumber());
            sqLiteDatabase.insert(GROWER_TABLE_NAME,null,contentValues);

        }

    }
    public List<Grower> getAllGrowersInCenter( String centerId){
        String sqlStatement="SELECT * FROM "+GROWER_TABLE_NAME +" WHERE "+ColumnCostants.CENTER_ID+" ='"+centerId+"'";
        List<Grower> growerList= new ArrayList<>();
        Cursor cursor=sqLiteDatabase.rawQuery(sqlStatement,null);
        if(cursor.moveToFirst()){
            do{

                Grower grower= new Grower();
                grower.setGrowerName(cursor.getString(0));
                grower.setGrowerId(cursor.getString(1));
                grower.setCenterId(cursor.getString(2));
                grower.setEmail(cursor.getString(3));

                growerList.add(grower);
            }while (cursor.moveToNext());
        }else{
            return  null;
        }
        cursor.close();
        return  growerList;
    }
    public List<Grower> getAllGrowers(){
        String sqlStatement="SELECT * FROM "+GROWER_TABLE_NAME;
        List<Grower> growerList= new ArrayList<>();
        Cursor cursor=sqLiteDatabase.rawQuery(sqlStatement,null);
        if(cursor.moveToFirst()){
            do{

                Grower grower= new Grower();
                grower.setGrowerName(cursor.getString(0));
                grower.setGrowerId(cursor.getString(1));
                grower.setCenterId(cursor.getString(2));
                grower.setEmail(cursor.getString(3));

               growerList.add(grower);
            }while (cursor.moveToNext());
        }else{
            return  null;
        }
        cursor.close();
        return  growerList;

    }
    public void insertAllGrowers(List<Grower>growers){
        if(growers!=null){
            for(Grower grower:growers) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(GROWER_NAME, grower.getGrowerName());
                contentValues.put(GROWEER_ID, grower.getGrowerId());
                contentValues.put(CENTER_ID, grower.getCenterId());
                contentValues.put(EMAIL, grower.getEmail());
                contentValues.put(PHONE_NUMBER, grower.getPhoneNumber());
                sqLiteDatabase.insert(GROWER_TABLE_NAME, null, contentValues);
            }
        }
    }
    public void dropDatabase(){
        context.deleteDatabase(DATABASE_NAME);

    }
    public  void cleanBufferedFactory(){
        String factoryTable="DELETE FROM "+FACTORY_TABLE_NAME;
        sqLiteDatabase.execSQL(factoryTable);
    }
    public void cleanBufferedCenter(){
        String centerTable="DELETE FROM "+CENTER_TABLE_NAME;
        sqLiteDatabase.execSQL(centerTable);
    }
    public void cleanBufferedGrower(){
        String growerTable="Delete FROM "+GROWER_TABLE_NAME;
        sqLiteDatabase.execSQL(growerTable);
    }
    public void cleanLocalBuffer(){
        String factoryTable="DELETE FROM "+FACTORY_TABLE_NAME;
        String centerTable="DELETE FROM "+CENTER_TABLE_NAME;

        String growerTable="Delete FROM "+GROWER_TABLE_NAME;
        sqLiteDatabase.execSQL(centerTable);
        sqLiteDatabase.execSQL(growerTable);
        sqLiteDatabase.execSQL(factoryTable);
        isDataBuffered=false;
    }
    public boolean isLocalBufferEmpty(){return isDataBuffered;
    }
    public Cursor getGenericLocalData(String sqlstatement){
        return sqLiteDatabase.rawQuery(sqlstatement,null);
    }

    public String getPhoneFromLocal(String growerid){
        String sqlStatement="SELECT "+ ColumnCostants.PHONE_NUMBER+" FROM "+ColumnCostants.GROWER_TABLE_NAME+" WHERE "+ColumnCostants.GROWEER_ID+" ='"+growerid+" '";
        Cursor cursor= sqLiteDatabase.rawQuery(sqlStatement,null);
        if(cursor.moveToFirst()){
            do{
                return cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return " ";
    }
}
