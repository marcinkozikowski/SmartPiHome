package fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ToggleButton;

import com.example.dell.smartpihome.Main3Activity;
import com.example.dell.smartpihome.R;

import java.util.List;

import CustomListAdapters.LightSceneAdapter;
import Entities.LightScene;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.example.dell.smartpihome.Main3Activity.tools;


public class LightFragment extends Fragment {
    //CurrentDeviceState tools;
    ToggleButton livingRoomLight;
    ToggleButton garageLight;
    ToggleButton corridorLight;
    ToggleButton kitchenLight;
    ToggleButton fastLight;
    ImageView addScene;
    ListView scenesListView;
    LightSceneAdapter lightSceneAdapter;
    List<LightScene> lc;
    LightScene choosenLightScene;
    LinearLayout addNewScene;
    PopupWindow addScenePopUp;
    Context mContext;

    private OnFragmentInteractionListener mListener;

    public LightFragment() {
        // Required empty public constructor
    }

    public static LightFragment newInstance(String param1, String param2) {
        LightFragment fragment = new LightFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lc = LightScene.listAll(LightScene.class);
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = null;

        inflatedView = inflater.inflate(R.layout.fragment_light, container, false);
        addScene = (ImageView)inflatedView.findViewById(R.id.addScene_ImageView);
        addNewScene = (LinearLayout)inflatedView.findViewById(R.id.addNewScene);

        setCurrentLightState(inflatedView);

        updateSceneList(inflatedView);

        scenesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                choosenLightScene = lc.get(position);
                try {
                    Main3Activity activity = (Main3Activity) getActivity();
                    activity.LightSceneClick(choosenLightScene);

                    if (choosenLightScene.isLivingRoom()) {
                        tools.setLivingRoomLight(true);
                        livingRoomLight.setChecked(tools.isLivingRoomLight());
                    } else {
                        tools.setLivingRoomLight(false);
                        livingRoomLight.setChecked(tools.isLivingRoomLight());
                    }
                    if (choosenLightScene.isKitchen()) {
                        tools.setKitchenLight(true);
                        kitchenLight.setChecked(tools.isKitchenLight());
                    } else {
                        tools.setKitchenLight(false);
                        kitchenLight.setChecked(tools.isKitchenLight());
                    }
                    if (choosenLightScene.isGarage()) {
                        tools.setGarageLight(true);
                        garageLight.setChecked(tools.isGarageLight());
                    } else {
                        tools.setGarageLight(false);
                        garageLight.setChecked(tools.isGarageLight());
                    }
                    if (choosenLightScene.isCorridor()) {
                        tools.setCorridorLight(true);
                        corridorLight.setChecked(tools.isCorridorLight());
                    } else {
                        tools.setCorridorLight(false);
                        corridorLight.setChecked(tools.isCorridorLight());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        View finalInflatedView = inflatedView;
        addNewScene.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.light_scene_popup_window,null);

                // Initialize a new instance of popup window
                addScenePopUp = new PopupWindow(
                        customView,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                addScenePopUp.setFocusable(true);

                if(Build.VERSION.SDK_INT>=21){
                    addScenePopUp.setElevation(5.0f);
                }

                Button closeButton = (Button) customView.findViewById(R.id.abortScene);
                Button saveButton = (Button) customView.findViewById(R.id.saveScene);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        SaveNewLightScene(customView);
                        updateSceneList();
                        addScenePopUp.dismiss();
                    }
                });

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        addScenePopUp.dismiss();
                    }
                });

                addScenePopUp.showAtLocation(addNewScene, Gravity.CENTER,0,0);
            }
        });

        return inflatedView;
    }

    private void SaveNewLightScene(View v)
    {
        EditText name = (EditText)v.findViewById(R.id.nameLightSceneET);
        CheckBox livingRoom = (CheckBox)v.findViewById(R.id.livingRoomCheckBox);
        CheckBox kitchen = (CheckBox)v.findViewById(R.id.kitchenCheckBox);
        CheckBox corridor = (CheckBox)v.findViewById(R.id.corridorCheckBox);
        CheckBox garage  = (CheckBox)v.findViewById(R.id.garageCheckBox);

        LightScene nl = new LightScene(name.getText().toString(),livingRoom.isChecked(),kitchen.isChecked(),corridor.isChecked(),garage.isChecked());
        nl.save();
    }

    private void updateSceneList(View v)
    {
        lc = LightScene.listAll(LightScene.class);
        scenesListView = (ListView)v.findViewById(R.id.lightScene_ListView);
        lightSceneAdapter= new LightSceneAdapter(lc,getContext());
        scenesListView.setAdapter(lightSceneAdapter);
    }

    private void updateSceneList()
    {
        View v = getView();
        lc = LightScene.listAll(LightScene.class);
        scenesListView = (ListView)v.findViewById(R.id.lightScene_ListView);
        lightSceneAdapter= new LightSceneAdapter(lc,getContext());
        scenesListView.setAdapter(lightSceneAdapter);
    }

    public View setCurrentLightState(View inflater)
    {
        livingRoomLight = (ToggleButton) inflater.findViewById(R.id.lightBtn);
        garageLight = (ToggleButton) inflater.findViewById(R.id.GarageLightBtn);
        corridorLight = (ToggleButton) inflater.findViewById(R.id.CorridorLightBtn);
        kitchenLight = (ToggleButton) inflater.findViewById(R.id.kitchenLightBtn);
        //fastLight = (ToggleButton) inflater.findViewById(R.id.fastLightBtn);

        livingRoomLight.setChecked(tools.isLivingRoomLight());
        garageLight.setChecked(tools.isGarageLight());
        corridorLight.setChecked(tools.isCorridorLight());
        kitchenLight.setChecked(tools.isKitchenLight());
        //fastLight.setChecked(tools.isFastLight());

        return inflater;
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
}
