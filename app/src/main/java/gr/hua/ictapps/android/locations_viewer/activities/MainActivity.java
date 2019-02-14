package gr.hua.ictapps.android.locations_viewer.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import gr.hua.ictapps.android.locations_viewer.R;
import gr.hua.ictapps.android.locations_viewer.ext_contract_classes.LocationsContract;

public class MainActivity extends AppCompatActivity {

    private TextView errorText;
    private ProgressBar progressBar;
    private Button button;
    private EditText editText;
    private LocationsContract locationsContract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationsContract = new LocationsContract();
        errorText = findViewById(R.id.main_text_view_error);
        (progressBar = findViewById(R.id.loadingPanel)).setVisibility(View.GONE);
        button = findViewById(R.id.main_button_view);
        editText = findViewById(R.id.main_edit_text_userid);

        // onClickListener for VIEW button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorText.setVisibility(View.GONE);

                String userid = editText.getText().toString().trim();

                // if no input provided, show error msg and return
                if (TextUtils.isEmpty(userid)) {
                    errorText.setText(R.string.activity_main_error_msg_empty_user_id);
                    errorText.setVisibility(View.VISIBLE);
                    return;
                }

                // if user provided a userid start a background thread (through AsyncTask)
                // to fetch locations
                LocationFetcherWorker locationFetcherWorker = new LocationFetcherWorker();
                locationFetcherWorker.execute(userid);
            }
        });
    }

    // AsyncTask to fetch locations from the ContentResolver w/o blocking the ui thread
    public class LocationFetcherWorker extends AsyncTask<String, Integer, ArrayList<LatLng>> {

        @Override
        protected ArrayList<LatLng> doInBackground(String... params) {
            // fetch locations for given userid in params
            // and store them in an ArrayList
            String userid = params[0];
            ArrayList<LatLng> coordsList = new ArrayList<>();
            Cursor cursor = getContentResolver().query(
                    Uri.parse(locationsContract.CONTENT_URL),
                    new String[]{locationsContract.KEY_LATITUDE, locationsContract.KEY_LONGITUDE},
                    locationsContract.KEY_USERID + "=?",
                    new String[]{userid},
                    null
            );
            if (cursor.moveToFirst())
                do {
                    Long lat = ((Float) cursor.getFloat(0)).longValue();
                    Long lon = ((Float) cursor.getFloat(1)).longValue();
                    LatLng coords = new LatLng(lat, lon);
                    coordsList.add(coords);
                } while (cursor.moveToNext());
            return coordsList;
        }

        @Override
        protected void onPreExecute() {
            // hide the button and the edittext and show progress bar
            button.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<LatLng> coordsList) {
            if (coordsList.isEmpty()) {
                // if no locations found, hide progress bar
                // show the hidden button and editText
                // and show err msg
                progressBar.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);
                errorText.setText(R.string.ctivity_main_error_msg_no_coords_found);
                errorText.setVisibility(View.VISIBLE);
            }
            else{
                Intent intent = new Intent();
                intent.setAction("gr.hua.ictapps.android.locations_viewer.activities.view_map");
                intent.putParcelableArrayListExtra("coordsList", coordsList);
                startActivity(intent);
                finish();
            }
        }
    }

}