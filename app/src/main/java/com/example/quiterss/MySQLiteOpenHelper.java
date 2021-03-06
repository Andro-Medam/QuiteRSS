package com.example.quiterss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.quiterss.bean.Channel;
import com.example.quiterss.bean.Folder;
import com.example.quiterss.bean.Folder_item;
import com.example.quiterss.bean.Item;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mySQLite.db";
    private static final String TABLE_NAME_ITEM = "item";
    private static final String TABLE_NAME_CHANNEL = "channel";
    private static final String TABLE_NAME_FOLDER = "folder";
    private static final String TABLE_NAME_FOLDER_ITEM = "folder_item";

    private static final String CREATE_TABLE_ITEM = "create table " + TABLE_NAME_ITEM + " (id integer primary key autoincrement, " +
            "title text, description text, link text, pubDate text, guid text, channel text, read integer);\n";
    private static final String CREATE_TABLE_CHANNEL = "create table " + TABLE_NAME_CHANNEL + " (id integer primary key autoincrement, " +
            "title text unique not null, description text, link text, RSSlink text unique not null);\n";
    private static final String CREATE_TABLE_FOLDER = "create table " + TABLE_NAME_FOLDER + " (id integer primary key autoincrement, " +
            "name text unique not null, description text, status text not null);\n";
    private static final String CREATE_TABLE_FOLDER_ITEM = "create table " + TABLE_NAME_FOLDER_ITEM + " (itemName text, folderName text, PRIMARY KEY(itemName,folderName));\n";

    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ITEM);
        sqLiteDatabase.execSQL(CREATE_TABLE_CHANNEL);
        sqLiteDatabase.execSQL(CREATE_TABLE_FOLDER);
        sqLiteDatabase.execSQL(CREATE_TABLE_FOLDER_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public boolean deleteDatabase(Context context, String databaseName) {
        return context.deleteDatabase(databaseName);
    }

    /*??????item???-1????????????*/
    public long InsertItem(Item item){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", item.getTitle());
        values.put("description", item.getDescription());
        values.put("link", item.getLink());
        values.put("pubDate", item.getPubDate());
        values.put("guid", item.getGuid());
        values.put("channel", item.getChannel());
        values.put("read", item.getRead());

        Log.d("AA", "insert an item");

        return db.insert(TABLE_NAME_ITEM, null, values);
    }

    /*
    ????????????title???item
     */
    public void DeleteItemByTitle(String title){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_ITEM, "title = ?", new String[] {title});
    }

    /*
    ????????????channel????????????item????????????channel
     */
    public void DeleteItemByChannel(String channel){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_ITEM, "channel = ?", new String[] {channel});
        db.delete(TABLE_NAME_CHANNEL, "title = ?", new String[] {channel});
    }

    public void DeleteFIByFolderName(String folderName){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_FOLDER_ITEM, "folderName = ?", new String[] {folderName});
    }

    /*
    ??????channel
     */
    public void InsertChannel(Channel channel){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", channel.getTitle());
        values.put("description", channel.getDescription());
        values.put("link", channel.getLink());
        values.put("RSSlink", channel.getRSSlink());

        db.insert(TABLE_NAME_CHANNEL, null, values);

    }

    public long InsertFolderItem(Folder_item folder_item){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("folderName", folder_item.getFolderName());
        values.put("itemName", folder_item.getItemName());

        return db.insert(TABLE_NAME_FOLDER_ITEM, null, values);
    }

    public long InsertFolder(Folder folder){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", folder.getName());
        values.put("status", folder.getStatus());
        values.put("description", folder.getDescription());

        return db.insert(TABLE_NAME_FOLDER, null, values);
    }

    public List<String> QueryItemByName(String name){
        SQLiteDatabase db = getWritableDatabase();

        String s = "select * from " + TABLE_NAME_ITEM + " where title like \"%" + name + "%\"";
        Cursor cursor = db.rawQuery(s, null);
        List<String> re = new ArrayList<String>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                re.add(cursor.getString(1));
            }
            cursor.close();
        }
        db.close();
        return re;
    }

    public Item QueryItem(String name){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME_ITEM, null, "title = ?", new String[]{name}, null, null, null);
        Item item = new Item();
        Log.d("xxxxxxxxxxx", "QueryItem: " + name);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                item.setTitle(cursor.getString(1));
                item.setDescription(cursor.getString(2));
            }
            Log.d("xxxxxxxxxxx", "QueryItem: " + cursor.getCount());
            cursor.close();
        }
        db.close();
        return item;
    }

    public Boolean QueryItemExist(String name){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME_ITEM, null, "title = ?", new String[]{name}, null, null, null);
        if (cursor.getCount() > 0){
            cursor.close();
            return true;
        }
        else{
            cursor.close();
            return false;
        }
    }

    /*
    ?????????????????????
     */
    public List<String> QueryAllFolder(){
        SQLiteDatabase db = getWritableDatabase();

        String s = "select * from " + TABLE_NAME_FOLDER;
        Cursor cursor = db.rawQuery(s, null);
        List<String> re = new ArrayList<String>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                re.add(cursor.getString(1));
            }
            cursor.close();
        }
        db.close();
        return re;
    }

    public List<String> QueryFolderByUserOrSys(String i){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME_FOLDER, null, "status = ?", new String[]{i}, null, null, null);
        List<String> re = new ArrayList<String>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                re.add(cursor.getString(1));
            }
            cursor.close();
        }
        db.close();
        return re;
    }

    /*
    ??????name?????????????????????item
     */
    public List<String> QueryItemByFolder(String name){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME_FOLDER_ITEM, null, "folderName like ?", new String[]{name}, null, null, null);
        List<String> re = new ArrayList<String>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                re.add(cursor.getString(0));
            }
            cursor.close();
        }
        db.close();
        return re;
    }

    /*
    ????????????link
     */
    public List<String> QueryLinkByChannel(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select RSSlink from " + TABLE_NAME_CHANNEL, null);
//        Cursor cursor = db.query(TABLE_NAME_CHANNEL, null, null, null, null, null, null);
        List<String> re = new ArrayList<String>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                re.add(cursor.getString(0));
                Log.d("aaaaaaaaaa", "QueryLinkByChannel: " + cursor.getString(0));
            }
            cursor.close();
        }
        db.close();
        return re;
    }

    /*
   ???????????????
     */
    public long DeleteFolderByName(String name){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME_FOLDER, "name = ?", new String[] {name});
    }

    /*
    ??????????????????
     */
    public long UpdateFolderByName(String oldName, String newName){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", newName);

        long x = db.update(TABLE_NAME_FOLDER, values, "name = ?", new String[] {oldName});
        if(x != -1){
            ContentValues values1 = new ContentValues();
            values1.put("folderName", newName);
            db.update(TABLE_NAME_FOLDER_ITEM, values1, "folderName = ?", new String[] {oldName});
            return 1;
        }
        else
            return -1;
    }
}
