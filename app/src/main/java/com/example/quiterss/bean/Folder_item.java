package com.example.quiterss.bean;

public class Folder_item {
    private int id;
    private String itemName;
    private String folderName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public String toString() {
        return "Folder_item{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", folderName='" + folderName + '\'' +
                '}';
    }
}
