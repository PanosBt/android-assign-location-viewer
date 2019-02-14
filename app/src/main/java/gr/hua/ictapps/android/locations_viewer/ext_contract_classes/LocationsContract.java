package gr.hua.ictapps.android.locations_viewer.ext_contract_classes;

public class LocationsContract {

    public static final String KEY_ID = "_id";
    public static final String KEY_USERID = "userid";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_DT = "dt";
    public static final String CONTENT_URL = "content://gr.hua.ictapps.android.locations_db_manager.provider/locations";
    public static final String AUTHORITY = "gr.hua.ictapps.android.locations_db_manager.provider";

    private int id;
    private String userid;
    private float longitude;
    private float latitude;
    private String dt;

    public LocationsContract() {
        this.id = 0;
    }

    public LocationsContract(String userid, float longitude, float latitude, String dt) {
        this.id = 0; // dummy initialisation
        this.userid = userid;
        this.longitude = longitude;
        this.latitude = latitude;
        this.dt = dt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUserid() {
        return userid;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public String getDt() {
        return dt;
    }
}
