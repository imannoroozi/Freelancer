package ir.weproject.freelance.freelance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.weproject.freelance.activities.MainActivity;
import ir.weproject.freelance.activities.ViewProfileActivity;
import ir.weproject.freelance.helper.Farsi;
import ir.weproject.freelance.helper.ImageHelper;
import ir.weproject.freelance.helper.SessionManager;
import ir.weproject.freelance.ir.weproject.poem.objects.Comment;

/**
 * Created by Iman on 11/11/2015. pasokh be porsesham fohshe rakik bood
 */
public class CommentsAdapter extends ArrayAdapter<Comment> {
    private final Context context;
    private final int userID;
    ArrayList<Comment> comments;
    int postID;
    Typeface typeFace;

    public enum ACTIONS {VOTE_UP_COMMENT, VOTE_DOWN_COMMENT, DELETE_COMMENT, REPORT_COMMENT};


    public CommentsAdapter(Context context, List<Comment> comments, int userID, int itemID) {
        super(context, -1, comments);
        this.context = context;
        this.comments = (ArrayList<Comment>) comments;
        this.userID = userID;
        this.postID = itemID;
        typeFace = Typeface.createFromAsset(context.getAssets(), "Fonts/DroidNaskhRegular.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createCommentView(getItem(position), position, parent);
    }

    private View createCommentView(final Comment comment, final int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.poem_comment, parent, false);

        TextView authorName = (TextView) rowView.findViewById(R.id.comment_author_name);
        TextView date = (TextView) rowView.findViewById(R.id.comment_date);
        TextView content = (TextView) rowView.findViewById(R.id.comment_content);
//        TextView score = (TextView) rowView.findViewById(R.id.comment_score);
        Button reportButton = (Button) rowView.findViewById(R.id.reportButton);
        Button cancelButton = (Button) rowView.findViewById(R.id.moreButton);
        Button deleteButton = (Button) rowView.findViewById(R.id.deleteButton);
        NetworkImageView authorImage = (NetworkImageView) rowView.findViewById(R.id.comment_author_image);

        final ViewFlipper viewFlipper = (ViewFlipper) rowView.findViewById(R.id.viewFlipper);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.abc_popup_enter));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.abc_popup_exit));
        LinearLayout commentLayout = (LinearLayout) rowView.findViewById(R.id.comment_layout);

        date.setText(Farsi.Convert(comment.getDate()));
        content.setText(Farsi.Convert(comment.getContent()));
//        score.setText(Farsi.Convert(String.valueOf(comment.getRate())));
        ImageHelper.loadNetworkImage(getContext(), authorImage, comment.getUser().getImageAddress());

        View.OnClickListener flipperListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
            }
        };

        commentLayout.setOnClickListener(flipperListener);
        cancelButton.setOnClickListener(flipperListener);

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentAction(rowView, userID, comment.getID(), postID, 0, ACTIONS.REPORT_COMMENT, position);
                viewFlipper.showNext();
            }
        });

        View.OnClickListener onAuthor = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewProfileIntent = new Intent(context, ViewProfileActivity.class);
                viewProfileIntent.putExtra(AppConfig.EXTRA_KEY_USER_ID, String.valueOf((new SessionManager(context)).getCurrentUserID()));
                viewProfileIntent.putExtra(AppConfig.EXTRA_KEY_AUTHOR_OBJECT, comment.getUser());
                context.startActivity(viewProfileIntent);
            }
        };

        authorImage.setOnClickListener(onAuthor);
        //authorName.setOnClickListener(onAuthor);

        authorName.setTypeface(typeFace);
        date.setTypeface(typeFace);
        content.setTypeface(typeFace);
//        score.setTypeface(typeFace);

        //delete button
        if( userID != comment.getUser().getID()){
            deleteButton.setVisibility(View.GONE);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentAction(rowView, userID, comment.getID(), postID, 0, ACTIONS.DELETE_COMMENT, position);
            }
        });
        return rowView;
    }

    private void commentAction(final View rowView, final int userID, final int commentID, final int postID, final double var, final ACTIONS action, final int position) {

        String tag_string_req = "posts_req";
        String action_URL = AppConfig.URL_COMMENTS;
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
                    case VOTE_UP_COMMENT:
                        break;
                    case VOTE_DOWN_COMMENT:
                        break;
                    case REPORT_COMMENT:
                        Toast.makeText(getContext(), "Reported", Toast.LENGTH_LONG).show();
                        break;
                    case DELETE_COMMENT:
                        comments.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_LONG).show();
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
                params.put("comment_id", String.valueOf(commentID));
                params.put("var", String.valueOf(var));
                params.put("action", action.name());
                return params;
            }
        };
        if( MainActivity.isNetworkAvailable(getContext())) {
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

}
