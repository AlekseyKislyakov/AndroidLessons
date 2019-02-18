package kislyakov.a06_1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_basic extends Fragment {

    public static String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static String IMAGE_MESSAGE = "IMAGE_MESSAGE";

    public Fragment_basic() {
        // Required empty public constructor
    }

    public static final Fragment_basic newInstance(String message, int img)
    {
        Fragment_basic fragment = new Fragment_basic();
        Bundle bdl = new Bundle();
        bdl.putString(EXTRA_MESSAGE, message);
        bdl.putInt(IMAGE_MESSAGE,img);
        fragment.setArguments(bdl);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String message = getArguments().getString(EXTRA_MESSAGE);
        View v = inflater.inflate(R.layout.fragment_basic, container, false);

        TextView messageTextView = (TextView)v.findViewById(R.id.textView);
        messageTextView.setText(message);

        int img = getArguments().getInt(IMAGE_MESSAGE);
        ImageView image = (ImageView)v.findViewById(R.id.imageView);
        image.setImageResource(img);

        return v;
    }

}
