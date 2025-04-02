package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class LivroDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "biblioteca.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_LIVROS = "livros";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_AUTOR = "autor";
    public static final String COLUMN_CATEGORIA = "categoria";
    public static final String COLUMN_LIDO = "lido";

    private static final String CREATE_TABLE_LIVROS =
            "CREATE TABLE " + TABLE_LIVROS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TITULO + " TEXT NOT NULL," +
                    COLUMN_AUTOR + " TEXT NOT NULL," +
                    COLUMN_CATEGORIA + " TEXT NOT NULL," +
                    COLUMN_LIDO + " INTEGER NOT NULL);"; // 0 não lido, 1 lido

    public LivroDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LIVROS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIVROS);
        onCreate(db);
    }

    public long adicionarLivro(Livro livro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, livro.getTitulo());
        values.put(COLUMN_AUTOR, livro.getAutor());
        values.put(COLUMN_CATEGORIA, livro.getCategoria());
        values.put(COLUMN_LIDO, livro.isLido() ? 1 : 0);
        long id = db.insert(TABLE_LIVROS, null, values);
        db.close();
        return id;
    }

    public Livro getLivro(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LIVROS,
                new String[]{COLUMN_ID, COLUMN_TITULO, COLUMN_AUTOR, COLUMN_CATEGORIA, COLUMN_LIDO},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Livro livro = new Livro(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4) == 1);
        livro.setId(Integer.parseInt(cursor.getString(0)));
        cursor.close();
        db.close();
        return livro;
    }

    public List<Livro> getAllLivros() {
        List<Livro> listaDeLivros = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_LIVROS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Livro livro = new Livro();
                livro.setId(Integer.parseInt(cursor.getString(0)));
                livro.setTitulo(cursor.getString(1));
                livro.setAutor(cursor.getString(2));
                livro.setCategoria(cursor.getString(3));
                livro.setLido(cursor.getInt(4) == 1);
                listaDeLivros.add(livro);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listaDeLivros;
    }

    public int atualizarLivro(Livro livro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, livro.getTitulo());
        values.put(COLUMN_AUTOR, livro.getAutor());
        values.put(COLUMN_CATEGORIA, livro.getCategoria());
        values.put(COLUMN_LIDO, livro.isLido() ? 1 : 0);
        int rowsAffected = db.update(TABLE_LIVROS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(livro.getId())});
        db.close();
        return rowsAffected;
    }

    public void deletarLivro(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LIVROS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}
