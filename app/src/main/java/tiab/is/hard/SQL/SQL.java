package tiab.is.hard.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import tiab.is.hard.BigBoyPageTabs.BigBoyPage;

public class SQL extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TIAB.db";
    private static final String COL_1 = "_ID";
    private static final String COL_2 = "NAME";
    private static final String COL_3 = "DISPLAY";
    private static final String COL_4 = "CATEGORY";
    private static final String COL_1_PLATE = "NAME";
    private static final String COL_2_PLATE = "DISPLAY";
    private static final String COL_3_PLATE = "CALORIES";
    private static final String COL_4_PLATE = "TIME";
    private static final String COL_5_PLATE = "INGREDIENTS";
    private static final String COL_6_PLATE = "AMOUNTS";
    private static final String COL_7_PLATE = "STEPS";
    private static final String COL_8_PLATE = "VIDEOS";
    private static final String COL_9_PLATE = "CATEGORIES";
    private static final String COL_1_SETTINGS = "SETTING";
    private static final String COL_2_SETTINGS = "VALUE";
    private static final String COL_1_PLATE_TYPES = "TYPE";

    public SQL(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE if not exists " + "PLATE_TYPES" + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_1_PLATE_TYPES + " TEXT " + ");");
        sqLiteDatabase.execSQL("CREATE TABLE if not exists " + "SELECTED_INGREDIENTS" + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT, " + COL_3 + " TEXT, " + COL_4 + " TEXT " + ");");
        sqLiteDatabase.execSQL("CREATE TABLE if not exists " + "ALL_INGREDIENTS" + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT, " + COL_3 + " TEXT, " + COL_4 + " TEXT " + ");");
        sqLiteDatabase.execSQL("CREATE TABLE if not exists " + "ALL_PLATES" + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_1_PLATE + " TEXT, " + COL_2_PLATE + " TEXT, " + COL_3_PLATE + " TEXT, " + COL_4_PLATE + " TEXT, " + COL_5_PLATE + " TEXT, " + COL_6_PLATE + " TEXT, " + COL_7_PLATE + " TEXT, " + COL_8_PLATE + " TEXT, " + COL_9_PLATE + " TEXT " + ");");
        sqLiteDatabase.execSQL("CREATE TABLE if not exists " + "PLATES_I_CAN_COOK" + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_1_PLATE + " TEXT, " + COL_2_PLATE + " TEXT, " + COL_3_PLATE + " TEXT, " + COL_4_PLATE + " TEXT, " + COL_5_PLATE + " TEXT, " + COL_6_PLATE + " TEXT, " + COL_7_PLATE + " TEXT, " + COL_8_PLATE + " TEXT, " + COL_9_PLATE + " TEXT " + ");");
        sqLiteDatabase.execSQL("CREATE TABLE if not exists " + "SETTINGS" + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_1_SETTINGS + " TEXT, " + COL_2_SETTINGS + " TEXT " + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BigBoyPage.SELECTED_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void insertData(String NAME, String DISPLAY, String CATEGORY){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, NAME);
        contentValues.put(COL_3, DISPLAY);
        contentValues.put(COL_4, CATEGORY);
        sqLiteDatabase.insert(BigBoyPage.SELECTED_TABLE, null, contentValues);
    }

    public void insertPlateType(String TYPE){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_PLATE_TYPES, TYPE);
        sqLiteDatabase.insert(BigBoyPage.SELECTED_TABLE, null, contentValues);
    }

    public void insertPlate(String NAME, String DISPLAY, String CALORIES, String TIME, String INGREDIENTS, String AMOUNTS, String STEPS, String VIDEOS, String CATEGORIES){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_PLATE, NAME);
        contentValues.put(COL_2_PLATE, DISPLAY);
        contentValues.put(COL_3_PLATE, CALORIES);
        contentValues.put(COL_4_PLATE, TIME);
        contentValues.put(COL_5_PLATE, INGREDIENTS);
        contentValues.put(COL_6_PLATE, AMOUNTS);
        contentValues.put(COL_7_PLATE, STEPS);
        contentValues.put(COL_8_PLATE, VIDEOS);
        contentValues.put(COL_9_PLATE, CATEGORIES);
        sqLiteDatabase.insert(BigBoyPage.SELECTED_TABLE, null, contentValues);
    }

    public void insertSetting(String SETTING, String VALUE){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_SETTINGS, SETTING);
        contentValues.put(COL_2_SETTINGS, VALUE);
        sqLiteDatabase.insert(BigBoyPage.SELECTED_TABLE, null, contentValues);
    }

    //outputting data
    public Cursor getAllDate() {
        //create database class
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //instance
        return sqLiteDatabase.rawQuery("select * from " + BigBoyPage.SELECTED_TABLE + ";", null);
    }

    //modify data
    public boolean updateSetting(String _ID, String SETTING, String VALUE){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, _ID);
        contentValues.put(COL_1_SETTINGS, SETTING);
        contentValues.put(COL_2_SETTINGS, VALUE);
        sqLiteDatabase.update(BigBoyPage.SELECTED_TABLE, contentValues, COL_1_SETTINGS + "=?", new String[] { SETTING });
        return true;
    }

    //modify data
    public void deleteEverything(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from "+ BigBoyPage.SELECTED_TABLE);
    }

    //delete data from database
    public void deleteData(String NAME){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //this delete method returns the number of rows deleted as an integer
        sqLiteDatabase.delete(BigBoyPage.SELECTED_TABLE, COL_2 + "=?", new String[]{NAME});
    }
}