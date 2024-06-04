package app.tabser.view.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import app.tabser.dom.Beat;

public class BeatAdapter extends BaseAdapter {
    private List<Beat> beats = Arrays.asList(Beat.FOUR_FOURTH, Beat.THREE_FOURTH);
    private final Activity activity;
    private final LayoutInflater inflater;

    public BeatAdapter(Activity activity) {
        this.activity = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return beats.size();
    }

    @Override
    public Object getItem(int i) {
        return beats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (Objects.isNull(view)) {
            view = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }
        Beat b = beats.get(i);
        String name = String.format("%d/%d", b.getBar(), b.getCount());
        ((TextView) view.findViewById(android.R.id.text1)).setText(name);
        /*
        view.setOnClickListener(v -> {
            Intent editorIntent = new Intent(activity, EditorActivity.class);
            editorIntent.putExtra("fileName", name);
            activity.startActivity(editorIntent);
        });
         */
        return view;
    }
}
