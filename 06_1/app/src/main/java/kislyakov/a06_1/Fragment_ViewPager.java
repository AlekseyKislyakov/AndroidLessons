package kislyakov.a06_1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ViewPager extends Fragment {

    private int dotscount;
    private ImageView[] dots;
    LinearLayout sliderDotspanel;

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
        sliderDotspanel = (LinearLayout) v.findViewById(R.id.SliderDots);
        ViewPager pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setAdapter(pageAdapter);

        dotscount = pageAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                for (int j = 0; j < dotscount; j++) {
                    dots[j].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nonactive_dot));
                }

                dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        return v;
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(Fragment_basic.newInstance("Картинка 1", R.drawable.image1));
        fragmentList.add(Fragment_basic.newInstance("Картинка 2", R.drawable.image2));
        fragmentList.add(Fragment_basic.newInstance("Картинка 3", R.drawable.image3));

        return fragmentList;
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private int[] mResources;

        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

}
