package org.example;

import lombok.SneakyThrows;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class GetObjectIDsByTypeName implements Runnable{

    private Document document;
    private String typeName;
    private BlockingQueue<String> bufferForObjectIdHavingTypeName;

    private NodeList nodeList;

    public GetObjectIDsByTypeName(Document document, String typeName, BlockingQueue<String> bufferForObjectIdHavingTypeName) {
        this.document = document;
        this.typeName = typeName;
        nodeList = document.getElementsByTagName("OBJECT");

        this.bufferForObjectIdHavingTypeName = bufferForObjectIdHavingTypeName;
    }

    @SneakyThrows
    @Override
    public void run() {

        for (int i = 0; i < nodeList.getLength(); i++) {

            Element item = (Element) nodeList.item(i);

            if (item.getAttribute("TYPENAME").equals(typeName)) {

                bufferForObjectIdHavingTypeName.put(item.getAttribute("OBJECTID"));
            }
        }
        bufferForObjectIdHavingTypeName.put("THE_END");
    }
}
