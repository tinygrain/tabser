package app.tabser.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Objects;

public class TuningAdapter extends BaseAdapter {

    private final Activity activity;
    private final LayoutInflater inflater;

    public TuningAdapter(Activity activity) {
        this.activity = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Tuning.STANDARD_TUNINGS.length;
    }

    @Override
    public Object getItem(int i) {
        return Tuning.STANDARD_TUNINGS[i];
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
        String name = Tuning.STANDARD_TUNINGS[i].getName();
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
