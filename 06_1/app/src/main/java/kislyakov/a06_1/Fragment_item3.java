package kislyakov.a06_1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_item3 extends Fragment implements View.OnClickListener {

    private boolean bannerPressed = false;
    public Fragment_item3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_item3, container, false);

        Button b = (Button) v.findViewById(R.id.button_viewpager);
        b.setOnClickListener(this);
        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onClick(View v) {
        Fragment ViewPagerFragment = null;
        Button b = (Button) v.findViewById(R.id.button_viewpager);
        switch (v.getId()) {
            case R.id.button_viewpager:{

                if(!bannerPressed){
                    ViewPagerFragment = new Fragment_ViewPager();
                    getChildFragmentManager().beginTransaction().replace(R.id.viewpager_container,
                            ViewPagerFragment).commit();
                    bannerPressed = true;

                    b.setText("СВЕРНУТЬ БАННЕР");
                }
                else {
                    if(getChildFragmentManager().findFragmentById(R.id.viewpager_container) != null){
                        getChildFragmentManager().beginTransaction().
                                remove(getChildFragmentManager().findFragmentById(R.id.viewpager_container)).commit();
                        bannerPressed = false;

                        b.setText("ПОКАЗАТЬ БАННЕР");
                    }

                }


                break;
            }

        }

    }
}
