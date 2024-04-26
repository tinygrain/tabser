package app.tabser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import app.tabser.fs.FS;
import app.tabser.model.Beat;
import app.tabser.model.BeatAdapter;
import app.tabser.model.TabModel;
import app.tabser.model.Tuning;
import app.tabser.model.TuningAdapter;

public class EditTabMetaDataActivity extends AppCompatActivity
/*{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tab_meta_data);
    }
}*/ {

    private TuningAdapter tuningAdapter;
    private BeatAdapter beatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tab_meta_data);
        Spinner spStrings = findViewById(R.id.spTuning);
        tuningAdapter = new TuningAdapter(this);
        spStrings.setAdapter(tuningAdapter);
        beatAdapter = new BeatAdapter(this);
        Spinner spBeat = findViewById(R.id.spBeat);
        spBeat.setAdapter(beatAdapter);
        Toolbar metaToolbar = findViewById(R.id.metaToolbar);
        setSupportActionBar(metaToolbar);
    }

    public void onCancel(View view) {
        finish();
    }

    public void onSave(View view) {
        EditText etTitle = findViewById(R.id.etTabTitle);
        EditText etArtist = findViewById(R.id.etTabArtist);
        EditText etAlbum = findViewById(R.id.etTabAlbum);
        EditText etReleased = findViewById(R.id.etTabReleased);
        EditText etTempo = findViewById(R.id.etTabTempo);
        RadioButton rbBassClef = findViewById(R.id.rbTabClefBass);
        RadioButton rbTrebleClef = findViewById(R.id.rbTabClefTreble);
        Spinner spTuning = findViewById(R.id.spTuning);
        EditText etInstrument = findViewById(R.id.etTabInstrumentName);
        String title = etTitle.getText().toString();

        TabModel model = new TabModel();
        if ("".equals(title)) {
            error(R.string.error_title_is_empty);
            etTitle.requestFocus();
            return;
        }
        model.setTitle(title);
        String artist = etArtist.getText().toString();
        if ("".equals(artist)) {
            error(R.string.error_artist_is_empty);
            etArtist.requestFocus();
            return;
        }
        model.setArtist(artist);
        int year = 0;
        try {
            year = Integer.parseInt(etReleased.getText().toString());
        } catch (NumberFormatException e) {
            error(R.string.error_year_is_empty);
            etReleased.requestFocus();
            return;
        }
        String instrument = etInstrument.getText().toString();

        if ("".equals(instrument)) {
            error(R.string.error_instrument_is_empty);
            etInstrument.requestFocus();
            return;
        }
        model.setInstrument(instrument);
        model.setAlbum(etAlbum.getText().toString());
        if (etTempo.getText().length() > 0)
            model.setTempo(Integer.parseInt(etTempo.getText().toString()));

        if (rbBassClef.isChecked()) {
            model.setClef(TabModel.Clef.BASS);
        } else {
            model.setClef(TabModel.Clef.TREBLE);
        }
        Tuning tuning = (Tuning) tuningAdapter.getItem(spTuning.getSelectedItemPosition());
        model.setTuning(tuning);
        Spinner spBeat = findViewById(R.id.spBeat);
        model.setBeat((Beat) spBeat.getSelectedItem());
        ObjectMapper om = new ObjectMapper();
        String data = "";
        try {
            data = om.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String fileName = FS.getName(title, artist, year, instrument);
        try (FileOutputStream fos = openFileOutput(fileName,
                Context.MODE_PRIVATE)) {
            fos.write(data.getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Intent i = new Intent(this, EditorActivity.class);
        i.putExtra("fileName", fileName);
        startActivity(i);
        finish();
    }

    private void error(int error_id) {
        new AlertDialog.Builder(this).setTitle(R.string.error_title).setMessage(error_id).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }
}