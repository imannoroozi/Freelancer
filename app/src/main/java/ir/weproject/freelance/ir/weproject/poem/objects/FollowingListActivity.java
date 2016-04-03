package ir.weproject.freelance.ir.weproject.poem.objects;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.weproject.freelance.activities.MainActivity;
import ir.weproject.freelance.helper.SessionManager;
import ir.weproject.freelance.freelance.AppConfig;
import ir.weproject.freelance.freelance.AppController;
import ir.weproject.freelance.freelance.FollowingAdapter;
import ir.weproject.freelance.freelance.R;


public class FollowingListActivity extends AppCompatActivity {

    ListView listView;

    private int mPage;
    ArrayList<User> followings;
    private SessionManager session;
    int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_list);

        followings = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listView);

        session = new SessionManager(this);

        FollowingAdapter followingsAdapter = new FollowingAdapter(this, followings);
        listView.setAdapter(followingsAdapter);

        //try to read from server or load cache
        readFollowingsFromServer(session.getCurrentUserID(), currentPage);
    }

    private void readFollowingsFromServer(final int uid, final int currentPage) {

        String tag_string_req = "posts_req";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_READ_FOLLOWINGS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                updateList(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //show the error
                Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                // Posting parameters to url
                Map<String, String> params = new HashMap<>();
                params.put("follower_id", String.valueOf(uid));
                params.put("action", "list");
                params.put("page", String.valueOf(currentPage));

                return params;
            }

        };
        if( MainActivity.isNetworkAvailable(getBaseContext())) {
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }else{
            //Not connected to internet
            Toast.makeText(getBaseContext(),
                    "Disconnect!", Toast.LENGTH_LONG).show();
        }

    }

    private void updateList(String response) {
        try {
            // Check for error node in json
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");

            if (!error) {
                JSONArray jsonFollowings = jObj.getJSONArray("followings");
                for(int i=0; i<jsonFollowings.length(); i++){
                    followings.add(new User(jsonFollowings.getJSONObject(i)));
                }

                //update list view
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            } else {
                // Error in receiving posts. Get the error message
                String errorMsg = jObj.getString("error_msg");
                Toast.makeText(getBaseContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}