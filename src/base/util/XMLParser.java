/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base.util;

import base.engine.Game;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Bailey
 */
public class XMLParser {
    
    
    public static Node loadFile(String path){
        File fXmlFile = new File(Game.Path+"/Animation/"+path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = null;
            try {
                doc = dBuilder.parse(fXmlFile);
            } catch (org.xml.sax.SAXException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            doc.getDocumentElement().normalize();
            return doc.getDocumentElement();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static void printChildNodes(Node node, int deapth){
        //Print this node information
        if(node.getNodeName().contains("#")){
            return;
        }
        for(int i = 0; i < deapth; i++){
            System.out.print(" ");
        }
        //Name
        System.out.print(node.getNodeName()+":");
        for(int i = 0; i < node.getAttributes().getLength(); i++){
            System.out.print(node.getAttributes().item(i).getNodeName()+"="+node.getAttributes().item(i).getNodeValue());
        }
        System.out.println();
        if(node.hasChildNodes()){
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                printChildNodes(node.getChildNodes().item(i), deapth+1);
            }
        }
        if(node.getNextSibling()!=null){
            printChildNodes(node.getNextSibling(), deapth);
        }
    }
    
    public static Node getFirstNodeWithName(String name, Node base){
        LinkedList<Node> nodes = new LinkedList<Node>();
        getAllNodesWithName(base, nodes);
        for(Node node:nodes){
            if(node.getNodeName().equals(name)){
                return node;
            }
        }
        return null;
    }
    
    public static Node[] getAllNodesWithName(String name, Node base){
        Node[] out = new Node[]{};
        LinkedList<Node> nodes = new LinkedList<Node>();
        getAllNodesWithName(base, nodes);
        int count = 0;
        for(Node node:nodes){
            if(node.getNodeName().equals(name)){
                count++;
            }
        }
        if(count==0){
            return out;
        }
        out = new Node[count];
        count=0;
        for(Node node:nodes){
            if(node.getNodeName().equals(name)){
                out[count] = node;
                count++;
            }
        }
        return out;
    }
    
    private static void getAllNodesWithName(Node base, LinkedList<Node> nodes){
        if(!base.getNodeName().contains("#")){
            nodes.add(base);
            if(base.hasChildNodes()){
                for(int i = 0; i < base.getChildNodes().getLength(); i++){
                    getAllNodesWithName(base.getChildNodes().item(i), nodes);
                }
            }

            if(base.getNextSibling()!=null){
                getAllNodesWithName(base.getNextSibling(), nodes);
            }
        }
    }
    
    private static boolean hasMore(Node node){
        return node.hasChildNodes()||(node.getNextSibling()==null);
    }

    
    public static String[] getAttributes(Node in){
        if(!in.hasAttributes()){
            return new String[]{""};
        }
        String[] out = new String[in.getAttributes().getLength()];
        for(int i = 0; i < out.length; i++){
            out[i] = in.getAttributes().item(i).getNodeName()+"="+in.getAttributes().item(i).getNodeValue();
        }
        return out;
    }
    
    public static String getDataFromAttribute(String id, String[] attributes){
        for(int i = 0; i < attributes.length; i++){
            if(attributes[i].startsWith(id)){
                return attributes[i].split("=")[1];
            }
        }
        return "";
    }
}
