package ir.weproject.freelance.ir.weproject.poem.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Iman on 11/11/2015.
 */
public class Project extends Post implements Serializable{

    ArrayList<String> tags;
    ArrayList<Skill> neededSkills;
    int initialCost;
    int initialDuration; // if hourly it is in hours otherwise it is in days
    boolean hourly;

    public Project(JSONObject jo){
        this.comments = new ArrayList<Comment>();
        this.neededSkills = new ArrayList<>();

        try {
            this.setPostId(jo.getInt("ID"));
            this.setDate(jo.getString("post_date"));
            this.setDescription(jo.getString("post_content"));
            this.setTitle(jo.getString("post_title"));
            this.setPostType(jo.getString("post_type"));
            this.setInitialCost(jo.getInt("initial_cost"));
            this.setInitialDuration(jo.getInt("initial_duration"));

            this.setUser(new User(jo.getJSONObject("user")));

            JSONArray commentsArray = jo.getJSONArray("comments");

            for( int i=0; i<commentsArray.length(); i++ ){
                this.comments.add(new Comment(commentsArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tags = new ArrayList<String>();
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<Skill> getNeededSkills() {
//        return neededSkills;
        ArrayList<Skill> ret = new ArrayList<>();
        ret.add(new Skill("Android"));
        ret.add(new Skill("JQuery"));
        return ret;
    }
    public void setNeededSkills(ArrayList<Skill> neededSkills) {
        this.neededSkills = neededSkills;
    }

    public int getInitialCost() {
        return initialCost;
    }

    public void setInitialCost(int initialCost) {
        this.initialCost = initialCost;
    }

    public int getInitialDuration() {
        return initialDuration;
    }

    public void setInitialDuration(int initialDuration) {
        this.initialDuration = initialDuration;
    }

    public boolean isHourly() {
        return hourly;
    }

    public void setHourly(boolean hourly) {
        this.hourly = hourly;
    }
}
