package CustomListAdapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.smartpihome.R;

import java.util.List;

import Entities.LightScene;
import io.realm.Realm;

/**
 * Created by Dell on 2017-11-22.
 */

public class LightSceneAdapter extends BaseAdapter {

    Realm realm = Realm.getInstance(Realm.getDefaultConfiguration());
    List<LightScene> scenes = realm.where(LightScene.class).findAll();
    private final LayoutInflater inflater;
    private Context context;

    public LightSceneAdapter(Context context) {
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
        View rowView = inflater.inflate(R.layout.music_item, parent, false);

        TextView sceneName = (TextView) rowView.findViewById(R.id.sceneName_TextView);
        ImageView livingRoom = (ImageView)rowView.findViewById(R.id.sceneLivingRoom_ImageView);
        ImageView kitchen = (ImageView)rowView.findViewById(R.id.sceneKitchen_ImageView);
        ImageView corridor = (ImageView)rowView.findViewById(R.id.sceneCorridor_ImageView);
        ImageView garage = (ImageView)rowView.findViewById(R.id.sceneGarage_ImageView);

        Drawable bulbOn = context.getDrawable(R.drawable.light_bulb_on);
        Drawable bulboff = context.getDrawable(R.drawable.light_bulb_off);

        LightScene c = scenes.get(position);
        sceneName.setText(c.sceneName);
        if(c.livingRoom) {
            livingRoom.setImageDrawable(bulbOn);
        }else
        {
            livingRoom.setImageDrawable(bulboff);
        }
        if(c.kitchen) {
            kitchen.setImageDrawable(bulbOn);
        }else
        {
            kitchen.setImageDrawable(bulboff);
        }
        if(c.corridor) {
            corridor.setImageDrawable(bulbOn);
        }else
        {
            corridor.setImageDrawable(bulboff);
        }
        if(c.garage) {
            garage.setImageDrawable(bulbOn);
        }else
        {
            garage.setImageDrawable(bulboff);
        }

        return rowView;
    }
}
