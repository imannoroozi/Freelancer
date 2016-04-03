package ir.weproject.freelance.freelance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import java.util.List;
import java.util.Map;

import ir.weproject.freelance.activities.MainActivity;
import ir.weproject.freelance.activities.PutBidActivity;
import ir.weproject.freelance.activities.ShowCommentsActivity;
import ir.weproject.freelance.helper.Farsi;
import ir.weproject.freelance.helper.SQLiteHandler;
import ir.weproject.freelance.helper.SessionManager;
import ir.weproject.freelance.ir.weproject.poem.objects.Comment;
import ir.weproject.freelance.ir.weproject.poem.objects.Project;
import ir.weproject.freelance.ir.weproject.poem.objects.Post;
import ir.weproject.freelance.ir.weproject.poem.objects.Skill;

/**
 * Created by Iman on 11/11/2015.
 */
public class PostsAdapter extends ArrayAdapter<Post> {

    public enum ACTIONS {LIKE_POST, BOOKMARK_POST, REPORT_POST, INSERT_COMMENT, RATE_POST};

    private final Context context;
    ArrayList<Post> posts;
//    RatingBar ratingBar;
    ACTIONS action;

    public PostsAdapter(Context context, List<Post> posts) {
        super(context, -1, posts);
        this.context = context;
        this.posts = (ArrayList<Post>) posts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Post post = getItem(position);

        if( post instanceof Project){
            return createPoemView((Project)post, position, parent);
        }else{
            return null;
        }
    }

    private View createPoemView(final Project project, final int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.project_post, parent, false);

        final HashMap<String, String> user;
        SQLiteHandler db = new SQLiteHandler(getContext());
        user = db.getUserDetails();

//        final LinearLayout postLayout = (LinearLayout) rowView.findViewById(R.id.project_layout);
        TextView title = (TextView) rowView.findViewById(R.id.poem_post_title);
//        TextView authorName = (TextView) rowView.findViewById(R.id.author_name);
        TextView date = (TextView) rowView.findViewById(R.id.posted_date);
        final TextView content = (TextView) rowView.findViewById(R.id.poem_post_content);
        TextView initialCost = (TextView) rowView.findViewById(R.id.initial_cost);
        TextView initialDuration = (TextView) rowView.findViewById(R.id.initial_duration);
//        RoundedNetworkImageView authorImage = (RoundedNetworkImageView) rowView.findViewById(R.id.author_image);
        ImageButton bookmarkButton = (ImageButton) rowView.findViewById(R.id.bookmarkButton);
        ImageButton reportButton = (ImageButton) rowView.findViewById(R.id.reportButton);
        ImageButton moreButton = (ImageButton) rowView.findViewById(R.id.moreButton);
        ImageButton backButton = (ImageButton) rowView.findViewById(R.id.backButton);
        Button bidButton = (Button) rowView.findViewById(R.id.bidButton);

        TextView neededSkills = (TextView) rowView.findViewById(R.id.neededSkills);
        if( project.getNeededSkills() != null && project.getNeededSkills().size() > 0 ) {
            neededSkills.setText(Skill.getNeededSkillsString(project.getNeededSkills()));
        }else{
            RelativeLayout skillsHeaderLayout = (RelativeLayout) rowView.findViewById(R.id.needed_skills_header_layout);
            skillsHeaderLayout.setVisibility(View.GONE);
        }

//        final EditText commentContent = (EditText) rowView.findViewById(R.id.comment_content);
//        ImageButton sendComment = (ImageButton) rowView.findViewById(R.id.send_commnet);
        ImageButton showCommwnts = (ImageButton) rowView.findViewById(R.id.preview_button);
//        TextView scoreText = (TextView) rowView.findViewById(R.id.post_score);

        final ViewFlipper viewFlipper = (ViewFlipper) rowView.findViewById(R.id.viewFlipper);
        View.OnClickListener viewFlipperListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
            }
        };

        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bidActivity = new Intent(getContext(), PutBidActivity.class);
                bidActivity.putExtra(AppConfig.EXTRA_KEY_PROJECT_OBJECT, project);
                getContext().startActivity(bidActivity);
            }
        });
        moreButton.setOnClickListener(viewFlipperListener);
        backButton.setOnClickListener(viewFlipperListener);

//        ratingBar = (RatingBar) rowView.findViewById(R.id.ratingBar);
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

        showCommwnts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getItem(position).getComments().size() == 0){
                    Toast.makeText(getContext(), "No comments", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent showComments = new Intent(context, ShowCommentsActivity.class);
                showComments.putExtra(AppConfig.EXTRA_KEY_USER_ID, (new SessionManager(context)).getCurrentUserID());
                showComments.putExtra(AppConfig.EXTRA_KEY_POST_ID, project.getPostId());
                context.startActivity(showComments);
            }
        });

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
                postAction(rowView, Integer.parseInt(user.get("uid")), project.getPostId(), (project.isReported() ? 0 : 1), ACTIONS.REPORT_POST, position, "");
                viewFlipper.showNext();
            }
        });

        return rowView;
    }


    private void postAction(final View rowView, final int userID, final int postID, final double rate, final ACTIONS action, final int position, final String commentContent) {

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
                    Toast.makeText(getContext(), "Action not successful", Toast.LENGTH_LONG).show();
                    return;
                }

                switch (action){
                    case LIKE_POST:

                        break;
                    case BOOKMARK_POST:
//                        bookmarkButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, rate == 1 ? R.drawable.ic_bookmark_black_24dp : R.drawable.ic_bookmark_border_black_24dp), null, null);

                        posts.get(position).setBookmarked(rate == 1 ? true : false);
                        break;
                    case REPORT_POST:
                        Toast.makeText(getContext(), "Reported", Toast.LENGTH_LONG).show();
                        break;
                    case RATE_POST:
                        //                ratingBar.setFocusable(true);
                        break;
                    case INSERT_COMMENT:
                        try {
                            JSONArray jsonComments = jObj.getJSONArray("comments");
                            posts.get(position).getComments().clear();
                            for(int i=0; i<jsonComments.length(); i++){
                                posts.get(position).getComments().add(new Comment(jsonComments.getJSONObject(i)));
                            }
                            ListView comments = ((ListView) rowView.findViewById(R.id.listView));
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
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                // Posting parameters to url
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userID));
                params.put("post_id", String.valueOf(postID));
                params.put("var", String.valueOf(rate));
                params.put("content", commentContent);
                params.put("action", action.name());
                return params;
            }
        };
        if( MainActivity.isNetworkAvailable(getContext())) {
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private boolean validateComment(String comment) {
        boolean retVal = true;
        if( comment == null || comment.equals("")){
            Toast.makeText(getContext(), "Empty field", Toast.LENGTH_LONG).show();
            retVal = false;
        }
        return retVal;
    }

    private void addNeededSkillsToLayout(LinearLayout ll, ArrayList<Skill> collection, String header) {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int maxWidth = width - 10;

        if (collection.size() > 0) {
            LinearLayout llAlso = new LinearLayout(context);
            llAlso.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            llAlso.setOrientation(LinearLayout.HORIZONTAL);

            TextView txtSample = new TextView(context);
            txtSample.setText("");
            txtSample.setText("");

            llAlso.addView(txtSample);
            txtSample.measure(0, 0);

            int widthSoFar = txtSample.getMeasuredWidth();

            /*android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:padding="5dp"
            android:layout_marginRight="10dp"
            android:layout_marginLeft="10dp"
            android:layout_marginBottom="2dp"
            android:layout_marginTop="2dp"
            android:background="@drawable/border_textview"
            android:text="Android"*/

            for (Skill samItem : collection) {

                float scale = context.getResources().getDisplayMetrics().density; //dp

                TextView txtSamItem = new TextView(context, null, android.R.attr.text);
                txtSamItem.setText(samItem.getName());
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                llp.setMargins(10, 10, 2, 2); // llp.setMargins(left, top, right, bottom);
                txtSamItem.setLayoutParams(llp);
                txtSamItem.setBackground(context.getResources().getDrawable(R.drawable.border_textview));
                txtSamItem.setPadding((int) (5 * scale + 0.5f),
                        (int) (5 * scale + 0.5f),
                        (int) (5 * scale + 0.5f),
                        (int) (5 * scale + 0.5f));
//                txtSamItem.setTag(samItem);

                txtSamItem.measure(0, 0);
                widthSoFar += txtSamItem.getMeasuredWidth();

                if (widthSoFar >= maxWidth) {
                    ll.addView(llAlso);

                    llAlso = new LinearLayout(context);
                    llAlso.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.FILL_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    llAlso.setOrientation(LinearLayout.HORIZONTAL);

                    llAlso.addView(txtSamItem);
                    widthSoFar = txtSamItem.getMeasuredWidth();
                } else {
                    llAlso.addView(txtSamItem);
                }
            }

            ll.addView(llAlso);
        }
    }
}
