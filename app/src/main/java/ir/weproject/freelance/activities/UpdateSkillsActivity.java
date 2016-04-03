package ir.weproject.freelance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.StringTokenizer;

import ir.weproject.freelance.freelance.AppConfig;
import ir.weproject.freelance.freelance.AppController;
import ir.weproject.freelance.freelance.MySkillsAdapter;
import ir.weproject.freelance.freelance.R;
import ir.weproject.freelance.freelance.SkillsAdapter;
import ir.weproject.freelance.helper.SQLiteHandler;
import ir.weproject.freelance.helper.SessionManager;
import ir.weproject.freelance.helper.SkillsParent;
import ir.weproject.freelance.ir.weproject.poem.objects.Skill;

public class UpdateSkillsActivity extends AppCompatActivity implements SkillsParent {

    enum ACTIONS { GET_ALL_SKILLS, GET_CATEGORIES, GET_SKILL_LIST, UPDATE_SKILLS};
    SessionManager session;
    SQLiteHandler db;

    HashMap<Integer, ArrayList<Skill>> skills; // <parentSkill, childSkills>
    HashMap<Integer, String> categories; //<id, name>

    ArrayList<Skill> mySkills, categoriesList, currentList;

    Button backToCategory, nextBtn, refreshBtn;
    TextView skipTxtView;

    ListView skillsListView, mySkillsListView;
    SkillsAdapter skillsAdapter;
    MySkillsAdapter mySkillsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_skills);

        session = new SessionManager(this);
        db = new SQLiteHandler(this);

        skillsListView = (ListView) findViewById(R.id.skillsListView);
        mySkillsListView = (ListView) findViewById(R.id.mySkillsListView);
        backToCategory = (Button) findViewById(R.id.back_to_category);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        skipTxtView = (TextView) findViewById(R.id.skipText);
        refreshBtn = (Button) findViewById(R.id.refreshBtn);


        mySkills = db.getMySkills();
        categoriesList = new ArrayList<>();

        HashMap<String, String> data = new HashMap();
        skills = new HashMap<>();
        categories = new HashMap<>();
        currentList = new ArrayList<>();
        data.put("user_id", String.valueOf(session.getCurrentUserID()));

        skillsAdapter = new SkillsAdapter(this, currentList);
        skillsListView.setAdapter(skillsAdapter);

        mySkillsAdapter = new MySkillsAdapter(this, mySkills);
        mySkillsListView.setAdapter(mySkillsAdapter);

        postAction(ACTIONS.GET_ALL_SKILLS, data);

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> data = new HashMap();
                data.put("user_id", String.valueOf(session.getCurrentUserID()));
                postAction(ACTIONS.GET_ALL_SKILLS, data);
            }
        });

        backToCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentList.clear();
                currentList.addAll(categoriesList);
                skillsAdapter.notifyDataSetChanged();
                backToCategory.setVisibility(View.INVISIBLE);
            }
        });
        
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMySkills();
            }
        });

        skipTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update registration level
                session.setRegistrationLevel(AppConfig.REGISTRATION_LEVEL.PROFILE_IMAGE);

//                Toast.makeText(UpdateSkillsActivity.this, "Skills have been updated.", Toast.LENGTH_SHORT).show();
                Intent profileImageIntent = new Intent(UpdateSkillsActivity.this, UploadProfileImageActivity.class);
                startActivity(profileImageIntent);
                finish();
            }
        });
    }

    private void updateMySkills() {

        //update Server
        HashMap<String, String> data = new HashMap<>();
        String skillStr = "";
        db.deleteSkills();
        for( Skill s : mySkills ){
            skillStr += s.getID() + ",";
            //update Local DB
            db.addSkill(s.getID(), s.getName(), s.getParentID(), s.isCategory() ? "Y" : "N");
        }
        data.put("skills_str", skillStr );
        data.put("user_id", String.valueOf(session.getCurrentUserID()));
//        data.put("action", ACTIONS.UPDATE_SKILLS.name());

        postAction(ACTIONS.UPDATE_SKILLS, data);

    }

    private void processResponse(String message) {
        try {
            JSONObject jo = new JSONObject(message);
            if (!jo.getBoolean("error")) {
                //update current user skills
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Error occurred.", Toast.LENGTH_LONG)
                        .show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void postAction(final ACTIONS action, final HashMap<String, String> data) {

        String tag_string_req = "posts_req";
        String action_URL = AppConfig.URL_SKILLS;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                action_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                JSONObject jObj = null;
                boolean error = true;
                String desc = "";
                try {
                    jObj = new JSONObject(response);
                    error = jObj.getBoolean("error");
                    if( error ){
                        desc = jObj.getString("desc");
                        Toast.makeText(UpdateSkillsActivity.this, desc, Toast.LENGTH_LONG).show();
                        skillsListView.setVisibility(View.GONE);
                        refreshBtn.setVisibility(View.VISIBLE);
                        return;
                    }

                    switch (action){
                        case GET_ALL_SKILLS:
                            JSONArray jsonPosts = jObj.getJSONArray("skills");
                            for(int i=0; i<jsonPosts.length(); i++){
                                Skill skill = new Skill(jsonPosts.getJSONObject(i));
                                if( skill.isCategory() ){
                                    categories.put(skill.getID(), skill.getName());
                                    categoriesList.add(skill);
                                }else{
                                    if( skills.get(skill.getParentID()) == null ){
                                        ArrayList<Skill> set = new ArrayList<>();
                                        set.add(skill);
                                        skills.put(skill.getParentID(), set);
                                    }else{
                                        skills.get(skill.getParentID()).add(skill);
                                    }
                                }
                            }
                            currentList.clear();
                            currentList.addAll(categoriesList);
                            drawLists();
                            break;

                        case UPDATE_SKILLS:
                            //update registration level
                            session.setRegistrationLevel(AppConfig.REGISTRATION_LEVEL.PROFILE_IMAGE);

                            Toast.makeText(UpdateSkillsActivity.this, "Skills have been updated.", Toast.LENGTH_SHORT).show();
                            Intent profileImageIntent = new Intent(UpdateSkillsActivity.this, UploadProfileImageActivity.class);
                            startActivity(profileImageIntent);
                            finish();
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //show the error
                Toast.makeText(UpdateSkillsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                skillsListView.setVisibility(View.GONE);
                refreshBtn.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params = data;
                data.put("action", action.name());
                return params;
            }
        };
        if( MainActivity.isNetworkAvailable(UpdateSkillsActivity.this)) {
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    public void drawLists(){
        refreshBtn.setVisibility(View.GONE);
        skillsListView.setVisibility(View.VISIBLE);
        skillsAdapter.notifyDataSetChanged();
    }

    public void skillClicked(Skill skill, int position) {
        if( skill.isCategory()){ // get into the list
            currentList.clear();
            currentList.addAll(skills.get(skill.getID()));
            skillsAdapter.notifyDataSetChanged();
            backToCategory.setVisibility(View.VISIBLE);
        }else if( !mySkills.contains(skill)) { //add it to my skills
            mySkills.add(skill);
            mySkillsAdapter.notifyDataSetChanged();
        }
    }


    public void removeMySkill(Skill skill) {
        mySkills.remove(skill);
        mySkillsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //set action bar
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}