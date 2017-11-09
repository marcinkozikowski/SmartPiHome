package CustomListAdapters;

/**
 * Created by Dell on 2017-11-09.
 */

public class Song {
    private String path;
    private String title;
    private String Artist;
    private String songLength;

    public Song(String Artist,String Title,String path,String songLength)
    {
        this.Artist = Artist;
        title = Title;
        this.path = path;
        this.songLength = songLength;
    }

    public Song()
    {
        this.Artist = null;
        title = null;
        this.path = null;
        this.songLength = null;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getSongLength() {
        return songLength;
    }

    public void setSongLength(String songLength) {
        this.songLength = songLength;
    }

}
