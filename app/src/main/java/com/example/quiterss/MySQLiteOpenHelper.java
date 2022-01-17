package com.example.quiterss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.quiterss.bean.Channel;
import com.example.quiterss.bean.Folder;
import com.example.quiterss.bean.Item;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mySQLite.db";
    private static final String TABLE_NAME_ITEM = "item";
    private static final String TABLE_NAME_CHANNEL = "channel";
    private static final String TABLE_NAME_FOLDER = "folder";
    private static final String TABLE_NAME_FOLDER_ITEM = "folder_item";

    private static final String CREATE_TABLE_ITEM = "create table " + TABLE_NAME_ITEM + " (id integer primary key autoincrement, " +
            "title text, description text, link text, pubDate text, guid text, channel text, read integer);\n";
    private static final String CREATE_TABLE_CHANNEL = "create table " + TABLE_NAME_CHANNEL + " (id integer primary key autoincrement, " +
            "title text, description text, link text);\n";
    private static final String CREATE_TABLE_FOLDER = "create table " + TABLE_NAME_FOLDER + " (id integer primary key autoincrement, " +
            "name text unique not null);\n";
    private static final String CREATE_TABLE_FOLDER_ITEM = "create table " + TABLE_NAME_FOLDER_ITEM + " (id integer primary key autoincrement, " +
            "itemName text, folderName text);\n";


    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ITEM);
        sqLiteDatabase.execSQL(CREATE_TABLE_CHANNEL);
        sqLiteDatabase.execSQL(CREATE_TABLE_FOLDER);
        sqLiteDatabase.execSQL(CREATE_TABLE_FOLDER_ITEM);
        sqLiteDatabase.execSQL("insert into folder values(0, 'a')");
        sqLiteDatabase.execSQL("insert into folder values(1, 'b')");
        sqLiteDatabase.execSQL("insert into folder_item values(0, 'aa', 'a')");
        sqLiteDatabase.execSQL("insert into folder_item values(1, 'ab', 'a')");
        sqLiteDatabase.execSQL("insert into folder_item values(2, 'ac', 'a')");
        sqLiteDatabase.execSQL("insert into folder_item values(3, 'ba', 'b')");
        sqLiteDatabase.execSQL("insert into folder_item values(4, 'bb', 'b')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /*插入item，-1插入失败*/
    public void InsertItem(Item item){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", item.getTitle());
        values.put("description", item.getDescription());
        values.put("link", item.getLink());
        values.put("pubDate", item.getPubDate());
        values.put("guid", item.getGuid());
        values.put("channel", item.getChannel());
        values.put("read", 0);

        db.insert(TABLE_NAME_ITEM, null, values);
    }

    /*
    删除名为title的item
     */
    public void DeleteItemByTitle(String title){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_ITEM, "title = ?", new String[] {title});
    }

    /*
    删除某个channel下的所有item，并删除channel
     */
    public void DeleteItemByChannel(String channel){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_ITEM, "channel = ?", new String[] {channel});
        db.delete(TABLE_NAME_CHANNEL, "title = ?", new String[] {channel});
    }

    /*
    插入channel
     */
    public void InsertChannel(Channel channel){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", channel.getTitle());
        values.put("description", channel.getDescription());
        values.put("link", channel.getLink());

        db.insert(TABLE_NAME_CHANNEL, null, values);

    }

    public void InsertFolder(Folder folder){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", folder.getName());

        db.insert(TABLE_NAME_FOLDER, null, values);
    }

    public String[] QueryItemByName(String name){
        SQLiteDatabase db = getWritableDatabase();

        String s = "select * from " + TABLE_NAME_ITEM + "where title like '%" + name + "%’";
        Cursor cursor = db.rawQuery(s, null);
        String[] re = new String[cursor.getColumnCount()];
        int i = 0;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                re[i++] = cursor.getString(1);
            }
            cursor.close();
        }
        db.close();
        return re;
    }

    /*
    查询所有文件夹
     */
    public String[] QueryAllFolder(){
        SQLiteDatabase db = getWritableDatabase();

        String s = "select * from " + TABLE_NAME_FOLDER;
        Cursor cursor = db.rawQuery(s, null);
        String[] re = new String[cursor.getColumnCount()];
        int i = 0;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                re[i++] = cursor.getString(1);
            }
            cursor.close();
        }
        db.close();
        return re;
    }

    /*
    查询name的文件夹有哪些item
     */
    public String[] QueryItemByFolder(String name){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME_FOLDER_ITEM, null, "folderName like ?", new String[]{name}, null, null, null);
        String[] re = new String[cursor.getColumnCount()];
        int i = 0;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                re[i++] = cursor.getString(1);
            }
            cursor.close();
        }
        db.close();
        return re;
    }

    /*
   删除文件夹
     */
    public void DeleteFolderByName(String name){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_FOLDER, "name = ?", new String[] {name});
    }

    /*
    文件夹重命名
     */
    public void UpdateFolderByName(String oldName, String newName){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", newName);

        db.update(TABLE_NAME_FOLDER, values, "name = ?", new String[] {oldName});
    }
}
