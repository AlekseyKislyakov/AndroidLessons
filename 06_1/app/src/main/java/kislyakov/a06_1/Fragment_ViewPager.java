package kislyakov.a06_1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ViewPager extends Fragment {


    public Fragment_ViewPager() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MyPageAdapter pageAdapter;
        List<Fragment> fragments = getFragments();

        View v = inflater.inflate(R.layout.fragment_view_pager, container, false);
        pageAdapter = new MyPageAdapter(getChildFragmentManager(), fragments);
        ViewPager pager = (ViewPager)v.findViewById(R.id.pager);
        pager.setAdapter(pageAdapter);

        return v;
    }

    private List<Fragment> getFragments(){
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(Fragment_basic.newInstance("Картинка 1", R.drawable.image1));
        fragmentList.add(Fragment_basic.newInstance("Картинка 2", R.drawable.image2));
        fragmentList.add(Fragment_basic.newInstance("Картинка 3", R.drawable.image3));

        return fragmentList;
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private int[] mResources;

        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments ) {
            super(fm);
            this.fragments = fragments;
        }
        @Override
        public Fragment getItem(int position)
        {
            return this.fragments.get(position);
        }

        @Override
        public int getCount()
        {
            return this.fragments.size();
        }
    }

}
