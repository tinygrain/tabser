package app.tabser.fs;

import app.tabser.dom.Song;

public class FS {
    private static final String formatName = "%s - %s - %d - %s.json";
    public static final String getName(String title, String artist, int year, String instrument) {
        return String.format(formatName, artist, title, year, instrument);
    }
    boolean exists(String title, String artist, int year, String instrument){
        return false;
    }

    void store(Song model){

    }
}
