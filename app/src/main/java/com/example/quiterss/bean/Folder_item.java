package com.example.quiterss.bean;

public class Folder_item {
    private String itemName;
    private String folderName;

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
                ", itemName='" + itemName + '\'' +
                ", folderName='" + folderName + '\'' +
                '}';
    }
}
