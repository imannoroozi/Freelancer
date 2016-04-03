package ir.weproject.freelance.freelance;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.weproject.freelance.helper.MilestoneParent;
import ir.weproject.freelance.ir.weproject.poem.objects.Milestone;

/**
 * Created by Iman on 11/11/2015. pasokh be porsesham fohshe rakik bood
 */
public class MilestoneAdapter extends ArrayAdapter<Milestone> {
    private final Context context;
    ArrayList<Milestone> milestones;
    Typeface typeFace;
    MilestoneParent parentActivity;

    public MilestoneAdapter(Context context, List<Milestone> milestones) {
        super(context, -1, milestones);
        this.context = context;
        this.milestones = (ArrayList<Milestone>) milestones;
        typeFace = Typeface.createFromAsset(context.getAssets(), "Fonts/DroidNaskhRegular.ttf");
        parentActivity = (MilestoneParent) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createMilestoneView(getItem(position), position, parent);
    }

    private View createMilestoneView(final Milestone milestone, final int position, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.milestone_item_layout, parent, false);

        final EditText milestoneName = (EditText) rowView.findViewById(R.id.milestone_name);
        final EditText milestoneCost = (EditText) rowView.findViewById(R.id.milestone_cost);
        ImageButton removeButton = (ImageButton) rowView.findViewById(R.id.remove_milestone_button);

        milestoneName.setText(milestone.getName());
        milestoneCost.setText(String.valueOf(milestone.getAmount()));

        milestoneName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
                milestoneName.setBackgroundColor(getContext().getResources().getColor(R.color.white));
            }
        });

        milestoneCost.addTextChangedListener( new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
                milestoneCost.setBackgroundColor(getContext().getResources().getColor(R.color.white));
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.milestoneRemoved(milestone, position);
            }
        });

        return rowView;
    }

}
