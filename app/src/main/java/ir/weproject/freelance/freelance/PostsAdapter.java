package ir.weproject.freelance.freelance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ir.weproject.freelance.activities.SingleProjectActivity;
import ir.weproject.freelance.helper.Farsi;
import ir.weproject.freelance.helper.ImageHelper;
import ir.weproject.freelance.helper.RoundedNetworkImageView;
import ir.weproject.freelance.ir.weproject.poem.objects.Post;
import ir.weproject.freelance.ir.weproject.poem.objects.Project;
import ir.weproject.freelance.ir.weproject.poem.objects.Skill;

/**
 * Created by Iman on 11/11/2015. there is no interaction with server from this adapter
 */
public class PostsAdapter extends ArrayAdapter<Post> {

    private final Context context;
    ArrayList<Post> posts;

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

        TextView title = (TextView) rowView.findViewById(R.id.poem_post_title);
        TextView date = (TextView) rowView.findViewById(R.id.posted_date);
        //final TextView content = (TextView) rowView.findViewById(R.id.poem_post_content);
        TextView initialCost = (TextView) rowView.findViewById(R.id.initial_cost);
        TextView initialDuration = (TextView) rowView.findViewById(R.id.initial_duration);
        TextView avgBids = (TextView) rowView.findViewById(R.id.avg_bids);
        TextView countBids = (TextView) rowView.findViewById(R.id.count_bids);
        RoundedNetworkImageView employerImage = (RoundedNetworkImageView) rowView.findViewById(R.id.employer_image);

        TextView neededSkills = (TextView) rowView.findViewById(R.id.neededSkills);
        if( project.getNeededSkills() != null && project.getNeededSkills().size() > 0 ) {
            neededSkills.setText(Skill.getNeededSkillsString(project.getNeededSkills()));
        }else{
            RelativeLayout skillsHeaderLayout = (RelativeLayout) rowView.findViewById(R.id.needed_skills_header_layout);
            skillsHeaderLayout.setVisibility(View.GONE);
        }

        DecimalFormat formatter = new DecimalFormat("#,###");

        title.setText(Farsi.Convert(project.getTitle()));
        date.setText(Farsi.Convert(project.getDate()));
//        content.setText(project.getDescription());
        initialCost.setText(String.valueOf(formatter.format(project.getInitialCost())));
        initialDuration.setText(String.valueOf(project.getInitialDuration()));
        avgBids.setText(String.valueOf(project.getBidsAvg()));
        countBids.setText(String.valueOf(project.getBidsCount()));
        ImageHelper.loadNetworkImage(context, employerImage, project.getUser().getImageAddress());

        Typeface tfRegular = Typeface.createFromAsset(context.getAssets(), "Fonts/droidnaskh_regular.ttf");
        Typeface tfBold = Typeface.createFromAsset(context.getAssets(), "Fonts/droidnaskh_bold.ttf");
        date.setTypeface(tfRegular);
//        content.setTypeface(tfRegular);
        initialCost.setTypeface(tfBold);
        initialDuration.setTypeface(tfBold);
        avgBids.setTypeface(tfBold);
        countBids.setTypeface(tfBold);


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent singlePost = new Intent(getContext(), SingleProjectActivity.class);
                singlePost.putExtra(AppConfig.EXTRA_KEY_PROJECT_ID, project.getPostId());
                getContext().startActivity(singlePost);
            }
        });
        return rowView;
    }

}
