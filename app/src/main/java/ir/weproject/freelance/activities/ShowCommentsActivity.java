package ir.weproject.freelance.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
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

import ir.weproject.freelance.helper.CacheHelper;
import ir.weproject.freelance.ir.weproject.poem.objects.Comment;
import ir.weproject.freelance.freelance.AppConfig;
import ir.weproject.freelance.freelance.AppController;
import ir.weproject.freelance.freelance.CommentsAdapter;
import ir.weproject.freelance.freelance.R;

/**
 * Created by Iman on 1/3/2016.
 */
public class ShowCommentsActivity extends AppCompatActivity {

    private static final int NUMBER_OF_COMMENTS = 10;
    ArrayList<Comment> comments;
    int currentPage = 0;
    private int userID;
    private int postID;
    ListView listView;

    public enum ACTIONS {GET_COMMENTS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comments);
        comments = new ArrayList<>();

        userID = getIntent().getIntExtra(AppConfig.EXTRA_KEY_USER_ID, -1);
        postID = getIntent().getIntExtra(AppConfig.EXTRA_KEY_POST_ID, -1);

        listView = (ListView) findViewById(R.id.listView);
        CommentsAdapter adapter = new CommentsAdapter(ShowCommentsActivity.this, comments, userID, postID );
        listView.setAdapter(adapter);

        readCommentsFromServer(userID, postID, currentPage++);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }


    private void readCommentsFromServer(final int userID, final int postID, final int currentPage) {

        String tag_string_req = "posts_req";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_COMMENTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                updateList(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //show the error
                Toast.makeText(ShowCommentsActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                // Posting parameters to url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", String.valueOf(userID));
                params.put("post_id", String.valueOf(postID));
                params.put("action", ACTIONS.GET_COMMENTS.name() );
                params.put("page", String.valueOf(currentPage));
                params.put("number_of_comments", String.valueOf(NUMBER_OF_COMMENTS));
                params.put("offset", String.valueOf(currentPage * NUMBER_OF_COMMENTS ));

                return params;
            }

        };
        if( MainActivity.isNetworkAvailable(ShowCommentsActivity.this)) {
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }else{
            //Not connected to internet
            Toast.makeText(ShowCommentsActivity.this,
                    "Disconnect!", Toast.LENGTH_LONG).show();
        }

    }

    private void updateList(String response) {
        try {
            // Check for error node in json
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");

            if (!error) {
                JSONArray jsonPosts = jObj.getJSONArray("comments");
                for(int i=0; i<jsonPosts.length(); i++){
                    comments.add(new Comment(jsonPosts.getJSONObject(i)));
                }

                //update cache file
                CacheHelper.writeToFile(ShowCommentsActivity.this, response);

                //update list view
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            } else {
                // Error in receiving posts. Get the error message
                String errorMsg = jObj.getString("desc");
                Toast.makeText(ShowCommentsActivity.this,
                        errorMsg, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
            Toast.makeText(ShowCommentsActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
