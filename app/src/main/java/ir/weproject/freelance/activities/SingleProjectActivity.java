package ir.weproject.freelance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.weproject.freelance.freelance.AppConfig;
import ir.weproject.freelance.freelance.AppController;
import ir.weproject.freelance.freelance.BidsAdapter;
import ir.weproject.freelance.freelance.R;
import ir.weproject.freelance.freelance.ReportAlertDialog;
import ir.weproject.freelance.helper.Farsi;
import ir.weproject.freelance.helper.ImageHelper;
import ir.weproject.freelance.helper.RoundedNetworkImageView;
import ir.weproject.freelance.helper.SessionManager;
import ir.weproject.freelance.ir.weproject.poem.objects.Bid;
import ir.weproject.freelance.ir.weproject.poem.objects.Project;


public class SingleProjectActivity extends AppCompatActivity {

    SessionManager session;

    Project project;
    int bidsPage = 1;
    public enum ACTIONS {GET_POST, LIKE_POST, BOOKMARK_POST, REPORT_POST, RATE_POST, GET_BIDS};

    TextView title,
            authorName ,
            date ,
            initialCost,
            initialDuration,
            content,
            neededSkills;
    RoundedNetworkImageView authorImage;
    ImageButton bookmarkButton ,
            reportButton,
            moreButton ,
            backButton;
    Button bidButton;
    ArrayList<Bid> bids;
    BidsAdapter bidAdapter;
    ListView bidsListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_project);

        session = new SessionManager(this);
        bids = new ArrayList<>();
//        project = (Project) getIntent().getSerializableExtra(AppConfig.EXTRA_KEY_PROJECT_OBJECT);

        title = (TextView) findViewById(R.id.poem_post_title);
        authorName = (TextView) findViewById(R.id.author_name);

        date = (TextView) findViewById(R.id.posted_date);
        content = (TextView) findViewById(R.id.poem_post_content);
        initialCost = (TextView) findViewById(R.id.initial_cost);
        initialDuration = (TextView) findViewById(R.id.initial_duration);
        authorImage = (RoundedNetworkImageView) findViewById(R.id.author_image);
        bookmarkButton = (ImageButton) findViewById(R.id.bookmarkButton);
        reportButton = (ImageButton) findViewById(R.id.reportButton);
        moreButton = (ImageButton) findViewById(R.id.moreButton);
        backButton = (ImageButton) findViewById(R.id.backButton);
        bidButton = (Button) findViewById(R.id.bidButton);
        bidsListView = (ListView) findViewById(R.id.bids_list);
        neededSkills = (TextView) findViewById(R.id.neededSkills);
        /*if( project.getNeededSkills() != null && project.getNeededSkills().size() > 0 ) {
            neededSkills.setText(Skill.getNeededSkillsString(project.getNeededSkills()));
        }else{
            RelativeLayout skillsHeaderLayout = (RelativeLayout) findViewById(R.id.needed_skills_header_layout);
            skillsHeaderLayout.setVisibility(View.GONE);
        }*/

        final ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        View.OnClickListener viewFlipperListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
            }
        };

        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bidActivity = new Intent(SingleProjectActivity.this, PutBidActivity.class);
                bidActivity.putExtra(AppConfig.EXTRA_KEY_PROJECT_OBJECT, project);
                startActivity(bidActivity);
            }
        });
        moreButton.setOnClickListener(viewFlipperListener);
        backButton.setOnClickListener(viewFlipperListener);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.bidder_rating_bar);

        authorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewProfileIntent = new Intent(SingleProjectActivity.this, ViewProfileActivity.class);
                viewProfileIntent.putExtra(AppConfig.EXTRA_KEY_USER_ID, String.valueOf((new SessionManager(SingleProjectActivity.this)).getCurrentUserID()));
                viewProfileIntent.putExtra(AppConfig.EXTRA_KEY_AUTHOR_OBJECT, (Serializable) project.getUser());
                startActivity(viewProfileIntent);
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingBar.setFocusable(false);
                postAction( ACTIONS.BOOKMARK_POST, null);
            }
        });

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //likeButton.setFocusable(false);
                postAction( ACTIONS.BOOKMARK_POST, null);
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //likeButton.setFocusable(false);
                ReportAlertDialog dialog = new ReportAlertDialog();
                postAction(null, null);
                viewFlipper.showNext();
            }
        });

        HashMap<String, String> data = new HashMap<>();
        data.put("user_id", String.valueOf(session.getCurrentUserID()));
        data.put("project_id", String.valueOf(getIntent().getIntExtra(AppConfig.EXTRA_KEY_PROJECT_ID,0)));
        postAction(ACTIONS.GET_POST, data);

        //load the bid list
        bidAdapter = new BidsAdapter(SingleProjectActivity.this, bids);
        bidsListView.setAdapter(bidAdapter);
        postAction(ACTIONS.GET_BIDS, data);
    }

    private void postAction(final ACTIONS action , final HashMap<String,String> data) {

        String tag_string_req = "projects_req";
        String action_URL = "";

        switch (action){
            case GET_POST:
            case GET_BIDS:
                action_URL = AppConfig.URL_READ_PROJECTS;
                break;
            case LIKE_POST:
                action_URL = AppConfig.URL_LIKE_POSTS;
                break;
            case BOOKMARK_POST:
                action_URL = AppConfig.URL_BOOKMARK_POSTS;
                break;
            case REPORT_POST:
                action_URL = AppConfig.URL_REPORT_POSTS;
                break;
            default:
                break;
        }
        StringRequest strReq = new StringRequest(Request.Method.POST,
                action_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                JSONObject jObj = null;
                boolean error = true;
                try {
                    jObj = new JSONObject(response);
                    error = jObj.getBoolean("error");
                } catch (JSONException e) {}

                if(error){
                    Toast.makeText(SingleProjectActivity.this, "Action not successful", Toast.LENGTH_LONG).show();
                    return;
                }

                switch (action){

                    case GET_POST:
                        try {
                            project = new Project(jObj.getJSONObject("post"));
                            updateView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case GET_BIDS:
                        JSONArray jsonPosts = null;
                        try {
                            jsonPosts = jObj.getJSONArray("bids");
                            for(int i=0; i<jsonPosts.length(); i++){
                                bids.add(new Bid(jsonPosts.getJSONObject(i)));
                            }
                            bidAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LIKE_POST:

                        break;
                    case BOOKMARK_POST:
//                        bookmarkButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, rate == 1 ? R.drawable.ic_bookmark_black_24dp : R.drawable.ic_bookmark_border_black_24dp), null, null);
                        project.setBookmarked(1 == 1 ? true : false);
                        break;
                    case REPORT_POST:
                        Toast.makeText(SingleProjectActivity.this, "Reported", Toast.LENGTH_LONG).show();
                        break;
                    case RATE_POST:
                        break;

                    default:
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //show the error
                Toast.makeText(SingleProjectActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to url
                Map<String, String> params = data;
                params.put("action", action.name());
                return params;
            }
        };
        if( MainActivity.isNetworkAvailable(SingleProjectActivity.this)) {
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    public void updateView(){
        title.setText(Farsi.Convert(project.getTitle()));
        date.setText(Farsi.Convert(project.getDate()));
        content.setText(Farsi.Convert(project.getDescription()));
        initialCost.setText(String.valueOf(project.getInitialCost()));
        initialDuration.setText(String.valueOf(project.getInitialDuration()));
        ImageHelper.loadNetworkImage(SingleProjectActivity.this, authorImage, project.getUser().getImageAddress());
    }
}