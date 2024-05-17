package app.tabser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class Songtext {
    private CharSequence text;
    @JsonIgnore
    private Map<Integer, CharSequence> textPerBarMap = new HashMap<>();

    public Songtext() {
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
}
