package app.tabser.dom;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class SongText {
    private String songVersionTitle;
    private CharSequence text;
    /**
     * {@code Map<barIndex, textByBar>}
     */
    @JsonIgnore
    private Map<Integer, CharSequence> textPerBarMap = new HashMap<>();

    public SongText() {
    }

    public CharSequence getText() {
        return text;
    }


    public CharSequence getTextByBar(Integer bar) {
        return textPerBarMap.getOrDefault(bar, "");
    }

    public void setText(CharSequence text) {
        this.text = text;
        // TODO parse into map
    }

    public String getSongVersionTitle() {
        return songVersionTitle;
    }

    public void setSongVersionTitle(String songVersionTitle) {
        this.songVersionTitle = songVersionTitle;
    }
}
