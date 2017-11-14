package Tools;

/**
 * Created by Dell on 2017-10-26.
 */

public final class CurrentDeviceState {

    private static int livingRoomLightPin;
    private static int kitchenLightPin;
    private static int garageLightPin;
    private static int corridorLightPin;
    private static int garageDoorPin;
    private static int frontDoorPin;
    private static int kitchenBlindPin;
    private static int livingRoomBlindPin;
    private static int livingRoomBlindPosition;
    private static boolean alarm;
    private static boolean kitchenBlind,livingRoomBlind;
    private static boolean garageDoor,frontDoor;
    private static int kitchenBlindPosition;

    public static int getLivingRoomLightPin() {
        return livingRoomLightPin;
    }

    public static void setLivingRoomLightPin(int livingRoomLightPin) {
        CurrentDeviceState.livingRoomLightPin = livingRoomLightPin;
    }

    public static int getKitchenLightPin() {
        return kitchenLightPin;
    }

    public static void setKitchenLightPin(int kitchenLightPin) {
        CurrentDeviceState.kitchenLightPin = kitchenLightPin;
    }

    public static int getGarageLightPin() {
        return garageLightPin;
    }

    public static void setGarageLightPin(int garageLightPin) {
        CurrentDeviceState.garageLightPin = garageLightPin;
    }

    public static int getCorridorLightPin() {
        return corridorLightPin;
    }

    public static void setCorridorLightPin(int corridorLightPin) {
        CurrentDeviceState.corridorLightPin = corridorLightPin;
    }

    public static int getGarageDoorPin() {
        return garageDoorPin;
    }

    public static void setGarageDoorPin(int garageDoorPin) {
        CurrentDeviceState.garageDoorPin = garageDoorPin;
    }

    public static int getFrontDoorPin() {
        return frontDoorPin;
    }

    public static void setFrontDoorPin(int frontDoorPin) {
        CurrentDeviceState.frontDoorPin = frontDoorPin;
    }

    public static int getKitchenBlindPin() {
        return kitchenBlindPin;
    }

    public static void setKitchenBlindPin(int kitchenBlindPin) {
        CurrentDeviceState.kitchenBlindPin = kitchenBlindPin;
    }

    public static int getLivingRoomBlindPin() {
        return livingRoomBlindPin;
    }

    public static void setLivingRoomBlindPin(int livingRoomBlindPin) {
        CurrentDeviceState.livingRoomBlindPin = livingRoomBlindPin;
    }



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

    public static int getKitchenBlindPosition() {
        return kitchenBlindPosition;
    }

    public static void setKitchenBlindPosition(int kitchenBlindPosition) {
        CurrentDeviceState.kitchenBlindPosition = kitchenBlindPosition;
    }

    public static int getLivingRoomBlindPosition() {
        return livingRoomBlindPosition;
    }

    public static void setLivingRoomBlindPosition(int livingRoomBlindPosition) {
        CurrentDeviceState.livingRoomBlindPosition = livingRoomBlindPosition;
    }

    public static boolean isAlarm() {
        return alarm;
    }

    public static void setAlarm(boolean alarm) {
        CurrentDeviceState.alarm = alarm;
    }

}
