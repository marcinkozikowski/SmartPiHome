package CustomListAdapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.smartpihome.R;

import java.util.List;

import Entities.LightScene;


public class LightSceneAdapter extends BaseAdapter {

    List<LightScene> scenes;
    private final LayoutInflater inflater;
    private Context context;

    public LightSceneAdapter(List<LightScene> list,Context context) {
        scenes = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return scenes.size();
    }

    @Override
    public Object getItem(int position) {
        return scenes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.light_scene_item, parent, false);

        TextView sceneName = (TextView) rowView.findViewById(R.id.sceneName_TextView);
        ImageView livingRoom = (ImageView)rowView.findViewById(R.id.sceneLivingRoom_ImageView);
        ImageView kitchen = (ImageView)rowView.findViewById(R.id.sceneKitchen_ImageView);
        ImageView corridor = (ImageView)rowView.findViewById(R.id.sceneCorridor_ImageView);
        ImageView garage = (ImageView)rowView.findViewById(R.id.sceneGarage_ImageView);

        Drawable bulbOn = context.getDrawable(R.drawable.light_bulb_on);
        Drawable bulboff = context.getDrawable(R.drawable.light_bulb_off);

        LightScene c = (LightScene) getItem(position);
        sceneName.setText(c.getSceneName());
        if(c.isLivingRoom()) {
            livingRoom.setImageDrawable(bulbOn);
        }else
        {
            livingRoom.setImageDrawable(bulboff);
        }
        if(c.isKitchen()) {
            kitchen.setImageDrawable(bulbOn);
        }else
        {
            kitchen.setImageDrawable(bulboff);
        }
        if(c.isCorridor()) {
            corridor.setImageDrawable(bulbOn);
        }else
        {
            corridor.setImageDrawable(bulboff);
        }
        if(c.isGarage()) {
            garage.setImageDrawable(bulbOn);
        }else
        {
            garage.setImageDrawable(bulboff);
        }

        LinearLayout delete = (LinearLayout) rowView.findViewById(R.id.deleteSceneItem);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scenes.remove(position);
                c.delete();
                LightSceneAdapter.this.notifyDataSetChanged();
                Toast.makeText(parent.getContext(),"Pomyślnie usunięto scenę",Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }
}
