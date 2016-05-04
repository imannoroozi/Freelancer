package ir.weproject.freelance.freelance;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

public class ProjectFragmentAdapter extends FragmentPagerAdapter implements
        PagerSlidingTabStrip.IconTabProvider {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] { "Home", "Bookmarked", "My Bids", "My Projects" };
    private int[] imageResId = {
            R.drawable.ic_free_breakfast_black_24dp,
            R.drawable.ic_touch_app_black_24dp,
            R.drawable.ic_bookmark_black_24dp,
            R.drawable.ic_store_black_24dp
    };

    public ProjectFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return HomeFragmentFreelancer.newInstance(position + 1);
            case 1:
                return HomeFragmentFreelancer.newInstance(position + 1);
            case 2:
                return HomeFragmentFreelancer.newInstance(position + 1);
            case 3:
                return HomeFragmentFreelancer.newInstance(position + 1);
            default:
                return HomeFragmentFreelancer.newInstance(position + 1);
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return "";//tabTitles[position];
    }

    @Override
    public int getPageIconResId(int position) {
        return imageResId[position];
    }
}