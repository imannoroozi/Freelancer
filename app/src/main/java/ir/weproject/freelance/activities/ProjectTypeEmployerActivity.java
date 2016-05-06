package ir.weproject.freelance.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;

import ir.weproject.freelance.freelance.DrawerItemCustomAdapter;
import ir.weproject.freelance.freelance.ProjectFragmentAdapter;
import ir.weproject.freelance.freelance.R;
import ir.weproject.freelance.helper.SessionManager;
import ir.weproject.freelance.ir.weproject.poem.objects.FollowingListActivity;
import ir.weproject.freelance.ir.weproject.poem.objects.ObjectDrawerItem;


public class ProjectTypeEmployerActivity extends AppCompatActivity {

    private String[] menuItemNames;
    private DrawerLayout mDrawerLayout;
    ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[6];
    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    SessionManager session;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_type_employer);

        session = new SessionManager(this);

        btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ProjectTypeEmployerActivity.this, ProjectDetailActivity.class);
                //MainActivityEmployer.this.
                startActivity(myIntent);
            }
        });
        //ommited
        /*
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ProjectFragmentAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(2);

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
        */

        menuItemNames = getResources().getStringArray(R.array.main_menu_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.right_drawer);

        //list of drawer icons
        drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_person_black_24dp, "Profile");
        drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_create_black_24dp, "Post");
        drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_group_black_24dp, "Following");
        drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_search_black_24dp, "Search");
//        drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_do_not_disturb_alt_black_24dp, "Help");
        drawerItem[4] = new ObjectDrawerItem(R.drawable.ic_power_settings_new_black_24dp, "Log out");
        drawerItem[5] = new ObjectDrawerItem(R.drawable.ic_help_outline_black_24dp, "About");

        // Set the adapter for the list view
        DrawerItemCustomAdapter drawerAdapter = new DrawerItemCustomAdapter(this, R.layout.drawer_list_item, drawerItem);
        mDrawerList.setAdapter(drawerAdapter);

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                null,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }


    /** Swaps fragments in the main content view */
    private void selectItem(int position) {

        mDrawerLayout.closeDrawer(mDrawerList);
            Fragment fragment = null;
            Intent intent = null;

        switch (position) {
                case 0: //Profile
                    intent = new Intent(ProjectTypeEmployerActivity.this,ProfileActivity.class);
                    startActivity(intent);
                    break;
                case 1: //Post
                    intent = new Intent(ProjectTypeEmployerActivity.this,PostProjectActivity.class);
                    startActivity(intent);
                    break;
                case 2: //Followings
                    intent = new Intent(ProjectTypeEmployerActivity.this,FollowingListActivity.class);
                    startActivity(intent);
                    break;
                case 3: //Search
                    break;
                case 4: // Log out
                    session.setLogin(false);
                    intent = new Intent(ProjectTypeEmployerActivity.this,LoginActivity.class);
                    startActivity(intent);
                    break;
                case 5: //About
                    break;
                default:
                    break;
            }

            if (fragment != null) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                getActionBar().setTitle(menuItemNames[position]);


            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //set action bar
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()){
            case R.id.action_more:
                if( mDrawerLayout.isDrawerOpen(mDrawerList)){
                    mDrawerLayout.closeDrawer(mDrawerList);
                }else{
                    mDrawerLayout.openDrawer(mDrawerList);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public static boolean isNetworkAvailable(final Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        if( mDrawerLayout.isDrawerOpen(mDrawerList)){
            mDrawerLayout.closeDrawer(mDrawerList);
        }else{
            super.onBackPressed();
        }
    }
}
