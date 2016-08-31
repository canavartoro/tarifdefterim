package com.toro.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.toro.objects.globalObject;
import com.toro.objects.tarifBeans;
import com.toro.screens.Screens;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "yemek.db";

    // Contacts table name
    private static final String DB_PATH = "/data/data/com.toro.tarifdefterim/databases/";
    private static final String TABLE_TARIFLER = "tarifler";
    private static final String TABLE_KATEGORI = "kategori";
    private static final String[] TABLE_TARIFLER_FIELDS = new String[] { "ad",
	    "resim", "resim_url", "hazirlama", "pisirme", "kisi", "malzemeler",
	    "hazirlanisi", "kategori", "puan" };

    // VACUUM tarifler
    // Contacts Table Columns names
    private static final String KEY_FIELD = "ad";

    public DataBaseHandler(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void createdatabase() throws IOException {
	boolean dbexist = checkdatabase();
	if (dbexist) {
	    System.out.println(" Database exists.");
	} else {
	    try {
		copydatabase();
	    } catch (IOException e) {
		e.printStackTrace();
		// throw new Error("Error copying database");
	    }
	}
    }

    private boolean checkdatabase() {

	boolean checkdb = false;
	try {
	    String myPath = DB_PATH + DATABASE_NAME;
	    File dbfile = new File(myPath);

	    checkdb = dbfile.exists();
	} catch (SQLiteException e) {
	    System.out.println("Database doesn't exist");
	}
	return checkdb;
    }

    private void copydatabase() throws IOException {
	// Open your local db as the input stream
	InputStream myinput = globalObject.getContext().getAssets()
		.open(DATABASE_NAME);

	// Path to the just created empty db
	String outfilename = DB_PATH + DATABASE_NAME;

	File file = new File(DB_PATH);
	if (!file.exists() && !file.mkdir())
	    return;

	// Open the empty db as the output stream
	OutputStream myoutput = new FileOutputStream(outfilename);

	// transfer byte to inputfile to outputfile
	byte[] buffer = new byte[1024];
	int length;
	while ((length = myinput.read(buffer)) > 0) {
	    myoutput.write(buffer, 0, length);
	}

	// Close the streams
	myoutput.flush();
	myoutput.close();
	myinput.close();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

	/*
	 * String CREATE_KATEGORI_TABLE = "CREATE TABLE \"" + TABLE_KATEGORI +
	 * "\" ( " + "\"ad\"  TEXT(50) NOT NULL, " +
	 * "\"sira\"  INTEGER DEFAULT 0, " + " PRIMARY KEY (\"ad\") " + ");";
	 * db.execSQL(CREATE_KATEGORI_TABLE);
	 * 
	 * String CREATE_TARIFLER_TABLE = "CREATE TABLE \"" + TABLE_TARIFLER +
	 * "\" ( " + "\"ad\"  TEXT(50) NOT NULL, " + "\"resim\"  TEXT, " +
	 * "\"hazirlama\"  INTEGER, " + "\"pisirme\"  INTEGER, " +
	 * "\"kisi\"  INTEGER, " + "\"malzemeler\"  TEXT, " +
	 * "\"hazirlanisi\"  TEXT, " + "\"kategori\"  TEXT(50), " +
	 * "\"puan\"  INTEGER, " + "PRIMARY KEY (\"ad\") " + "); ";
	 * db.execSQL(CREATE_TARIFLER_TABLE);
	 * 
	 * String CREATE_INDEX =
	 * "CREATE INDEX \"katg\" ON \"tarifler\" (\"kategori\" ASC); ";
	 * db.execSQL(CREATE_INDEX);
	 */
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// Drop older table if existed
	db.execSQL("DROP TABLE IF EXISTS " + TABLE_TARIFLER);
	db.execSQL("DROP TABLE IF EXISTS " + TABLE_KATEGORI);
	// Create tables again
	onCreate(db);
    }

    public// Adding new contact
    void addKategori(String kategori) {

	int sira = 1;// getKategoriCount() + 1;

	SQLiteDatabase db = this.getWritableDatabase();

	String countQuery = "SELECT  * FROM " + TABLE_KATEGORI;
	Cursor cursor = db.rawQuery(countQuery, null);
	sira = cursor.getCount();
	cursor.close();

	ContentValues values = new ContentValues();
	values.put("ad", kategori); // Contact Name
	values.put("sira", sira); // Contact Phone

	// Inserting Row
	db.insert(TABLE_KATEGORI, null, values);
	db.close(); // Closing database connection
    }

    public// Adding new contact
    void addTarif(tarifBeans tarif) {

	if (tarif.getAd().length() < 1) {
	    Screens.showAlert("Yemek adý girilmedi!");
	    return;
	}

	SQLiteDatabase db = this.getWritableDatabase();

	ContentValues values = new ContentValues();
	values.put("ad", tarif.getAd()); // Contact Name
	values.put("resim", tarif.getResim()); // Contact Phone
	values.put("hazirlama", tarif.getHazirlama());
	values.put("pisirme", tarif.getPisirme());
	values.put("kisi", tarif.getKisi());
	values.put("malzemeler", tarif.getMalzemeler());
	values.put("hazirlanisi", tarif.getHazirlanisi());
	values.put("kategori", tarif.getKategori());
	values.put("puan", tarif.getPuan());
	values.put("def", tarif.getDefault());

	// Inserting Row
	db.insert(TABLE_TARIFLER, null, values);
	db.close(); // Closing database connection
    }

    public// Getting single contact
    tarifBeans getTarif(String ad) {
	SQLiteDatabase db = this.getReadableDatabase();

	Cursor cursor = db.query(TABLE_TARIFLER, TABLE_TARIFLER_FIELDS,
		KEY_FIELD + "=?", new String[] { ad }, null, null, null, null);
	if (cursor != null)
	    cursor.moveToFirst();

	tarifBeans tarif = new tarifBeans(cursor.getString(0),
		cursor.getBlob(1), cursor.getString(2), Integer.parseInt(cursor
			.getString(3)), Integer.parseInt(cursor.getString(4)),
		Integer.parseInt(cursor.getString(5)), cursor.getString(6),
		cursor.getString(7), cursor.getString(8),
		Integer.parseInt(cursor.getString(9)));

	// return contact
	db.close();
	return tarif;

    }

    public ArrayList<tarifBeans> getAllTarifler(String filter, Boolean flag) {

	// Select All Query
	String selectQuery = "";
	SQLiteDatabase db = this.getWritableDatabase();
	Cursor cursor = null;

	try {
	    if (globalObject.getKategori() != null
		    && globalObject.getKategori().length() > 0) {
		selectQuery = "SELECT  * FROM \""
			+ TABLE_TARIFLER
			+ "\""
			+ " WHERE \"kategori\" = ? and \"ad\" like ? ORDER BY \"ad\"";
		cursor = db.rawQuery(selectQuery,
			new String[] { globalObject.getKategori(),
				"%" + filter + "%" });
	    } else if (flag) {
		selectQuery = "SELECT  * FROM \"" + TABLE_TARIFLER + "\""
			+ " WHERE \"def\" = 0 ORDER BY \"ad\"";
		cursor = db.rawQuery(selectQuery, null);
	    } else {
		selectQuery = "SELECT  * FROM \"" + TABLE_TARIFLER
			+ "\" WHERE \"ad\" like ? ORDER BY \"ad\"";
		// + "\" WHERE \"ad\" like ? COLLATE NOCASE ORDER BY \"ad\"";
		cursor = db.rawQuery(selectQuery, new String[] { "%" + filter
			+ "%" });
	    }

	    return toArray(cursor);

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    db.close();
	}
	return null;
    }

    private ArrayList<tarifBeans> toArray(Cursor cursor) {
	String strVal = "";
	ArrayList<tarifBeans> tarifList = new ArrayList<tarifBeans>();
	// looping through all rows and adding to list
	if (cursor != null && cursor.moveToFirst()) {
	    do {
		tarifBeans tarif = new tarifBeans();
		tarif.setAd(cursor.getString(0));
		tarif.setResim(cursor.getBlob(1));
		tarif.setResimUrl(cursor.getString(2));
		strVal = cursor.getString(3);
		if (strVal != null && strVal.length() > 0)
		    tarif.setHazirlama(Integer.valueOf(strVal));
		strVal = cursor.getString(4);
		if (strVal != null && strVal.length() > 0)
		    tarif.setPisirme(Integer.valueOf(strVal));
		strVal = cursor.getString(5);
		if (strVal != null && strVal.length() > 0)
		    tarif.setKisi(Integer.valueOf(strVal));
		tarif.setMalzemeler(cursor.getString(6));
		tarif.setHazirlanisi(cursor.getString(7));
		tarif.setKategori(cursor.getString(8));
		strVal = cursor.getString(9);
		if (strVal != null && strVal.length() > 0)
		    tarif.setPuan(Integer.valueOf(strVal));
		strVal = cursor.getString(10);
		if (strVal != null && strVal.length() > 0)
		    tarif.setDefault(Integer.valueOf(strVal));
		// Adding contact to list
		tarifList.add(tarif);
	    } while (cursor.moveToNext());
	}
	// close inserting data from database
	cursor.close();
	// return contact list
	return tarifList;
    }

    public String[] getAllKategori() {
	String[] data = null;
	// Select All Query
	String selectQuery = "SELECT  * FROM \"" + TABLE_KATEGORI
		+ "\" ORDER BY \"sira\"";

	int index = 0;

	SQLiteDatabase db = this.getWritableDatabase();
	Cursor cursor = db.rawQuery(selectQuery, null);
	data = new String[cursor.getCount()];
	// looping through all rows and adding to list
	if (cursor.moveToFirst()) {
	    do {
		data[index] = new String(cursor.getString(0));
		index++;
	    } while (cursor.moveToNext());
	}
	// close inserting data from database
	cursor.close();
	db.close();
	// return contact list
	return data;

    }

    public int updateTarif(tarifBeans tarif) {
	SQLiteDatabase db = this.getWritableDatabase();

	ContentValues values = new ContentValues();
	values.put("resim", tarif.getResim()); // Contact Phone
	// values.put("hazirlama", tarif.getHazirlama());
	// values.put("pisirme", tarif.getPisirme());
	// values.put("kisi", tarif.getKisi());
	// values.put("malzemeler", tarif.getMalzemeler());
	// values.put("hazirlanisi", tarif.getHazirlanisi());
	// values.put("kategori", tarif.getKategori());
	values.put("puan", tarif.getPuan());

	int i = db.update(TABLE_TARIFLER, values, KEY_FIELD + " = ?",
		new String[] { String.valueOf(tarif.getAd()) });

	return i;
    }

    public void deleteTarif(tarifBeans tarif) {
	SQLiteDatabase db = this.getWritableDatabase();
	db.delete(TABLE_TARIFLER, KEY_FIELD + " = ?",
		new String[] { String.valueOf(tarif.getAd()) });
	db.close();
    }

    public void deleteTarifNotDefault() {
	SQLiteDatabase db = this.getWritableDatabase();
	db.delete(TABLE_TARIFLER, " def = ?", new String[] { "0" });
	db.close();
    }

    public void deleteKategori(String ad) {
	SQLiteDatabase db = this.getWritableDatabase();
	db.delete(TABLE_KATEGORI, KEY_FIELD + " = ?", new String[] { ad });
	db.close();
    }

    public int getTarifCount() {
	String countQuery = "SELECT  * FROM " + TABLE_TARIFLER;
	SQLiteDatabase db = this.getReadableDatabase();
	Cursor cursor = db.rawQuery(countQuery, null);
	cursor.close();
	db.close();
	return cursor.getCount();
    }

    public int getKategoriCount() {
	String countQuery = "SELECT  * FROM " + TABLE_KATEGORI;
	SQLiteDatabase db = this.getReadableDatabase();
	Cursor cursor = db.rawQuery(countQuery, null);
	int i = cursor.getCount();
	cursor.close();
	db.close();
	return i;
    }

}
