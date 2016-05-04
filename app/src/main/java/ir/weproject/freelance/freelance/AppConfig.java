package ir.weproject.freelance.freelance;

/**
 * Created by Iman on 10/22/2015.
 */
public class AppConfig {


    //Server IP
    public static String SERVER_IP = "http://www.weproject.ir/webservice/";
    public static String URL_READ_HOME_POSTS = SERVER_IP + "read_home_posts.php";
    public static String URL_READ_PROJECTS = SERVER_IP + "readProjects.php";
    public static String URL_REGISTER = SERVER_IP + "register.php";
    public static String URL_READ_RELATION = SERVER_IP + "followings.php";
    public static String URL_LOGIN = SERVER_IP + "login.php";
    public static final String URL_RATE_POSTS = SERVER_IP + "ratings.php";
    public static final String URL_LIKE_POSTS = SERVER_IP + "likes.php";
    public static final String URL_BOOKMARK_POSTS = SERVER_IP + "bookmarks.php";
    public static final String URL_REPORT_POSTS = SERVER_IP + "reports.php";
    public static final String URL_UPDATE_PROFILE = SERVER_IP + "profileUpdate.php";
    public static final String URL_POST_POEM = SERVER_IP + "postPoem.php";
    public static final String URL_READ_FOLLOWINGS = SERVER_IP + "followings.php";
    public static final String URL_SKILLS = SERVER_IP + "skills.php";
    public static final String URL_BIDDING = SERVER_IP + "bidding.php";

    //comments
    public static String URL_COMMENTS = SERVER_IP + "comments.php";

    // File upload url (replace the ip with your server address)
    public static final String FILE_UPLOAD_URL = SERVER_IP + "profileImageUpload.php";

    //Registration level
    public static enum REGISTRATION_LEVEL { WELCOME, SKILL_INFOR, PROFILE_IMAGE, SELECT_ROLE, PERSONAL_INFORMATION, COMPLETE}

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";

    // Intent extras
    public static final String EXTRA_KEY_USER_ID = "CURRENT_USER_ID";
    public static final String EXTRA_KEY_AUTHOR_OBJECT = "AUTHOR_OBJECT";
    public static final String EXTRA_KEY_POST_ID = "POST_D";
    public static final String EXTRA_KEY_USER_NAME =  "USER_NAME";
    public static final String EXTRA_KEY_USER_PASSWORD_OLD = "OLD_PASSWORD";
    public static final String EXTRA_KEY_USER_PASSWORD = "PASSWORD";
    public static final String EXTRA_KEY_IMAGE_OBJECT = "IMAGE_OBJECT";
    public static final String EXTRA_KEY_PROJECT_ID = "PROJECT_ID";
    public static final String EXTRA_KEY_PROJECT_OBJECT = "PROJECT_OBJECT";

    public static final String EXTRA_KEY_EMPLOYER_ROLE = "EPLOYER_ROLE";
    public static final String EXTRA_KEY_FREELANCER_ROLE = "FREELANCER_ROLE";
}
