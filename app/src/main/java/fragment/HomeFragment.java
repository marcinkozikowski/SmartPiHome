package fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.smartpihome.R;

import java.util.LinkedList;
import java.util.List;

import static com.example.dell.smartpihome.Main3Activity.tools;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    ImageView livingRoomLight;
    ImageView garageLight;
    ImageView corridorLight;
    ImageView kitchenLight;
    ImageView frontDoor;
    ImageView garageDoor;
    //CurrentDeviceState tool;

    private static final String ARG_PARAM1 = "temp";
    private static final String ARG_PARAM2 = "huminidity";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView tempTextView;
    TextView huminidityTextView;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
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
        try {
            tempTextView = (TextView)v.findViewById(R.id.TemperatureTextView);
            huminidityTextView = (TextView)v.findViewById(R.id.HumidityTextView);
            tempTextView.setText(mParam1 + " ºC");
            huminidityTextView.setText(mParam2 + " %");
            System.out.println(mParam1+"\t"+mParam2);
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
        return v;
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

    // TODO: Rename method, update argument and hook method into UI event
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
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
