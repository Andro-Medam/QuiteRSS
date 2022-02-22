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
            "title text, description text, link text, RSSlink text unique not null);\n";
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
    public int InsertItem(Item item){
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

        return (int) db.insert(TABLE_NAME_ITEM, null, values);
    }


    public void InsertItemFromURL(String url){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(url);
            NodeList itemList = document.getElementsByTagName("item");
            NodeList channelTitle = document.getElementsByTagName("title");
            NodeList channelDesc = document.getElementsByTagName("description");
            NodeList channelLink = document.getElementsByTagName("link");

            String folderTitle = channelTitle.item(0).getFirstChild().getNodeValue();
            String folderDesc = channelDesc.item(0).getFirstChild().getNodeValue();
            String folderLink = channelLink.item(0).getFirstChild().getNodeValue();

            Channel channel = new Channel();
            channel.setTitle(folderTitle);
            channel.setDescription(folderDesc);
            channel.setLink(folderLink);
            InsertChannel(channel);

            Folder folder = new Folder();
            folder.setName(folderTitle);
            InsertFolder(folder);

            for(int i = 0; i < itemList.getLength(); i++) {
                Item item = new Item();
                Node n = itemList.item(i);
                NamedNodeMap attrs = n.getAttributes();
                NodeList childNodes = n.getChildNodes();
                item.setRead(0);

                String title = new String();
                for (int k = 0; k < childNodes.getLength(); k++) {
                    //区分出text类型的node以及element类型的node
                    if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                        switch (childNodes.item(k).getNodeName()) {
                            case "title":
                                title = childNodes.item(k).getTextContent();
                                item.setTitle(title);
                                break;
                            case "description":
                                item.setDescription(childNodes.item(k).getTextContent());
                                break;
                            case "link":
                                item.setLink(childNodes.item(k).getTextContent());
                                break;
                            case "pubDate":
                                item.setPubDate(childNodes.item(k).getTextContent());
                                break;
                            case "guid":
                                item.setGuid(childNodes.item(k).getTextContent());
                                break;
                            default:
                                break;
                            //获取了element类型节点的节点名
                            //System.out.print("第" + (k + 1) + "个节点的节点名：" + childNodes.item(k).getNodeName());
                            //获取了element类型节点的节点值
                            // System.out.println("--节点值是：" + childNodes.item(k).getFirstChild().getNodeValue());
                            //System.out.println("--节点值是：" + childNodes.item(k).getTextContent());
                        }
                    }
                }
                InsertItem(item);

                Folder_item folder_item = new Folder_item();
                folder_item.setFolderName(folderTitle);
                folder_item.setItemName(title);
                InsertFolderItem(folder_item);
            }
        }catch (Exception e){
            //Toast.makeText(getActivity(), "you clicked more!", Toast.LENGTH_SHORT).show();
        }

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
        values.put("RSSlink", channel.getRSSlink());

        db.insert(TABLE_NAME_CHANNEL, null, values);

    }

    public void InsertFolderItem(Folder_item folder_item){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("folderName", folder_item.getFolderName());
        values.put("itemName", folder_item.getItemName());

        db.insert(TABLE_NAME_FOLDER_ITEM, null, values);
    }

    public void InsertFolder(Folder folder){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", folder.getName());

        long z = db.insert(TABLE_NAME_FOLDER, null, values);
        if (z != -1)
            Log.d("addFolder", "add a folder:" + folder.getName());
    }

    public List<String> QueryItemByName(String name){
        SQLiteDatabase db = getWritableDatabase();

        String s = "select * from " + TABLE_NAME_ITEM + " where title like \"%" + name + "%\"";
        Cursor cursor = db.rawQuery(s, null);
        List<String> re = new ArrayList<String>();
        int i = 0;
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
    查询所有文件夹
     */
    public List<String> QueryAllFolder(){
        SQLiteDatabase db = getWritableDatabase();

        String s = "select * from " + TABLE_NAME_FOLDER;
        Cursor cursor = db.rawQuery(s, null);
        List<String> re = new ArrayList<String>();
        int j = 0;
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
    查询name的文件夹有哪些item
     */
    public List<String> QueryItemByFolder(String name){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME_FOLDER_ITEM, null, "folderName like ?", new String[]{name}, null, null, null);
        List<String> re = new ArrayList<String>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                re.add(cursor.getString(1));
                Log.d("test", String.valueOf(cursor.getString(1)));
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
