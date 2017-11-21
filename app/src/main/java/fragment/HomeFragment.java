package fragment;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.dell.smartpihome.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.dell.smartpihome.Main3Activity.player;
import static com.example.dell.smartpihome.Main3Activity.tools;


public class HomeFragment extends Fragment {

    ImageView livingRoomLight;
    ImageView garageLight;
    ImageView corridorLight;
    ImageView kitchenLight;
    ImageView frontDoor;
    ImageView garageDoor;
    ImageView kitchenBlind;
    ImageView livingRoomBlind;
    TextView kitchenBlindPosition;
    TextView livingRoomBlindPosition;
    TextView nowPlaying;
    ImageView nextSong;
    ImageView previousSong;
    ImageView stopSong;
    ImageView playPouseSong;
    SeekBar seekBar;
    ImageView animationImage;
    Timer timer;
    static Animation animation1;

    private static final String ARG_PARAM1 = "temp";
    private static final String ARG_PARAM2 = "huminidity";


    private String mParam1;
    private String mParam2;
    TextView tempTextView;
    TextView huminidityTextView;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        v =checkLightState(v);
        v =checkDoorState(v);
        v = checkNowPlaying(v);
        v = checkBlindState(v);
        checkAlarmState(v);
        isMotionDetected();

        try {
            tempTextView = (TextView)v.findViewById(R.id.TemperatureTextView);
            huminidityTextView = (TextView)v.findViewById(R.id.HumidityTextView);
            nextSong = (ImageView)v.findViewById(R.id.NextImgHome);
            previousSong = (ImageView)v.findViewById(R.id.PreviousImgHome);
            stopSong=(ImageView)v.findViewById(R.id.StopImgHome);
            playPouseSong = (ImageView)v.findViewById(R.id.NowPlayingImgHome);
            seekBar = (SeekBar)v.findViewById(R.id.ProgresBar_SeekBar);

            tempTextView.setText(mParam1 + " ºC");
            huminidityTextView.setText(mParam2 + " %");
            System.out.println(mParam1+"\t"+mParam2);
            getSeekBarStatus();
            stopOrStartAnimation();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.nextSong();
                if(player.isPlaying())
                {
                    nowPlaying.setText(player.getCurrentSong().getArtist()+" "+player.getCurrentSong().getTitle());
                }
            }
        });
        previousSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.previousSong();
                if(player.isPlaying())
                {
                    nowPlaying.setText(player.getCurrentSong().getArtist()+" "+player.getCurrentSong().getTitle());
                }
            }
        });
        stopSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stopPlaying();
                if(player.isPlaying())
                {
                    nowPlaying.setText(player.getCurrentSong().getArtist()+" "+player.getCurrentSong().getTitle());
                }
            }
        });
        playPouseSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player.isPlaying())
                {
                    player.pouseSong();
                }
                else
                {
                    player.playSong();
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress=0;

            @Override
            public void onProgressChanged(final SeekBar seekBar, int ProgressValue, boolean fromUser) {
                if (fromUser) {
                    player.seekTo(ProgressValue);//if user drags the seekbar, it gets the position and updates in textView.
                }
                final long mMinutes=(ProgressValue/1000)/60;//converting into minutes
                final int mSeconds=((ProgressValue/1000)%60);//converting into seconds
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

    private void checkAlarmState(View v)
    {
        TextView alarm = (TextView)v.findViewById(R.id.AlarmActivTextView);
        if(tools.isAlarm())
        {
            alarm.setTextColor(getResources().getColor(R.color.AlarmEnabled));
            alarm.setTypeface(null, Typeface.BOLD);
            alarm.setText("Włączony");
        }
        else
        {
            alarm.setTextColor(getResources().getColor(R.color.AlarmDiseabled));
            alarm.setTypeface(null, Typeface.BOLD);
            alarm.setText("Wyłączony");
        }
    }

    public void stopOrStartAnimation()
    {
        timer = new Timer();
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
                isMotionDetected();
            }
        };
        timer.scheduleAtFixedRate(t,1000,1000);
    }
    public void isMotionDetected()
    {
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                animationImage = (ImageView) getActivity().getWindow().getDecorView().getRootView().findViewById(R.id.MotionImg);
                                animation1 =
                                        AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.blink);
                                animation1.setRepeatMode(Animation.RESTART);
                                animation1.setRepeatCount(Animation.INFINITE);
                                if (tools.isMotionDetected()) {
                                    animationImage.startAnimation(animation1);
                                } else {
                                    if(animationImage!=null) {
                                        animationImage.clearAnimation();
                                    }
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
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

    public void getSeekBarStatus(){

        new Thread(new Runnable() {

            @Override
            public void run() {
                int currentPosition = 0;
                int total = player.getDuration();
                seekBar.setMax(total);
                while (player != null && currentPosition < total) {
                    try {
                        Thread.sleep(1000);
                        currentPosition = player.currentPosition();
                    } catch (InterruptedException e) {
                        return;
                    }
                    seekBar.setProgress(currentPosition);

                }
            }
        }).start();
    }

    public View checkNowPlaying(View inflater)
    {
        nowPlaying = (TextView)inflater.findViewById(R.id.NowPlayingHome_TextView);

        if(player.isPlaying())
        {
            nowPlaying.setText(player.getCurrentSong().getArtist()+" "+player.getCurrentSong().getTitle());
        }

        return inflater;
    }

    public View checkLightState(View inflater)
    {
        livingRoomLight = (ImageView)inflater.findViewById(R.id.LivingRoomLightImg);
        kitchenLight = (ImageView)inflater.findViewById(R.id.KitchenLightImg);
        corridorLight = (ImageView)inflater.findViewById(R.id.CorridorLightImg);
        garageLight = (ImageView)inflater.findViewById(R.id.GarageLightImg);

        List<ImageView> lightsList = new LinkedList<ImageView>();
        List<Boolean> lightsState = new LinkedList<Boolean>();
        lightsState.add(tools.isLivingRoomLight());
        lightsState.add(tools.isKitchenLight());
        lightsState.add(tools.isCorridorLight());
        lightsState.add(tools.isGarageLight());
        lightsList.add(livingRoomLight);
        lightsList.add(kitchenLight);
        lightsList.add(corridorLight);
        lightsList.add(garageLight);

        for(int i=0;i<lightsState.size();i++)
        {
            if(lightsState.get(i))
            {
                lightsList.get(i).setImageResource(R.drawable.light_bulb_on);
            }
            else
            {
                lightsList.get(i).setImageResource(R.drawable.light_bulb_off);
            }
        }

        return inflater;
    }

    public View checkDoorState(View inflater)
    {
        frontDoor = (ImageView)inflater.findViewById(R.id.FrontDoorImg);
        garageDoor = (ImageView)inflater.findViewById(R.id.GarageDoorImg);

        if(tools.isFrontDoor())
        {
            frontDoor.setImageResource(R.drawable.icons8_door_closed_filled_100);
        }
        else{
            frontDoor.setImageResource(R.drawable.icons8_door_opened_filled_100);
        }

        if(tools.isGarageDoor())
        {
            garageDoor.setImageResource(R.drawable.icons8_gate_filled_100);
        }
        else{
            garageDoor.setImageResource(R.drawable.icons8_front_gate_open_filled_100);
        }
        return inflater;
    }

    public View checkBlindState(View inflater)
    {
        kitchenBlind = (ImageView)inflater.findViewById(R.id.KitchenBlindsImg);
        livingRoomBlind = (ImageView)inflater.findViewById(R.id.LivingRoomBlindsImg);

        kitchenBlindPosition =(TextView)inflater.findViewById(R.id.KitchenBlindPosition) ;
        livingRoomBlindPosition =(TextView)inflater.findViewById(R.id.LivingRoomBlindPosition);

        if(tools.isKitchenBlind())
        {
            kitchenBlind.setImageResource(R.drawable.blinds_close);
        }
        else{
            kitchenBlind.setImageResource(R.drawable.blinds_open);
        }
        kitchenBlindPosition.setText(tools.getKitchenBlindPosition()+"/4");
        livingRoomBlindPosition.setText(tools.getLivingRoomBlindPosition()+"/4");

        if(tools.isLivingRoomBlind())
        {
            livingRoomBlind.setImageResource(R.drawable.blinds_close);
        }
        else{
            livingRoomBlind.setImageResource(R.drawable.blinds_open);
        }
        return inflater;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        timer.cancel();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
