package app.tabser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import app.tabser.fs.FSAdapter;

public class MainActivity extends AppCompatActivity {

    private ListView contentList;
    private FSAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, EditTabMetaDataActivity.class);
                MainActivity.this.startActivity(i);
            }
        });

        contentList = findViewById(R.id.content_list);
        //data = new ArrayList<>();
        //ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, data);
        adapter = new FSAdapter( this);
        contentList.setAdapter(adapter);
    }
}