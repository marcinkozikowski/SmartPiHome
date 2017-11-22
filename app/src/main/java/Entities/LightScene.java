package Entities;


import com.orm.SugarRecord;

public class LightScene extends SugarRecord {

    private String sceneName;
    private boolean livingRoom,kitchen,corridor,garage;


    public LightScene() {
    }

    public LightScene(String sceneName, boolean livingRoom, boolean kitchen, boolean corridor, boolean garage) {
        this.sceneName = sceneName;
        this.livingRoom = livingRoom;
        this.kitchen = kitchen;
        this.corridor = corridor;
        this.garage = garage;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public boolean isLivingRoom() {
        return livingRoom;
    }

    public void setLivingRoom(boolean livingRoom) {
        this.livingRoom = livingRoom;
    }

    public boolean isKitchen() {
        return kitchen;
    }

    public void setKitchen(boolean kitchen) {
        this.kitchen = kitchen;
    }

    public boolean isCorridor() {
        return corridor;
    }

    public void setCorridor(boolean corridor) {
        this.corridor = corridor;
    }

    public boolean isGarage() {
        return garage;
    }

    public void setGarage(boolean garage) {
        this.garage = garage;
    }
}
