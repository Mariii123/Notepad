package com.example.notepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.content.*;
import android.widget.Toast;

public class UpdateNoteActivity extends AppCompatActivity {
    TextView updateNoteTitle;
    EditText updateNoteDesc;
    String time;
    long id;
    Menu myMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);
        Toolbar uToolbar = findViewById(R.id.update_note_toolbar);
        setSupportActionBar(uToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        updateNoteTitle = findViewById(R.id.update_note_title);
        updateNoteDesc = findViewById(R.id.update_note_desc);
        Intent i = getIntent();
        id = Long.parseLong(i.getStringExtra("id"));
        time = i.getStringExtra("title");
        String desc = i.getStringExtra("desc");
        final String titleTxt = time + " | " + desc.length() + " characters";
        updateNoteTitle.setText(titleTxt);
        updateNoteDesc.setText(desc);
        updateNoteDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MenuItem update = myMenu.findItem(R.id.update_note);
                MenuItem clear = myMenu.findItem(R.id.u_clear_note);
                int c = s.length();
                String title = time + " | " + c + " characters";
                updateNoteTitle.setText(title);
                if (c > 0){
                    update.setVisible(true);
                    clear.setVisible(true);
                }
                else {
                    update.setVisible(false);
                    clear.setVisible(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        myMenu = menu;
        getMenuInflater().inflate(R.menu.update_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.u_clear_note:
                updateNoteDesc.setText("");
                return true;
            case R.id.update_note:
                updateNote();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateNote() {
        DatabaseHelper helper= new DatabaseHelper(this);
        String note = updateNoteDesc.getText().toString();
        Notes notes = new Notes((int)id, time, note);
        helper.updateNote(notes);
        Toast.makeText(getApplicationContext(), "Updated Notes Successfully", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
}
