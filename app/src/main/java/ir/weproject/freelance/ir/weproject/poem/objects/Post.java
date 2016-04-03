package ir.weproject.freelance.ir.weproject.poem.objects;

import java.util.ArrayList;

/**
 * Created by Iman on 11/11/2015.
 */
public abstract class Post {

    String  title = null;
    String description = null;
    String date = null;
    String postType = null;
    User user = null;

    boolean bookmarked, reported;

    ArrayList<Comment> comments;

    int postId = 0,
        dateTime = 0;

    public User getUser() { return this.user; }

    public void setUser(User user){ this.user = user; }

    public int getDateTime(){ return this.dateTime; }

    public void setDateTime(int dateTime){ this.dateTime = dateTime; }

    public int getPostId(){ return postId; }

    public void setPostId(int postId){ this.postId = postId; }

    public String getTitle(){ return  this.title; }

    public void setTitle(String title){ this.title = title; }

    public String getDescription(){ return this.description; }

    public void setDescription(String description){ this.description = description; }

    public String getDate(){ return this.date; }

    public void setDate(String date){ this.date = date; }

    public String getPostType(){ return  postType; }

    public void setPostType(String postType){ this.postType = postType; }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
    }
}
