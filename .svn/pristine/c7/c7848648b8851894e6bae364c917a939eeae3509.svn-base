package ir.weproject.freelance.ir.weproject.poem.objects;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import ir.weproject.freelance.freelance.R;

/**
 * Created by Iman on 12/3/2015.
 */
public class Skill implements Serializable{
    int ID, parentID;
    String name,
        parentName;
    boolean category;

    public Skill(JSONObject jo){

        /*ID,
        name,
        parent_id,
        isCategory*/

        try {
            this.setID(jo.getInt("ID"));
            this.setName(jo.getString("name"));
            this.setCategory(jo.getString("isCategory").equalsIgnoreCase("Y") ? true : false);
            this.setParentID(jo.getInt("parent_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getNeededSkillsString( ArrayList<Skill> skills ){
        String retVal = "";

        try {
            retVal = skills.get(0).getName();

            for (int i = 1; i < skills.size(); i++) {
                retVal += " | " + skills.get(i).getName();

            }
        }catch (Exception e){
            //do nothing
        }
        return retVal;
    }

    public Skill( String category, String name ){
        this.name = name;
    }

    public Skill(){ }

    public Skill(String name) {
        this.setName(name);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public boolean isCategory() {
        return category;
    }

    public void setCategory(boolean category) {
        this.category = category;
    }
}
