package ir.weproject.freelance.freelance;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.weproject.freelance.activities.UpdateSkillsActivity;
import ir.weproject.freelance.ir.weproject.poem.objects.Skill;

/**
 * Created by Iman on 11/11/2015. pasokh be porsesham fohshe rakik bood
 */
public class SkillsAdapter extends ArrayAdapter<Skill> {
    private final Context context;
    ArrayList<Skill> skills;
    UpdateSkillsActivity activity;
    int postID;
    Typeface typeFace;

    public enum ACTIONS {VOTE_UP_COMMENT, VOTE_DOWN_COMMENT, DELETE_COMMENT, REPORT_COMMENT};


    public SkillsAdapter(Context context, List<Skill> skills) {
        super(context, -1, skills);
        this.context = context;
        this.skills = (ArrayList<Skill>) skills;
        typeFace = Typeface.createFromAsset(context.getAssets(), "Fonts/DroidNaskhRegular.ttf");
        activity = (UpdateSkillsActivity) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createSkillView(getItem(position), position, parent);
    }

    private View createSkillView(final Skill skill, final int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.skill_item_layout, parent, false);

        TextView skillText = (TextView) rowView.findViewById(R.id.milestone_name);
        skillText.setText(skill.getName());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.skillClicked(skill, position);
            }
        });

        return rowView;
    }

}
