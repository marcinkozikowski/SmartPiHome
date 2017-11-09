package CustomListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell.smartpihome.R;

import java.util.List;

/**
 * Created by Dell on 2017-11-09.
 */

public class MusicListAdapter extends BaseAdapter {

    private List<Song> songsList;
    private final LayoutInflater inflater;
    private Context context;

    public MusicListAdapter(List<Song> songsList, Context context) {
        super();
        this.songsList = songsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return songsList.size();
    }

    @Override
    public Object getItem(int position) {
        return songsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.music_item, parent, false);

        TextView artistName = (TextView) rowView.findViewById(R.id.ArtistName_TextView);
        TextView songTitle = (TextView) rowView.findViewById(R.id.SongTitle_TextView);
        TextView songLength = (TextView) rowView.findViewById(R.id.SongLength_TextView);

        Song g = (Song) getItem(position);

        artistName.setText(g.getArtist());
        songTitle.setText(g.getTitle());
        songLength.setText(g.getSongLength());

        return rowView;
    }
}
