package com.example.quiterss;

import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.Console;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Dom_analyze {

    public void url_analyze(String url){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(url);
            NodeList itemlist = document.getElementsByTagName("item");
            for(int i = 0; i < itemlist.getLength(); i++) {
                System.out.println("----------"+(i+1) + ":");
                Node n = itemlist.item(i);
                String title = null, description= null, link= null, pubDate= null, guid = null;
                NamedNodeMap attrs = n.getAttributes();
                //System.out.println("有" + attrs.getLength() + "个属性：");
                NodeList childNodes = n.getChildNodes();
                //System.out.println("第" + (i+1) + "本书共有" + childNodes.getLength() + "个子节点");
                for (int k = 0; k < childNodes.getLength(); k++) {
                    //区分出text类型的node以及element类型的node
                    if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                        switch (childNodes.item(k).getNodeName()) {
                            case "title":
                                title = childNodes.item(k).getTextContent();
                                //System.out.println("---" + title);
                                break;
                            case "description":
                                description = childNodes.item(k).getTextContent();
                                break;
                            case "link":
                                link = childNodes.item(k).getTextContent();
                                break;
                            case "pubDate":
                                pubDate = childNodes.item(k).getTextContent();
                                break;
                            case "guid":
                                guid = childNodes.item(k).getTextContent();
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
            }


        }catch (Exception e){
            //Toast.makeText(getActivity(), "you clicked more!", Toast.LENGTH_SHORT).show();
        }
    }
}
