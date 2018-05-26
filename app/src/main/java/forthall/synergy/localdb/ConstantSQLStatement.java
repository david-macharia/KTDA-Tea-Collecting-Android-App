package forthall.synergy.localdb;

/**
 * Created by MY-HOMEDESKTOP on 5/11/2017.
 */
public class ConstantSQLStatement implements ColumnCostants {
    protected static final String CREATE_REGION_TABLE="CREATE TABLE "+REGIONS_TABLE_NAME+" ("+REGION_NAME+" TEXT NOT NULL,"+NO_OF_CENTERS+" INTEGER NOT NULL," +
            ""+NO_OF_CENTERS+" INTEGER,"+REGION_ID+" INTEGER);";
}
