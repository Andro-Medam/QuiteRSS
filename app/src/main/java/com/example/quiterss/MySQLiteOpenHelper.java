package com.example.quiterss;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.quiterss.bean.Channel;
import com.example.quiterss.bean.Folder;
import com.example.quiterss.bean.Item;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mySQLite.db";
    private static final String TABLE_NAME_ITEM = "item";
    private static final String TABLE_NAME_CHANNEL = "channel";
    private static final String TABLE_NAME_FOLDER = "folder";

    private static final String CREATE_TABLE_ITEM = "create table " + TABLE_NAME_ITEM + " (id integer primary key autoincrement, " +
            "title text, description text, link text, pubDate text, guid text, channel text, folder text);\n";
    private static final String CREATE_TABLE_CHANNEL = "create table " + TABLE_NAME_CHANNEL + " (id integer primary key autoincrement, " +
            "title text, description text, link text);\n";
    private static final String CREATE_TABLE_FOLDER = "create table " + TABLE_NAME_FOLDER + " (id integer primary key autoincrement, " +
            "name text);\n";


    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ITEM);
        sqLiteDatabase.execSQL(CREATE_TABLE_CHANNEL);
        sqLiteDatabase.execSQL(CREATE_TABLE_FOLDER);
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
        values.put("folder", item.getFolder());

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
