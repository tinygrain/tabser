package app.tabser;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import app.tabser.model.TabModel;
import app.tabser.view.TabView;

public class EditorActivity extends AppCompatActivity {

    private TabView tabView;
    private String fileName;
    //private TabModel tabModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        fileName = getIntent().getStringExtra("fileName");
        tabView = findViewById(R.id.tabView);
        try(InputStream in = openFileInput(fileName)){
            tabView.loadModel(new ObjectMapper().readValue(in, TabModel.class));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        ObjectMapper om = new ObjectMapper();
        String data = "";
        try {
            data = om.writerWithDefaultPrettyPrinter().writeValueAsString(tabView.getModel());
            Log.d("JSON!", data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        try (FileOutputStream fos = openFileOutput(fileName,
                Context.MODE_PRIVATE)) {
            fos.write(data.getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBackPressed() {
        if (tabView.isInSubMenu()) {
            tabView.showKeyboardMainMenu();
        } else {
            super.onBackPressed();
        }
    }
}
