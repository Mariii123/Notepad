package com.example.notepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "test.db";
    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE notes(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT);";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public long insertNote(String title, String desc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("description", desc);
        long id = db.insert("notes", null, cv);
        db.close();
        return id;
    }
    public Notes getNote(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("notes",
                new String[]{Notes.COLUMN_ID, Notes.COLUMN_TITLE, Notes.COLUMN_DESCRIPTION}
                , Notes.COLUMN_ID + "=?"
                , new String[]{String.valueOf(id)}
                , null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        Notes note = new Notes(
                cursor.getInt(cursor.getColumnIndex(Notes.COLUMN_ID))
        , cursor.getString(cursor.getColumnIndex(Notes.COLUMN_TITLE))
        , cursor.getString(cursor.getColumnIndex(Notes.COLUMN_DESCRIPTION)));
        cursor.close();
        return note;
    }
    public List<Notes> getAllNotes(){
        List<Notes> notes = new ArrayList<>();
        String selectQuery = "Select * from notes ORDER BY " + Notes.COLUMN_TITLE + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Notes note = new Notes();
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));
                note.setDesc(cursor.getString(2));
                notes.add(note);
            }
            while (cursor.moveToNext());

        }
        return notes;
    }
    public void updateNote(Notes note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Notes.COLUMN_DESCRIPTION, note.getDesc());
        db.update("notes", cv,Notes.COLUMN_ID + "=?", new String[]{String.valueOf(note.getId())});
    }
    public void deleteNote(Notes notes){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("notes", Notes.COLUMN_ID + "= ?", new String[]{String.valueOf(notes.getId())});
        db.close();
    }
}
