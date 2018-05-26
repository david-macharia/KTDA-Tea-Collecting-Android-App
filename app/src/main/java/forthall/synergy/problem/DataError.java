package forthall.synergy.problem;

/**
 * Created by MY-HOMEDESKTOP on 5/10/2017.
 */
public class DataError {
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public DataError(String message) {
        this.error=message;
    }
}
