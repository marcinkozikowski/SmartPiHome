package fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.example.dell.smartpihome.R;

import static com.example.dell.smartpihome.Main3Activity.tools;

public class BlindsFragment extends Fragment {

    ToggleButton kitchenBlind;
    ToggleButton livingRoomBlind;
    SeekBar kitchenBlindSeekBar;
    SeekBar livingRoomBlindSeekBar;

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
        kitchenBlindSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, int ProgressValue, boolean fromUser) {
                if (fromUser) {
                    tools.setKitchenBlindPosition(ProgressValue);
                    if(kitchenBlindSeekBar.getProgress()==kitchenBlindSeekBar.getMax())
                    {
                        tools.setKitchenBlind(true);
                        kitchenBlind.setChecked(true);
                    }
                    else if(kitchenBlindSeekBar.getProgress()==0)
                    {
                        tools.setKitchenBlind(false);
                        kitchenBlind.setChecked(false);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        livingRoomBlindSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, int ProgressValue, boolean fromUser) {
                if (fromUser) {
                    tools.setLivingRoomBlindPosition(ProgressValue);
                    if(livingRoomBlindSeekBar.getProgress()==livingRoomBlindSeekBar.getMax())
                    {
                        tools.setLivingRoomBlind(true);
                        livingRoomBlind.setChecked(true);
                    }
                    else if(livingRoomBlindSeekBar.getProgress()==0)
                    {
                        tools.setLivingRoomBlind(false);
                        livingRoomBlind.setChecked(false);
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return v;
    }

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
        void onFragmentInteraction(Uri uri);
    }

    public View setCurrentBlindState(View v)
    {
        kitchenBlind = (ToggleButton)v.findViewById(R.id.kitchenBlindBtn);
        livingRoomBlind = (ToggleButton) v.findViewById(R.id.livingRoomBlindBtn);
        kitchenBlindSeekBar = (SeekBar)v.findViewById(R.id.kitchenblind1SeekBar);
        livingRoomBlindSeekBar = (SeekBar)v.findViewById(R.id.blind1SeekBar);

        kitchenBlind.setChecked(tools.isKitchenBlind());
        livingRoomBlind.setChecked(tools.isLivingRoomBlind());
        kitchenBlindSeekBar.setProgress(tools.getKitchenBlindPosition());
        livingRoomBlindSeekBar.setProgress(tools.getLivingRoomBlindPosition());

        return v;
    }
}
