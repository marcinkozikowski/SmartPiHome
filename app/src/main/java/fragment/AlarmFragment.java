package fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.example.dell.smartpihome.R;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.dell.smartpihome.Main3Activity.tools;

public class AlarmFragment extends Fragment {

    Timer timer;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AlarmFragment() {
        // Required empty public constructor
    }

    public static AlarmFragment newInstance(String param1, String param2) {
        AlarmFragment fragment = new AlarmFragment();
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
        View v = inflater.inflate(R.layout.fragment_alarm, container, false);
        stopOrStartAnimation();
        checkAlarmState(v);
        return v;
    }

    public void stopOrStartAnimation()
    {
        timer = new Timer();
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
                move();
            }
        };
        timer.scheduleAtFixedRate(t,1000,1000);
    }

    public void move(){
            Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ImageView animationImage = (ImageView) getActivity().getWindow().getDecorView().getRootView().findViewById(R.id.motionSensorImg);
                                Animation animation1 =
                                        AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.move);
                                animation1.setRepeatMode(Animation.REVERSE);
                                animation1.setRepeatCount(Animation.INFINITE);
                                if(tools.isMotionDetected()) {
                                    animationImage.startAnimation(animation1);
                                }
                                else
                                {
                                    if(animationImage!=null) {
                                        animationImage.clearAnimation();
                                    }
                                }
                            }
                        });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        };
        thread.start();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void checkAlarmState(View v)
    {
        ToggleButton alarm = (ToggleButton)v.findViewById(R.id.alarmBtn);
        if(tools.isAlarm())
        {
            alarm.setChecked(true);
        }
        else
        {
            alarm.setChecked(false);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        timer.cancel();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
