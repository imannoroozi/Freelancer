package ir.weproject.freelance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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

import java.util.HashMap;
import java.util.Map;

import ir.weproject.freelance.freelance.AppConfig;
import ir.weproject.freelance.freelance.AppController;
import ir.weproject.freelance.freelance.R;
import ir.weproject.freelance.freelance.ReportAlertDialog;
import ir.weproject.freelance.helper.Farsi;
import ir.weproject.freelance.helper.SQLiteHandler;
import ir.weproject.freelance.helper.SessionManager;
import ir.weproject.freelance.ir.weproject.poem.objects.Comment;
import ir.weproject.freelance.ir.weproject.poem.objects.Project;
import ir.weproject.freelance.ir.weproject.poem.objects.Skill;


public class SingleProjectActivity extends AppCompatActivity {

    Button okButton;
    SessionManager session;

    Project project;
    public enum ACTIONS {LIKE_POST, BOOKMARK_POST, REPORT_POST, INSERT_COMMENT, RATE_POST};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_project);

        final HashMap<String, String> user;
        SQLiteHandler db = new SQLiteHandler(SingleProjectActivity.this);
        user = db.getUserDetails();

        project = (Project) getIntent().getSerializableExtra(AppConfig.EXTRA_KEY_PROJECT_OBJECT);

        TextView title = (TextView) findViewById(R.id.poem_post_title);
//        TextView authorName = (TextView) rowView.findViewById(R.id.author_name);
        TextView date = (TextView) findViewById(R.id.posted_date);
        final TextView content = (TextView) findViewById(R.id.poem_post_content);
        TextView initialCost = (TextView) findViewById(R.id.initial_cost);
        TextView initialDuration = (TextView) findViewById(R.id.initial_duration);
//        RoundedNetworkImageView authorImage = (RoundedNetworkImageView) rowView.findViewById(R.id.author_image);
        ImageButton bookmarkButton = (ImageButton) findViewById(R.id.bookmarkButton);
        ImageButton reportButton = (ImageButton) findViewById(R.id.reportButton);
        ImageButton moreButton = (ImageButton) findViewById(R.id.moreButton);
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        Button bidButton = (Button) findViewById(R.id.bidButton);

        TextView neededSkills = (TextView) findViewById(R.id.neededSkills);
        if( project.getNeededSkills() != null && project.getNeededSkills().size() > 0 ) {
            neededSkills.setText(Skill.getNeededSkillsString(project.getNeededSkills()));
        }else{
            RelativeLayout skillsHeaderLayout = (RelativeLayout) findViewById(R.id.needed_skills_header_layout);
            skillsHeaderLayout.setVisibility(View.GONE);
        }

//        final EditText commentContent = (EditText) rowView.findViewById(R.id.comment_content);
//        ImageButton sendComment = (ImageButton) rowView.findViewById(R.id.send_commnet);
//        TextView scoreText = (TextView) rowView.findViewById(R.id.post_score);

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

//        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
//        RoundedNetworkImageView authorImageNewComment = (RoundedNetworkImageView) rowView.findViewById(R.id.new_comment_author_image);

//        ListView comments = (ListView) rowView.findViewById(R.id.listView);

        title.setText(Farsi.Convert(project.getTitle()));
        date.setText(Farsi.Convert(project.getDate()));
        content.setText(Farsi.Convert(project.getDescription()));
        initialCost.setText(String.valueOf(project.getInitialCost()));
        initialDuration.setText(String.valueOf(project.getInitialDuration()));
//        ImageHelper.loadNetworkImage(getContext(), authorImage, project.getUser().getImageAddress());

        /*authorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewProfileIntent = new Intent(context, ViewProfileActivity.class);
                viewProfileIntent.putExtra(AppConfig.EXTRA_KEY_USER_ID, String.valueOf((new SessionManager(context)).getCurrentUserID()));
                viewProfileIntent.putExtra(AppConfig.EXTRA_KEY_AUTHOR_OBJECT, (Serializable) project.getUser());
                context.startActivity(viewProfileIntent);
            }
        });*/

        /*ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingBar.setFocusable(false);
                postAction(Integer.parseInt(user.get("uid")), freelance.getPostId(), rating, "RATE_POST");
            }
        });*/

        /*bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //likeButton.setFocusable(false);
                postAction(rowView, Integer.parseInt(user.get("uid")), project.getPostId(), (project.isBookmarked() ? 0 : 1), ACTIONS.BOOKMARK_POST, position, "");
            }
        });*/

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //likeButton.setFocusable(false);
                ReportAlertDialog dialog = new ReportAlertDialog();
                postAction(null, null);
                viewFlipper.showNext();
            }
        });
    }

    private void postAction(final ACTIONS action , final HashMap<String,String> data) {

        String tag_string_req = "posts_req";
        String action_URL = "";
        switch (action){
            case LIKE_POST:
                action_URL = AppConfig.URL_LIKE_POSTS;
                break;
            case BOOKMARK_POST:
                action_URL = AppConfig.URL_BOOKMARK_POSTS;
                break;
            case REPORT_POST:
                action_URL = AppConfig.URL_REPORT_POSTS;
                break;
            case RATE_POST:
                break;
            case INSERT_COMMENT:
                action_URL = AppConfig.URL_COMMENTS;
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
                        //                ratingBar.setFocusable(true);
                        break;
                    case INSERT_COMMENT:
                        try {
                            JSONArray jsonComments = jObj.getJSONArray("comments");
                            project.getComments().clear();
                            for(int i=0; i<jsonComments.length(); i++){
                                project.getComments().add(new Comment(jsonComments.getJSONObject(i)));
                            }
                            ListView comments = ((ListView) findViewById(R.id.listView));
                            ((BaseAdapter) comments.getAdapter()).notifyDataSetChanged();
                            //setListViewHeightBasedOnChildren(comments);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
                Map<String, String> params = new HashMap<>();
                params = data;
                params.put("action", action.name());
                return params;
            }
        };
        if( MainActivity.isNetworkAvailable(SingleProjectActivity.this)) {
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }
}