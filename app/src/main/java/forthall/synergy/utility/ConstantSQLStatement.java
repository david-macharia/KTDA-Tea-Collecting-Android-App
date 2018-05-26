package forthall.synergy.utility;

/**
 * Created by MY-HOMEDESKTOP on 5/11/2017.
 */
public class ConstantSQLStatement implements ColumnCostants {
    protected static final String CREATE_REGION_TABLE="CREATE TABLE "+REGIONS_TABLE_NAME+" ("+REGION_NAME+" TEXT NOT NULL,"+NO_OF_CENTERS+" INTEGER NOT NULL," +
            "" +REGION_ID+" INTEGER);";
    protected static final String CREATE_TABLE_FACTORIES="CREATE TABLE "+FACTORY_TABLE_NAME+" ("+FACTORY_NAME+" TEXT NOT NULL,"+FACTORY_ID+" TEXT NOT NULL," +
            ""+NO_OF_CENTERS+" INT(11));";
   protected  static  final String CREATE_TABLE_CENTERS="CREATE TABLE "+CENTER_TABLE_NAME+" ("+CENTER_NAME+" VARCHAR(50) NOT NULL,"+CENTER_ID+" VARCHAR(50),"+FACTORY_ID
           +" VARCHAR(50) NOT NULL,"+NO_OF_GROWERS+" INTEGER);";
    protected  static  final String CREATE_TABLE_GROWERS="CREATE TABLE IF NOT EXISTS "+GROWER_TABLE_NAME+" (" +
            "  `Grower_name` varchar(50) NOT NULL," +
            "  `Grower_id` varchar(11) NOT NULL," +
            "  `center_id` varchar(11) NOT NULL," +
            "  `email` varchar(50) NOT NULL," +
            "  `phone_number` varchar(15) NOT NULL" +
            ");";
}
