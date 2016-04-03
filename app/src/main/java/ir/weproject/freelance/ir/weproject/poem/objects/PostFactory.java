package ir.weproject.freelance.ir.weproject.poem.objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Iman on 12/3/2015.
 */
public class PostFactory {

    public Post getPost(JSONObject jo){
        String postType = null;
        try {
            postType = jo.getString("post_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(postType == null){
            return null;
        }

        if(postType.equalsIgnoreCase("PROJECT")){
            return new Project(jo);

        } else if(postType.equalsIgnoreCase("ADVERTISEMENT")){
            return null;

        }

        return null;
    }

}
