package fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.example.dell.smartpihome.R;

import static com.example.dell.smartpihome.Main3Activity.tools;

public class BlindsFragment extends Fragment {

    ToggleButton kitchenBlind;
    ToggleButton livingRoomBlind;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BlindsFragment() {
        // Required empty public constructor
    }


    public static BlindsFragment newInstance(String param1, String param2) {
        BlindsFragment fragment = new BlindsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blinds, container, false);
        v = setCurrentBlindState(v);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public View setCurrentBlindState(View v)
    {
        kitchenBlind = (ToggleButton)v.findViewById(R.id.kitchenBlindBtn);
        livingRoomBlind = (ToggleButton) v.findViewById(R.id.livingRoomBlindBtn);

        kitchenBlind.setChecked(tools.isKitchenBlind());
        livingRoomBlind.setChecked(tools.isLivingRoomBlind());

        return v;
    }
}
