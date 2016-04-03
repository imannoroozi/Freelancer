package ir.weproject.freelance.ir.weproject.poem.objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Iman on 3/30/2016.
 */
public class Milestone {
    String name;
    int amount; //in Tomans
    int duration,  //in days
            id;

    public Milestone(JSONObject jo){

        try {
            this.setName(jo.getString("name"));
            this.setAmount(jo.getInt("amount"));
            this.setDuration(jo.getInt("duration"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSON(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("name", getName());
            jo.put("amount", getAmount());
            jo.put("duration", getDuration());
            jo.put("id", getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }
    public Milestone(String name) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
