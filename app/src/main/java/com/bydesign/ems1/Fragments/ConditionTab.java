package com.bydesign.ems1.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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
public class ConditionTab extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public ConditionTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_condition_tab, container, false);
    }
    public void onStart() {
        super.onStart();
        Session_Management();
        viewPager = (ViewPager) getView().findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) getView().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }
    public void Session_Management()
    {
        //Session Manager
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.cleardata();
    }
    private void setupViewPager(final ViewPager viewPager) {

        final ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new DeviceConditionFragment(), "Device Condition");
        adapter.addFragment(new DataConditionFragment(), " Data Condition");


        viewPager.setAdapter(adapter);


        ViewPager.OnPageChangeListener pagechangelistener = new ViewPager.OnPageChangeListener() {

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

      /*  @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }
*/

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
}
