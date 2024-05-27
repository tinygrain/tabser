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

import app.tabser.model.Song;
import app.tabser.view.SheetView;

public class SheetEditorActivity extends AppCompatActivity {

    private SheetView sheetView;
    private String fileName;
    private Song song;
    //private TabModel tabModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_editor);
        fileName = getIntent().getStringExtra("fileName");
        String mode = getIntent().getStringExtra("mode");
        sheetView = findViewById(R.id.sheetView);
        try(InputStream in = openFileInput(fileName)){
            loadModel(mode, new ObjectMapper().readValue(in, Song.class));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadModel(String mode, Song model) {
        this.song = model;
        keyboard.loadModel(model);
        sheetView.loadModel(model);
        sheetView.settings.setMode(SheetView.Mode.valueOf(mode));
        viewControls.loadModel(model);
        invalidate();
    }

    @Override
    protected void onPause() {
        super.onPause();

        ObjectMapper om = new ObjectMapper();
        String data = "";
        try {
            data = om.writerWithDefaultPrettyPrinter().writeValueAsString(sheetScrollView.getModel());
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
        if (sheetScrollView.isInSubMenu()) {
            sheetScrollView.showKeyboardMainMenu();
        } else {
            super.onBackPressed();
        }
    }
}
