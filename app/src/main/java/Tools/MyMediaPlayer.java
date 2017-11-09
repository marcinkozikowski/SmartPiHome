package Tools;

import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import CustomListAdapters.Song;


public final class MyMediaPlayer {


    private static final MediaPlayer player = new MediaPlayer();
    private static Song currentSong;
    private boolean playing;

    public static MediaPlayer getPlayer() {
        return player;
    }

    public static Song getCurrentSong() {
        return currentSong;
    }

    public static void setCurrentSong(Song currentSong) {
        MyMediaPlayer.currentSong = currentSong;
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public static List<Song> getCurrentPlaylist() {
        return currentPlaylist;
    }

    public static void setCurrentPlaylist(List<Song> currentPlaylist) {
        MyMediaPlayer.currentPlaylist = currentPlaylist;
    }

    private static List<Song> currentPlaylist = new ArrayList<>();



    public void startPlaying() throws IOException {
        try {
            player.reset();
            player.setDataSource(currentSong.getPath());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void stopPlaying()
    {
        player.stop();
    }
    public void nextSong()
    {
        int current = currentPlaylist.indexOf(currentSong);
        if(current+1<currentPlaylist.size()) {
            player.reset();
            try {
                player.setDataSource(currentPlaylist.get(current+1).getPath());
                player.prepare();
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else
        {
            player.reset();
            try {
                player.setDataSource(currentPlaylist.get(0).getPath());
                player.prepare();
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}

