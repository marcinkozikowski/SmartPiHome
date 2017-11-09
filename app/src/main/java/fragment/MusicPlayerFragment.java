package fragment;

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
import android.widget.ListView;

import com.example.dell.smartpihome.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import CustomListAdapters.MusicListAdapter;
import CustomListAdapters.Song;
import Tools.MyMediaPlayer;

public class MusicPlayerFragment extends Fragment {

    MyMediaPlayer player=new MyMediaPlayer();
    List<Song> songsList = new ArrayList<Song>();
    ListView songsListView;
    MusicListAdapter musicAdapter;
    Song choosenSong;

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
                songs.add(new Song(artist, title, myFile.getAbsolutePath(),duration));
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
        songsListView = (ListView)v.findViewById(R.id.MusicListView);
        RefreshList(v);
        songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                choosenSong = player.getCurrentPlaylist().get(position);
                player.setCurrentSong(choosenSong);
                //Toast.makeText(getContext(), "Nie można odtworzyć wybrango utworu "+choosenSong.getPath().toString(), Toast.LENGTH_SHORT).show();
                try {
                    player.startPlaying();
                } catch (IOException e) {
                    e.printStackTrace();
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


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
