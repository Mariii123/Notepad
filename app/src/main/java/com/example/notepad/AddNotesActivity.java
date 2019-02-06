package com.example.notepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;

public class AddNotesActivity extends AppCompatActivity {
    Toolbar addNotesToolbar;
    TextView addNotesTitle;
    EditText notesTxt;
    DatabaseHelper helper;
    String title, time;
    Menu myMenu;
    MenuItem saveItem, clearItem;
    List<Notes> notesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        addNotesToolbar = findViewById(R.id.add_note_toolbar);
        helper = new DatabaseHelper(getApplicationContext());
        notesList = new ArrayList<>();
        setSupportActionBar(addNotesToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addNotesTitle = findViewById(R.id.add_note_title);
        notesTxt = findViewById(R.id.add_note_text);
        long millis = new Date().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        Date date = new Date();
        time = format.format(date);
        title = time + " | 0 characters";
        addNotesTitle.setText(title);
        notesTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                addNotesTitle.setText(title);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int c;
                c = s.length();
                title = time + " | " + c + " characters";
                addNotesTitle.setText(title);
                saveItem = myMenu.findItem(R.id.save_note);
                clearItem = myMenu.findItem(R.id.clear_note);
                if (c > 0) {
                    clearItem.setVisible(true);
                    saveItem.setVisible(true);
                } else {
                    clearItem.setVisible(false);
                    saveItem.setVisible(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.myMenu = menu;
        getMenuInflater().inflate(R.menu.add_note_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            case R.id.clear_note:
                notesTxt.setText("");
                clearItem.setVisible(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        String noteTxt = notesTxt.getText().toString();
        long id = helper.insertNote(time, noteTxt);
        if (id != -1) {
            Intent i = new Intent(AddNotesActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

}
