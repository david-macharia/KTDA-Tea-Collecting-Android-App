package forthall.synergy.postingdata;

/**
 * Created by MY-HOMEDESKTOP on 3/8/2017.
 */
public class PostMetadata {
    private String url;
    private String  JSONEDBags;
    private String growerName;
private String fileName;
    public PostMetadata(String url, String JSONEDBags, String growerName,String fileName) {
        this.url = url;
        this.JSONEDBags = JSONEDBags;
        this.growerName = growerName;
        this.fileName=fileName;
    }
    public PostMetadata(String url, String JSONEDBags, String growerName) {
        this.url = url;
        this.JSONEDBags = JSONEDBags;
        this.growerName = growerName;

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJSONEDBags() {
        return JSONEDBags;
    }

    public void setJSONEDBags(String JSONEDBags) {
        this.JSONEDBags = JSONEDBags;
    }

    public String getGrowerName() {
        return growerName;
    }

    public void setGrowerName(String growerName) {
        this.growerName = growerName;
    }
}
