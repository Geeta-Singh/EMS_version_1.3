package com.bydesign.ems1.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bydesign.ems1.R;
import com.bydesign.ems1.services.SessionManager;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {
   // static ProgressDialog pdailo=null;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.chartpie,
            R.drawable.chartbar,

    };
    public TabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    public void onStart() {
        super.onStart();
       // Session_Management();
        viewPager = (ViewPager) getView().findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) getView().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

      //  setupTabIcons();
    }
    public void Session_Management()
    {
        //Session Manager
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.cleardata();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
       // tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
    private void setupViewPager(final ViewPager viewPager) {

        final ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new CurrentFragment(), "Latest Data(Table)");
        adapter.addFragment(new GraphicalCurrentFragment(), " Latest Data(Graph)");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        ViewPager.OnPageChangeListener pagechangelistener = new ViewPager.OnPageChangeListener() {

       /*     public void onTabChanged(String tabId) {}*/
            @Override
            public void onPageSelected(int arg0) {

                viewPager.getAdapter().notifyDataSetChanged();

                viewPager.setCurrentItem(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

                Log.d("", "Called second");
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

                Log.d("Called third", "");

            }
        };
        viewPager.setOnPageChangeListener(pagechangelistener);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }


        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
    public void onPause(){
        super.onPause();
    }
    public void onResume(){
        super.onResume();
        setRetainInstance(true);
    }
}