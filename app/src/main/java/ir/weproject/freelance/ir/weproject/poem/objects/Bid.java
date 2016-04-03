package ir.weproject.freelance.ir.weproject.poem.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Iman on 3/31/2016.
 */
public class Bid {
    int bidID, projectID, duration, bidderID;
    double cost;
    String description, date;

    ArrayList<Milestone> milestones;

    public JSONObject toJSON(){
        JSONObject jo = new JSONObject();

        try{
            jo.put("project_id", getProjectID());
            jo.put("bidder_id", getBidderID());
            jo.put("duration", getDuration());
            jo.put("cost", getCost());
            jo.put("description", getDescription());
            jo.put("milestones_count", getMilestones().size());

            JSONArray milestonesArray = new JSONArray();
            for( Milestone m : this.getMilestones() ){
                milestonesArray.put( m.toJSON() );
            }
            jo.put("milestones", milestonesArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getBidderID() {
        return bidderID;
    }

    public void setBidderID(int bidderID) {
        this.bidderID = bidderID;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Milestone> getMilestones() {
        return milestones;
    }

    public void setMilestones(ArrayList<Milestone> milestones) {
        this.milestones = milestones;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getBidID() {
        return bidID;
    }

    public void setBidID(int bidID) {
        this.bidID = bidID;
    }
}
