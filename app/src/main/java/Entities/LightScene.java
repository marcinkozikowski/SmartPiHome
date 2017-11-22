package Entities;


import io.realm.RealmObject;

/**
 * Created by Dell on 2017-11-22.
 */

public class LightScene extends RealmObject {

    public String sceneName;
    public boolean livingRoom,kitchen,corridor,garage;

    public LightScene() {
    }

    public LightScene(String sceneName, boolean livingRoom, boolean kitchen, boolean corridor, boolean garage) {
        this.sceneName = sceneName;
        this.livingRoom = livingRoom;
        this.kitchen = kitchen;
        this.corridor = corridor;
        this.garage = garage;
    }
}
