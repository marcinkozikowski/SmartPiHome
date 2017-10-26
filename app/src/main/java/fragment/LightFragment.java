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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LightFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LightFragment extends Fragment {
    //CurrentDeviceState tools;
    ToggleButton livingRoomLight;
    ToggleButton garageLight;
    ToggleButton corridorLight;
    ToggleButton kitchenLight;
    ToggleButton fastLight;

    private OnFragmentInteractionListener mListener;

    public LightFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LightFragment newInstance(String param1, String param2) {
        LightFragment fragment = new LightFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //tools.setLivingRoomLight(true);
        //livingRoomLight.setChecked(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = null;

        inflatedView = inflater.inflate(R.layout.fragment_light, container, false);
        //setCurrentLightState(inflatedView);
        setCurrentLightState(inflatedView);
//        livingRoomLight = (ToggleButton) inflatedView.findViewById(R.id.lightBtn);
//        livingRoomLight.setChecked(tools.isLivingRoomLight());
//        garageLight = (ToggleButton) inflatedView.findViewById(R.id.GarageLightBtn);
//        corridorLight = (ToggleButton) inflatedView.findViewById(R.id.CorridorLightBtn);
//        kitchenLight = (ToggleButton) inflatedView.findViewById(R.id.kitchenLightBtn);
//        fastLight = (ToggleButton) inflatedView.findViewById(R.id.fastLightBtn);
//
//        livingRoomLight.setChecked(tools.isLivingRoomLight());
//        garageLight.setChecked(tools.isGarageLight());
//        corridorLight.setChecked(tools.isCorridorLight());
//        kitchenLight.setChecked(tools.isKitchenLight());
//        fastLight.setChecked(tools.isFastLight());

        return inflatedView;
    }

    public View setCurrentLightState(View inflater)
    {
        livingRoomLight = (ToggleButton) inflater.findViewById(R.id.lightBtn);
        garageLight = (ToggleButton) inflater.findViewById(R.id.GarageLightBtn);
        corridorLight = (ToggleButton) inflater.findViewById(R.id.CorridorLightBtn);
        kitchenLight = (ToggleButton) inflater.findViewById(R.id.kitchenLightBtn);
        fastLight = (ToggleButton) inflater.findViewById(R.id.fastLightBtn);

        livingRoomLight.setChecked(tools.isLivingRoomLight());
        garageLight.setChecked(tools.isGarageLight());
        corridorLight.setChecked(tools.isCorridorLight());
        kitchenLight.setChecked(tools.isKitchenLight());
        fastLight.setChecked(tools.isFastLight());

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
