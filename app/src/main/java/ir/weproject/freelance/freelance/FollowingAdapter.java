package ir.weproject.freelance.freelance;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.weproject.freelance.activities.ViewProfileActivity;
import ir.weproject.freelance.helper.Farsi;
import ir.weproject.freelance.helper.ImageHelper;
import ir.weproject.freelance.helper.SQLiteHandler;
import ir.weproject.freelance.helper.SessionManager;
import ir.weproject.freelance.ir.weproject.poem.objects.User;

/**
 * Created by Iman on 11/11/2015.
 */
public class FollowingAdapter extends ArrayAdapter<User> {

    public enum relationStatus {NO_RELATION, ACTIVE_RELATION, PENDING_RELATION, REQUESTED_RELATION};

    private final Context context;
    ArrayList<User> users;

    public FollowingAdapter(Context context, List<User> users) {
        super(context, -1, users);
        this.context = context;
        this.users = (ArrayList<User>) users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);

        return createFollowingView((User) user, position, parent);
    }

    private View createFollowingView(final User author, final int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.following_list_item, parent, false);


        final HashMap<String, String> user;
        SQLiteHandler db = new SQLiteHandler(getContext());
        user = db.getUserDetails();

        TextView authorName = (TextView) rowView.findViewById(R.id.member_name);
        TextView authorDesc = (TextView) rowView.findViewById(R.id.member_desc);
        Button followButton = (Button) rowView.findViewById(R.id.follow_button);
        NetworkImageView image = (NetworkImageView) rowView.findViewById(R.id.author_image);

        ImageHelper.loadNetworkImage(getContext(), image, author.getImageAddress());

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewProfileIntent = new Intent(context, ViewProfileActivity.class);
                viewProfileIntent.putExtra(AppConfig.EXTRA_KEY_USER_ID, String.valueOf((new SessionManager(context)).getCurrentUserID()));
                viewProfileIntent.putExtra(AppConfig.EXTRA_KEY_AUTHOR_OBJECT, author);
                context.startActivity(viewProfileIntent);
            }
        });


        /*followButton.setOnClickListener(new View.OnClickListener() {
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
        });*/

        return rowView;
    }

}
