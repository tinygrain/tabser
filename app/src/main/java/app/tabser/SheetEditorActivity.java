package app.tabser;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import app.tabser.dom.Song;
import app.tabser.sheetview.MenuAnimator;
import app.tabser.sheetview.SheetView;
import app.tabser.sheetview.menu.EditorMenu;
import app.tabser.sheetview.menu.MenuView;
import app.tabser.sheetview.menu.ViewerMenu;

public class SheetEditorActivity extends AppCompatActivity {
    private SheetView sheetView;
    private EditorMenu editorMenu;
    private ViewerMenu viewerMenu;
    private String fileName;
    private Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_editor);
        fileName = getIntent().getStringExtra("fileName");
        String modeString = getIntent().getStringExtra("mode");
        SheetView.Mode mode = SheetView.Mode.valueOf(modeString);
        sheetView = findViewById(R.id.sheetView);
        ScrollView sheetScrollView = findViewById(R.id.sheetScrollView);
        sheetScrollView.setOnGenericMotionListener(sheetView);
        MenuView menuView = findViewById(R.id.sheetMenu);
        LinearLayout sheetEditorLayout = findViewById(R.id.sheetEditorLayout);
        MenuAnimator menuAnimator = new MenuAnimator(sheetEditorLayout, sheetScrollView, menuView, mode);
        sheetView.setMenuAnimator(menuAnimator);
        menuView.setMenuAnimator(menuAnimator);
        editorMenu = new EditorMenu(sheetView.keyboardRect, sheetView, getApplicationContext(),
                sheetView.getTheme(), sheetView.controller.getPreferences());
        int[] buttons = {R.drawable.baseline_play_arrow_24, R.drawable.baseline_stop_24,
                R.drawable.baseline_loop_24, R.drawable.metronome, R.drawable.baseline_edit_24};
        viewerMenu = new ViewerMenu(sheetView, getApplicationContext(), sheetView.getTheme(), buttons);
        try (InputStream in = openFileInput(fileName)) {
            loadModel(mode, new ObjectMapper().readValue(in, Song.class));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        menuAnimator.startMode(mode);
    }

    public void loadModel(SheetView.Mode mode, Song model) {
        this.song = model;
        editorMenu.loadModel(model);
        sheetView.loadModel(model);
        sheetView.settings.setMode(mode);
        viewerMenu.loadModel(model);
        sheetView.invalidate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ObjectMapper om = new ObjectMapper();
        String data = "";
        try {
            data = om.writerWithDefaultPrettyPrinter().writeValueAsString(song);
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
        if (editorMenu.isInSubMenu()) {
            editorMenu.showMainMenu(sheetView);
        } else {
            super.onBackPressed();
        }
    }
}
