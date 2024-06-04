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

import app.tabser.model.Song;
import app.tabser.view.MenuAnimator;
import app.tabser.view.SheetView;
import app.tabser.view.input.EditorMenu;
import app.tabser.view.input.MenuView;
import app.tabser.view.input.ViewerMenu;

public class SheetEditorActivity extends AppCompatActivity {

    private SheetView sheetView;
    private EditorMenu editorMenu;
    private ViewerMenu viewerMenu;
    private String fileName;
    private Song song;
    private ScrollView sheetScrollView;
    private MenuView menuView;
    private MenuAnimator menuAnimator;
    private LinearLayout sheetEditorLayout;
    private SheetView.Mode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_editor);
        fileName = getIntent().getStringExtra("fileName");
        String modeString = getIntent().getStringExtra("mode");
        mode = SheetView.Mode.valueOf(modeString);
        sheetView = findViewById(R.id.sheetView);
        sheetScrollView = findViewById(R.id.sheetScrollView);
        menuView = findViewById(R.id.sheetMenu);
        sheetEditorLayout = findViewById(R.id.sheetEditorLayout);
        menuAnimator = new MenuAnimator(sheetEditorLayout, sheetScrollView, menuView, mode);
        sheetView.setMenuAnimator(menuAnimator);
        menuView.setMenuAnimator(menuAnimator);
//        ((LinearLayout.LayoutParams)sheetScrollView.getLayoutParams()).weight = 75f;
//        ((LinearLayout.LayoutParams)menuView.getLayoutParams()).weight = 25f;
        editorMenu = new EditorMenu(sheetView.keyboardRect, sheetView, getApplicationContext(),
                sheetView.getTheme(), sheetView.controller.getPreferences());
        viewerMenu = new ViewerMenu(sheetView, getApplicationContext(), sheetView.getTheme());
        try (InputStream in = openFileInput(fileName)) {
            loadModel(modeString, new ObjectMapper().readValue(in, Song.class));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        menuAnimator.setMode(mode);
        menuAnimator.setHidePhase();
    }

    public void loadModel(String mode, Song model) {
        this.song = model;
        editorMenu.loadModel(model);
        sheetView.loadModel(model);
        sheetView.settings.setMode(SheetView.Mode.valueOf(mode));
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
