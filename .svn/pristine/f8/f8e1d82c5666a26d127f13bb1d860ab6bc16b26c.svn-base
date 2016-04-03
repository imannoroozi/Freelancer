package ir.weproject.freelance.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.weproject.freelance.freelance.AppConfig;
import ir.weproject.freelance.freelance.AppController;
import ir.weproject.freelance.freelance.MilestoneAdapter;
import ir.weproject.freelance.freelance.R;
import ir.weproject.freelance.helper.MilestoneParent;
import ir.weproject.freelance.helper.SQLiteHandler;
import ir.weproject.freelance.helper.SessionManager;
import ir.weproject.freelance.ir.weproject.poem.objects.Bid;
import ir.weproject.freelance.ir.weproject.poem.objects.Milestone;
import ir.weproject.freelance.ir.weproject.poem.objects.Project;


public class PutBidActivity extends Activity implements MilestoneParent{
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private Button btnLogin;
    private Button btnLinkToRegister;

    private EditText inputEmail;
    private EditText inputPassword;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    private  enum ACTIONS { POST_BID };

    ListView milestonesListView;
    MilestoneAdapter milestoneAdapter;

    Button putMyBid;
    Button addMilestone;

    Project project;

    EditText totalDuration, totalCost, description;

    ArrayList<Milestone> milestones;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_bid);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        project = (Project) getIntent().getSerializableExtra(AppConfig.EXTRA_KEY_PROJECT_OBJECT);

        addMilestone = (Button) findViewById(R.id.add_milestone_button);
        putMyBid = (Button) findViewById(R.id.put_my_bid);

        totalCost = (EditText) findViewById(R.id.total_cost);
        totalDuration = (EditText) findViewById(R.id.total_duration);
        description = (EditText) findViewById(R.id.bid_description);

        totalCost.setText(String.valueOf(project.getInitialCost()));
        totalDuration.setText(String.valueOf(project.getInitialDuration()));

        totalCost.addTextChangedListener( new TextWatcher() {
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
                totalCost.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        totalDuration.addTextChangedListener( new TextWatcher() {
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
                totalDuration.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        description.addTextChangedListener( new TextWatcher() {
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
                description.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        milestones = new ArrayList<>();
        //milestones.add(new Milestone(""));
        milestonesListView = (ListView) findViewById(R.id.milestones_list_view);
        milestoneAdapter = new MilestoneAdapter(this, milestones);
        milestonesListView.setAdapter(milestoneAdapter);

        addMilestone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !saveMilestones() ) return;
                milestones.add(new Milestone(""));
                milestoneAdapter.notifyDataSetChanged();
                milestonesListView.smoothScrollToPosition(milestones.size());
            }
        });

        putMyBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validate bid fields
                if( ! (checkForLength(totalCost, 0) && checkForNumeric(totalCost) &&
                    checkForLength(totalDuration, 0) && checkForNumeric(totalDuration) &&
                    checkForLength(description, 20)) ) return;

                //validate milestones fields
                View row;
                EditText title, cost;
                int costSummation = 0;
                for (int i = 0; i < milestonesListView.getCount(); i++) {
                    row = milestonesListView.getChildAt(i);
                    title = (EditText) row.findViewById(R.id.milestone_name);
                    cost = (EditText) row.findViewById(R.id.milestone_cost);
                    if( ! (checkForLength(title, 0) && checkForLength(cost, 0) && checkForNumeric(cost))) return;
                    costSummation += Integer.parseInt(cost.getText().toString());
                }

                //validate summations
                if( costSummation != Integer.parseInt(totalCost.getText().toString())){
                    Toast.makeText(PutBidActivity.this, "Costs should match", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bid bid = new Bid();
                bid.setDuration(Integer.parseInt(totalDuration.getText().toString().trim()));
                bid.setDescription(description.getText().toString().trim());
                bid.setCost(Double.parseDouble(totalCost.getText().toString().trim()));
                bid.setBidderID(session.getCurrentUserID());
                bid.setProjectID(project.getPostId());
                bid.setMilestones(milestones);

                //post request
                HashMap<String,String> data = new HashMap<String, String>();
                data.put("json_bid", String.valueOf(bid.toJSON()));
                postAction(ACTIONS.POST_BID, data);
            }
        });
    }

    private boolean saveMilestones() {
        View row;
        EditText title, cost;
        int costSummation = 0;
        for (int i = 0; i < milestonesListView.getCount(); i++) {
            row = milestonesListView.getChildAt(i);
            title = (EditText) row.findViewById(R.id.milestone_name);
            cost = (EditText) row.findViewById(R.id.milestone_cost);
            if( ! (checkForLength(title, 0) && checkForLength(cost, 0) && checkForNumeric(cost))) return false;

            milestones.get(i).setName(title.getText().toString());
            milestones.get(i).setAmount(Integer.parseInt(cost.getText().toString()));
        }
        return true;
    }


    @Override
    public void milestoneRemoved(Milestone m, int position) {
        milestones.remove(position);
        milestoneAdapter.notifyDataSetChanged();
    }

    private void postAction( final ACTIONS action, final HashMap<String, String> data) {

        String tag_string_req = "posts_req";
        String action_URL = AppConfig.URL_BIDDING;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                action_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                JSONObject jObj = null;
                boolean error = true;
                String msg = "";
                try {
                    jObj = new JSONObject(response);
                    error = jObj.getBoolean("error");
                    msg = jObj.getString("desc");
                } catch (JSONException e) {}

                if(error){
                    Toast.makeText(PutBidActivity.this, msg, Toast.LENGTH_LONG).show();
                    return;
                }

                switch (action){
                    case POST_BID:

                        break;
                    default:
                        break;
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //show the error
                Toast.makeText(PutBidActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                // Posting parameters to url
                Map<String, String> params = new HashMap<>();
                params = data;
                params.put("action", action.name());
                params.put("user_id", String.valueOf(session.getCurrentUserID()));
                return params;
            }
        };
        if( MainActivity.isNetworkAvailable(PutBidActivity.this)) {
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    public boolean checkForLength( EditText et, int len ){
        if( et.getText().toString().length() <= len){
            Toast.makeText(PutBidActivity.this, "Less than expected", Toast.LENGTH_SHORT).show();
            et.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
            return false;
        }
        return true;
    }

    public boolean checkForNumeric( EditText et ){
        String regexStr = "^[0-9]*$";
        if( !et.getText().toString().matches(regexStr)){
            Toast.makeText(PutBidActivity.this, "Should be numeric", Toast.LENGTH_SHORT).show();
            et.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
            return false;
        }
        return true;
    }
}