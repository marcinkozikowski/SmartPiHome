package Tools;

/**
 * Created by Dell on 2017-10-26.
 */

public final class CurrentDeviceState {
    public static boolean isKitchenLight() {
        return kitchenLight;
    }

    public static void setKitchenLight(boolean kitchenLight) {
        CurrentDeviceState.kitchenLight = kitchenLight;
    }

    public static boolean isLivingRoomLight() {
        return livingRoomLight;
    }

    public static void setLivingRoomLight(boolean livingRoomLight) {
        CurrentDeviceState.livingRoomLight = livingRoomLight;
    }

    public static boolean isCorridorLight() {
        return corridorLight;
    }

    public static void setCorridorLight(boolean corridorLight) {
        CurrentDeviceState.corridorLight = corridorLight;
    }

    public static boolean isGarageLight() {
        return garageLight;
    }

    public static void setGarageLight(boolean garageLight) {
        CurrentDeviceState.garageLight = garageLight;
    }

    public static boolean isFastLight() {
        return fastLight;
    }

    public static void setFastLight(boolean fastLight) {
        CurrentDeviceState.fastLight = fastLight;
    }


    public static boolean isGarageDoor() {
        return garageDoor;
    }

    public static void setGarageDoor(boolean garageDoor) {
        CurrentDeviceState.garageDoor = garageDoor;
    }

    public static boolean isFrontDoor() {
        return frontDoor;
    }

    public static void setFrontDoor(boolean frontDoor) {
        CurrentDeviceState.frontDoor = frontDoor;
    }

    private static boolean kitchenLight=false,livingRoomLight=false,corridorLight=false,garageLight=false,fastLight=false;


    public static boolean isKitchenBlind() {
        return kitchenBlind;
    }

    public static void setKitchenBlind(boolean kitchenBlind) {
        CurrentDeviceState.kitchenBlind = kitchenBlind;
    }

    public static boolean isLivingRoomBlind() {
        return livingRoomBlind;
    }

    public static void setLivingRoomBlind(boolean livingRoomBlind) {
        CurrentDeviceState.livingRoomBlind = livingRoomBlind;
    }

    private static boolean kitchenBlind,livingRoomBlind;

    private static boolean garageDoor,frontDoor;

    public static boolean isAlarm() {
        return alarm;
    }

    public static void setAlarm(boolean alarm) {
        CurrentDeviceState.alarm = alarm;
    }

    private static boolean alarm;
}
