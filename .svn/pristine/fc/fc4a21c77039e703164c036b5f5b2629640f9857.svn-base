package ir.weproject.freelance.freelance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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
import ir.weproject.freelance.helper.CacheHelper;
import ir.weproject.freelance.helper.OnDetectScrollListener;
import ir.weproject.freelance.helper.SQLiteHandler;
import ir.weproject.freelance.helper.ScrollListView;
import ir.weproject.freelance.helper.SessionManager;
import ir.weproject.freelance.ir.weproject.poem.objects.Post;
import ir.weproject.freelance.ir.weproject.poem.objects.PostFactory;


public class HomeFragmentFreelancer extends Fragment implements AdapterView.OnItemClickListener {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    ArrayList<Post> posts;
    private SQLiteHandler db;
    SessionManager session;
    int currentPage = 1;

    ScrollListView listView;
//    ImageButton newProjectButton;

   public static Fragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        HomeFragmentFreelancer fragment = new HomeFragmentFreelancer();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPage = getArguments().getInt(ARG_PAGE);

        db = new SQLiteHandler(getActivity());
        session = new SessionManager(getActivity());
        posts = new ArrayList<Post>();

        //try to read from server or load cache
        readPostsFromServer(session.getCurrentUserID(), currentPage);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        listView = (ScrollListView) view.findViewById(R.id.listView);
//        newProjectButton = (ImageButton) view.findViewById(R.id.newProjectBtn);

        //get current user details
        HashMap<String, String> user;
        user = db.getUserDetails();

        //load data from cache
        PostsAdapter postsAdapter = new PostsAdapter(getActivity(), posts);
        listView.setAdapter(postsAdapter);

        listView.setOnItemClickListener(this);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //load Cache
        //updateList(CacheHelper.readFromFile(getActivity()));
    }


    private void readPostsFromServer(final int userID, final int currentPage) {

        String tag_string_req = "posts_req";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_READ_PROJECTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                updateList(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //show the error
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                // Posting parameters to url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", String.valueOf(userID));
                params.put("page", String.valueOf(currentPage));
                params.put("action", "GET_ALL_PROJECTS");

                return params;
            }

        };
        if( MainActivity.isNetworkAvailable(getActivity())) {
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }else{
            //Not connected to internet
            Toast.makeText(getActivity(),
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

                //update cache file
                CacheHelper.writeToFile(getActivity(), response);

                //update list view
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            } else {
                // Error in receiving posts. Get the error message
                String errorMsg = jObj.getString("desc");
                Toast.makeText(getActivity(),
                        errorMsg, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
            Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }

}
