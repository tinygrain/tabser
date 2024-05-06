package app.tabser.fs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.Objects;

import app.tabser.EditorActivity;

public class FSAdapter extends BaseAdapter {
    private final File dataDir;
    private File[] files;

    private final Activity activity;
    private final LayoutInflater inflater;

    public FSAdapter(Activity activity) {
        this.activity = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dataDir = activity.getApplicationContext().getFilesDir();
        refresh();
    }

    public void refresh() {
        files = dataDir.listFiles();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return files.length;
    }

    @Override
    public Object getItem(int i) {
        return files[i].getName();
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
        String fileName = files[i].getName();
        ((TextView) view.findViewById(android.R.id.text1)).setText(fileName);
        view.setOnClickListener(v -> {
            Intent editorIntent = new Intent(activity, EditorActivity.class);
            editorIntent.putExtra("fileName", fileName);
            editorIntent.putExtra("mode", "VIEW");
            activity.startActivity(editorIntent);
        });
        return view;
    }
}
