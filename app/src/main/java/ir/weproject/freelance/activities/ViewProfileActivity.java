package ir.weproject.freelance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.weproject.freelance.helper.ImageHelper;
import ir.weproject.freelance.helper.SQLiteHandler;
import ir.weproject.freelance.ir.weproject.poem.objects.User;
import ir.weproject.freelance.ir.weproject.poem.objects.Post;
import ir.weproject.freelance.ir.weproject.poem.objects.PostFactory;
import ir.weproject.freelance.freelance.AppConfig;
import ir.weproject.freelance.freelance.AppController;
import ir.weproject.freelance.freelance.PostsAdapter;
import ir.weproject.freelance.freelance.R;


public class ViewProfileActivity extends AppCompatActivity {

    ListView listView;

    public enum relationStatus {NO_RELATION, ACTIVE_RELATION, PENDING_RELATION, REQUESTED_RELATION};

    private int mPage;
    private relationStatus relationship;
    ArrayList<Post> posts;
    private SQLiteHandler db;
    int currentPage = 1;

    Button followButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        posts = new ArrayList<>();

        Intent intent = getIntent();
        final User author = (User) intent.getSerializableExtra(AppConfig.EXTRA_KEY_AUTHOR_OBJECT);

        TextView authorName = (TextView) findViewById(R.id.member_name);
        TextView authorDesc = (TextView) findViewById(R.id.member_desc);
        followButton = (Button) findViewById(R.id.follow_button);

        listView = (ListView) findViewById(R.id.listView);

        NetworkImageView image = (NetworkImageView) findViewById(R.id.author_image);
        ImageHelper.loadNetworkImage(this,image, null);

        final HashMap<String, String> user;
        db = new SQLiteHandler(this);
        user = db.getUserDetails();

        PostsAdapter postsAdapter = new PostsAdapter(this, posts);
        listView.setAdapter(postsAdapter);

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( relationship == relationStatus.NO_RELATION ){
                    updateRelation(Integer.parseInt(user.get("uid")), author.getID(), "FOLLOW");
                }else if( relationship == relationStatus.ACTIVE_RELATION ){
                    updateRelation(Integer.parseInt(user.get("uid")), author.getID(), "UNFOLLOW");
                }else if( relationship == relationStatus.PENDING_RELATION ){
                    updateRelation(Integer.parseInt(user.get("uid")), author.getID(), "ACCEPT");
                }else if( relationship == relationStatus.REQUESTED_RELATION ){
                    updateRelation(Integer.parseInt(user.get("uid")), author.getID(), "CANCEL");
                }
            }
        });

        //try to read from server or load cache
        readPostsFromServer(user.get("email"), currentPage);
        updateRelation(Integer.parseInt(user.get("uid")), author.getID(), "relation_status");
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

    private void updateRelation(final int userID, final int authorID, final String action) {

        String tag_string_req = "posts_req";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_READ_RELATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               processResponse(response, action);
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
                params.put("follower_id", String.valueOf(userID));
                params.put("followee_id", String.valueOf(authorID));
                params.put("action", action);
                return params;
            }
        };
        if( MainActivity.isNetworkAvailable(getBaseContext())) {
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
                switch (action){
                    case "relation_status":
                    case "FOLLOW":
                    case "UNFOLLOW":
                    case "ACCEPT":
                    case "CANCEL":
                    case "PENDING":
                        relationship = relationStatus.valueOf(jObj.getString("relation"));
                        updateRelationButtons();
                        break;
                    default:
                        break;
                }
            }
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void updateRelationButtons() {
        switch (relationship){
            case NO_RELATION:
                followButton.setText("Follow");
                followButton.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(ViewProfileActivity.this, R.drawable.ic_person_add_black_24dp),null,null,null);
                break;
            case ACTIVE_RELATION:
                followButton.setText("Unfollow");
                followButton.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(ViewProfileActivity.this, R.drawable.ic_group_black_24dp), null, null, null);
                break;
            case PENDING_RELATION:
                followButton.setText("Accept");
                followButton.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(ViewProfileActivity.this, R.drawable.ic_person_add_black_24dp), null, null, null);
                break;
            case REQUESTED_RELATION:
                followButton.setText("Requested");
                followButton.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(ViewProfileActivity.this, R.drawable.ic_person_add_black_24dp), null, null, null);
                break;
            default:
                break;
        }
    }
}