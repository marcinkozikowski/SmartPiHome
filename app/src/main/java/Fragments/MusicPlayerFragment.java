package Fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.dell.smartpihome.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import CustomListAdapters.MusicListAdapter;
import CustomListAdapters.Song;

import static com.example.dell.smartpihome.Main3Activity.player;

public class MusicPlayerFragment extends Fragment{

    List<Song> songsList = new ArrayList<Song>();
    ListView songsListView;
    MusicListAdapter musicAdapter;
    Song choosenSong;
    TextView nowPlaying;
    ImageView nextSong;
    ImageView previousSong;
    ImageView stopSong;
    ImageView playPouseSong;
    SeekBar seekBar;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MusicPlayerFragment() {
        // Required empty public constructor
    }

    public static MusicPlayerFragment newInstance(String param1, String param2) {
        MusicPlayerFragment fragment = new MusicPlayerFragment();
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
        findAllMusic();

    }

    private String convertSongDuration(String duration)
    {
        int dur = Integer.parseInt(duration);
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;
        String minutes;
        String seconds;
        if(mns<10)
        {
            minutes="0"+mns;
        }
        else
        {
            minutes=mns+"";
        }
        if(scs<10)
        {
            seconds="0"+scs;
        }
        else
        {
            seconds=scs+"";
        }
        return minutes+":"+seconds;
    }

    public void findAllMusic(){
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        List<Song> songs = new ArrayList<Song>();

        if(songCursor != null && songCursor.moveToFirst())
        {
            int artistName = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int durationId = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int pathId = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);


            do {
                String artist = songCursor.getString(artistName);
                String title = songCursor.getString(songTitle);
                String duration = songCursor.getString(durationId);
                String path = songCursor.getString(pathId);
                File myFile = new File(path.toString());
                if(artist.contains("Kortez")||artist.contains("Franko")) {
                    songs.add(new Song(artist, title, myFile.getAbsolutePath(), convertSongDuration(duration)));
                }
            } while(songCursor.moveToNext());
        }
        player.setCurrentPlaylist(songs);
    }

    public void RefreshList(View v)
    {
        songsListView = (ListView)v.findViewById(R.id.MusicListView);

        musicAdapter= new MusicListAdapter(player.getCurrentPlaylist(),getContext());      // Ustawianie wlasnego adaptera w celu wyswietlenia listy

        songsListView.setAdapter(musicAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_music_player, container, false);
        nowPlaying = (TextView)v.findViewById(R.id.NowPlaying_TextView);
        songsListView = (ListView)v.findViewById(R.id.MusicListView);
        nextSong = (ImageView)v.findViewById(R.id.NextImg);
        previousSong = (ImageView)v.findViewById(R.id.PreviousImg);
        stopSong=(ImageView)v.findViewById(R.id.StopImg);
        playPouseSong = (ImageView)v.findViewById(R.id.NowPlayingImg);
        seekBar = (SeekBar)v.findViewById(R.id.ProgresBarPlayer_SeekBar);
        RefreshList(v);
        if(player.isPlaying())
        {
            nowPlaying.setText(player.getCurrentSong().getArtist()+" "+player.getCurrentSong().getTitle());
            getSeekBarStatus();
        }
        songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                choosenSong = player.getCurrentPlaylist().get(position);
                player.setCurrentSong(choosenSong);
                //Toast.makeText(getContext(), "Nie można odtworzyć wybrango utworu "+choosenSong.getPath().toString(), Toast.LENGTH_SHORT).show();
                try {
                    player.startPlaying();
                    nowPlaying.setText(player.getCurrentSong().getArtist()+" "+player.getCurrentSong().getTitle());
                    getSeekBarStatus();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress=0;

            @Override
            public void onProgressChanged(final SeekBar seekBar, int ProgressValue, boolean fromUser) {
                if (fromUser) {
                    player.seekTo(ProgressValue);//if user drags the seekbar, it gets the position and updates in textView.
                }
                final long mMinutes=(ProgressValue/1000)/60;//converting into minutes
                final int mSeconds=((ProgressValue/1000)%60);//converting into seconds
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.nextSong();
                if(player.isPlaying())
                {
                    nowPlaying.setText(player.getCurrentSong().getArtist()+" "+player.getCurrentSong().getTitle());
                }
            }
        });
        previousSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.previousSong();
                if(player.isPlaying())
                {
                    nowPlaying.setText(player.getCurrentSong().getArtist()+" "+player.getCurrentSong().getTitle());
                }
            }
        });
        stopSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stopPlaying();
                if(player.isPlaying())
                {
                    nowPlaying.setText(player.getCurrentSong().getArtist()+" "+player.getCurrentSong().getTitle());
                }
            }
        });
        playPouseSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player.isPlaying())
                {
                    player.pouseSong();
                }
                else
                {
                    player.playSong();
                }
            }
        });
        return v;
    }

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

    public void getSeekBarStatus(){

        new Thread(new Runnable() {

            @Override
            public void run() {
                int currentPosition = 0;
                int total = player.getDuration();
                seekBar.setMax(total);
                while (player != null && currentPosition < total) {
                    try {
                        Thread.sleep(1000);
                        currentPosition = player.currentPosition();
                    } catch (InterruptedException e) {
                        return;
                    }
                    seekBar.setProgress(currentPosition);
                }
            }
        }).start();
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
