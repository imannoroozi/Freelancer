package ir.weproject.freelance.ir.weproject.poem.objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Iman on 12/3/2015.
 */
public class Comment {
    int ID;
    User user;
    String content = null;
    String date = null;
    String authorImageUrl = null;
    double rate;

    public Comment(JSONObject jo) {

        /*
        'ID'
        'description'
        'date'
        'rate'
        'user'
         */

        try {
            this.setID(jo.getInt("ID"));
            this.setContent(jo.getString("description"));
            this.setDate(jo.getString("date"));
            this.setRate(jo.getDouble("rate"));
            this.setUser(new User(jo.getJSONObject("user")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthorImageUrl() {
        return authorImageUrl;
    }

    public void setAuthorImageUrl(String authorImageUrl) {
        this.authorImageUrl = authorImageUrl;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
