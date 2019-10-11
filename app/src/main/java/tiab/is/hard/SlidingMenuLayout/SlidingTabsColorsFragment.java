package tiab.is.hard.SlidingMenuLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import tiab.is.hard.R;


public class SlidingTabsColorsFragment extends Fragment {

    static class SamplePagerItem {
        private final CharSequence mTitle;
        private final int mIndicatorColor;
        private final int mDividerColor;

        SamplePagerItem(CharSequence title, int indicatorColor, int dividerColor) {
            mTitle = title;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
        }

        Fragment createFragment() {
            return ContentFragment.newInstance(mTitle, mIndicatorColor, mDividerColor);
        }

        CharSequence getTitle() {
            return mTitle;
        }

        int getIndicatorColor() {
            return mIndicatorColor;
        }

        int getDividerColor() {
            return mDividerColor;
        }
    }

    private SlidingTabLayout mSlidingTabLayout;
    public static ViewPager mViewPager;
    private List<SamplePagerItem> mTabs = new ArrayList<SamplePagerItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTabs.add(new SamplePagerItem(
                getString(R.string.vegetables), // Title
                Color.GREEN, // Indicator color
                Color.GRAY // Divider color
        ));

        mTabs.add(new SamplePagerItem(
                getString(R.string.dairy), // Title
                Color.WHITE, // Indicator color
                Color.GRAY // Divider color
        ));

        mTabs.add(new SamplePagerItem(
                getString(R.string.spices), // Title
                Color.BLACK, // Indicator color
                Color.GRAY // Divider color
        ));

        mTabs.add(new SamplePagerItem(
                getString(R.string.fruits), // Title
                Color.GREEN, // Indicator color
                Color.GRAY // Divider color
        ));

        mTabs.add(new SamplePagerItem(
                getString(R.string.canned), // Title
                Color.RED, // Indicator color
                Color.GRAY // Divider color
        ));

        mTabs.add(new SamplePagerItem(
                getString(R.string.miscellaneous), // Title
                Color.MAGENTA, // Indicator color
                Color.GRAY // Divider color
        ));
        // TODO: Ingredient Types - Page Headings
        // END_INCLUDE (populate_tabs)
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.slidingtabscolorsfragement_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mViewPager = view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SampleFragmentPagerAdapter(getChildFragmentManager()));
        mSlidingTabLayout = view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return mTabs.get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return mTabs.get(position).getDividerColor();
            }

        });
    }


    class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

        SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mTabs.get(i).createFragment();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position).getTitle();
        }
    }


}