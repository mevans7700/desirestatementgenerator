package com.evansappwriter.dsgenerator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class StatementDbAdpater {
	/**
     * Database creation sql statement
     */
	private static final String DATABASE_NAME = "DS_db";
    private static final String DATABASE_TABLE = "statements";
    private static final int DATABASE_VERSION = 1;    
	public static final String KEY_ROWID = "_id";
	public static final String KEY_BOX1 = "desire";
    public static final String KEY_BOX2 = "specific";
    public static final String KEY_BOX3 = "specific_desire";
    public static final String KEY_BOX4 = "timeframe";
    public static final String KEY_BOX5 = "timeframe_feel";
    public static final String KEY_BOX6 = "timeframe_final";
    public static final String KEY_BOX7 = "so_statement";
    public static final String KEY_BOX8 = "part1_statement";
    public static final String KEY_BOX9 = "feelings_brainstorm";
    public static final String KEY_BOX10 = "feelings_words";
    public static final String KEY_BOX11 = "part2_statement";
    public static final String KEY_BOX12 = "purpose_yours";
    public static final String KEY_BOX13 = "purpose_arroundyou";
    public static final String KEY_BOX14 = "purpose_connectedto";
    public static final String KEY_BOX15 = "part3_statement";
    public static final String KEY_BOX16 = "statement_combined";
    public static final String KEY_BOX17 = "statement_scratch";
    public static final String KEY_BOX18 = "checkin_feel";
    public static final String KEY_BOX19 = "checkin_energized";
    public static final String KEY_BOX20 = "checkin_doubt";
    public static final String KEY_BOX21 = "statement_final";
    public static final String KEY_BOX22 = "desire_name";
    public static final String KEY_BOX23 = "finish";    
    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, " 
                    + KEY_BOX1 + " text not null, " + KEY_BOX2 + " text, "
                    + KEY_BOX3 + " text, " + KEY_BOX4 + " text, "
                    + KEY_BOX5 + " text, " + KEY_BOX6 + " text, "
                    + KEY_BOX7 + " text, " + KEY_BOX8 + " text, "
                    + KEY_BOX9 + " text, " + KEY_BOX10 + " text, "
                    + KEY_BOX11 + " text, " + KEY_BOX12 + " text, "
                    + KEY_BOX13 + " text, " + KEY_BOX14 + " text, "
                    + KEY_BOX15 + " text, " + KEY_BOX16 + " text, "
                    + KEY_BOX17 + " text, " + KEY_BOX18 + " text, "
                    + KEY_BOX19 + " text, " + KEY_BOX20 + " text, "
                    + KEY_BOX21 + " text, " + KEY_BOX22 + " text,"
                    + KEY_BOX23 + " integer);";

   
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;
    
    @SuppressWarnings("unused")
	private static final String TAG = "StatementDbAdapter";
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);            
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
            //        + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }
    
    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public StatementDbAdpater(Context ctx) {
        this.mCtx = ctx;        
    }
    
    /**
     * Open the DS database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public StatementDbAdpater open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }
    
    /**
     * Create a new statement row using the first field. If the statement is
     * successfully created return the new rowId for that statement, otherwise return
     * a -1 to indicate failure.
     * 
     * @param desire box 1 of the statement
     * @return rowId or -1 if failed
     */
    public long createEntry(String col1) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_BOX1, col1);
        initialValues.put(KEY_BOX23, 1);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    /**
     * Delete the statement with the given rowId
     * 
     * @param rowId id of statement to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteEntry(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    /**
     * Return a Cursor over the list of all statements in the database
     * 
     * @param String Array of columns
     * @return Cursor over all notes
     */
    public Cursor fetchAllEntries(String[] columns, int type) {
    	
        return mDb.query(DATABASE_TABLE, columns, KEY_BOX23 + "=" + type, null, null, null,  KEY_BOX22);
    }
    
    /**
     * Return a Cursor positioned at the statement that matches the given rowId
     * 
     * @param rowId id of statement to retrieve
     * @param columns columns of statement
     * @return Cursor positioned to matching statement, if found
     * @throws SQLException if statement could not be found/retrieved
     */
    public Cursor fetchEntry(long rowId, String[] columns) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, columns, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    /**
     * Update the statement using the details provided. The statement to be updated is
     * specified using the rowId, and it is altered to use the columns
     * values passed in
     * 
     * @param rowId id of statement to update
     * @param args contentvalues to set statement
     * @return true if the statement was successfully updated, false otherwise
     */
    public boolean updateEntry(long rowId, ContentValues args) {
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}
