package com.example.quiterss;

import android.util.Log;

import com.example.quiterss.bean.Channel;
import com.example.quiterss.bean.Folder;
import com.example.quiterss.bean.Folder_item;
import com.example.quiterss.bean.Item;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class SaxHelper extends DefaultHandler {
    private Item item;
    private ArrayList<Item> items;
    private Channel channel;
    private Folder folder;
    private Folder_item folder_item;
    private ArrayList<Folder_item> folder_items;
    private String tagName = null;
    private String folder_title = null;
    private Boolean bl = false;

    @Override
    public void startDocument() throws SAXException {
        this.items = new ArrayList<Item>();
        this.channel = new Channel();
        this.folder = new Folder();
        this.folder_items = new ArrayList<Folder_item>();
        Log.d("----------", "startDocument:init items");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(localName.equals("item")){
            item = new Item();
            folder_item = new Folder_item();
            bl = true;
        }else if (localName.equals("channel")){
            bl = false;
        }
        this.tagName = localName;
    }

    /*读取到内容*/
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.tagName != null) {
            String data = new String(ch, start, length);
            if (bl) {
                switch (this.tagName) {
                    case "title":
                        this.item.setTitle(data);
                        this.folder_item.setFolderName(folder_title);
                        this.folder_item.setItemName(data);
                        Log.d("TAG", "characters: " +data);
                        break;
                    case "description":
                        this.item.setDescription(data);
                        break;
                    case "link":
                        this.item.setLink(data);
                        break;
                    case "pubDate":
                        this.item.setPubDate(data);
                        break;
                    case "guid":
                        this.item.setGuid(data);
                        break;
                    default:
                        break;
                }
            }else {
                switch (this.tagName) {
                    case "title":
                        this.channel.setTitle(data);
                        folder_title = data;
                        this.folder.setName(data);
                        break;
                    case "description":
                        this.channel.setDescription(data);
                        break;
                    case "link":
                        this.channel.setLink(data);
                        break;
                    default:
                        break;
                }
            }
        }
    }


    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(localName.equals("item")){
            item.setRead(0);
            this.items.add(item);
            Log.d("aaaaaaaaaaaaaaa", "endElement: 解析了一条数据");
            this.folder_items.add(folder_item);
            item = null;
            folder_item = null;
        }
        this.tagName = null;
    }

    @Override
    public void endDocument() throws SAXException {
        bl = false;
        Log.d("----------------", "size:" + folder_items.size());
        super.endDocument();
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public Channel getChannel() {
        return channel;
    }

    public Folder getFolder() {
        return folder;
    }

    public ArrayList<Folder_item> getFolder_items() {
        return folder_items;
    }
}
