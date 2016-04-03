package ir.weproject.freelance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
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

import ir.weproject.freelance.helper.SQLiteHandler;
import ir.weproject.freelance.ir.weproject.poem.objects.User;
import ir.weproject.freelance.ir.weproject.poem.objects.Post;
import ir.weproject.freelance.ir.weproject.poem.objects.PostFactory;
import ir.weproject.freelance.freelance.AppConfig;
import ir.weproject.freelance.freelance.AppController;
import ir.weproject.freelance.freelance.PostsAdapter;
import ir.weproject.freelance.freelance.R;


public class SearchActivity extends AppCompatActivity {

    ListView listView;

    private int mPage;
    ArrayList<Post> posts;
    private SQLiteHandler db;
    int currentPage = 1;

    Button searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        posts = new ArrayList<>();

        Intent intent = getIntent();
        final User author = (User) intent.getSerializableExtra(AppConfig.EXTRA_KEY_AUTHOR_OBJECT);

        searchButton = (Button) findViewById(R.id.follow_button);

        listView = (ListView) findViewById(R.id.listView);

        final HashMap<String, String> user;
        db = new SQLiteHandler(this);
        user = db.getUserDetails();

        PostsAdapter postsAdapter = new PostsAdapter(this, posts);
        listView.setAdapter(postsAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //try to read from server or load cache
                readPostsFromServer(user.get("email"), currentPage);
            }
        });

    }

    private void readPostsFromServer(final String email, final int currentPage) {

        String tag_string_req = "posts_req";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_READ_HOME_POSTS, new Response.Listener<String>() {

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
                params.put("email", email);
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
            PostFactory postFactory = new PostFactory();

            if (!error) {
                JSONArray jsonPosts = jObj.getJSONArray("posts");
                for(int i=0; i<jsonPosts.length(); i++){
                    posts.add(postFactory.getPost(jsonPosts.getJSONObject(i)));
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

    private void processResponse( String response, String action ){
        try {
            // Check for error node in json
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");

            if (!error) {
            }
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}