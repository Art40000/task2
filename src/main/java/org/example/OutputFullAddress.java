package org.example;

import lombok.SneakyThrows;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class OutputFullAddress implements Runnable {

    private static final String OBJECTID = "OBJECTID";

    private Document document;

    private BlockingQueue<String> bufferForAddressChainsObjectId;

    private NodeList nodeList;

    public OutputFullAddress(Document document, BlockingQueue<String> bufferForAddressChainsObjectId) {
        this.document = document;
        this.bufferForAddressChainsObjectId = bufferForAddressChainsObjectId;

        nodeList = document.getElementsByTagName("OBJECT");
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            String chainObjectIds = bufferForAddressChainsObjectId.take();

            if (chainObjectIds.equals("THE_END")) {
                break;
            }

            String[] objectIds = chainObjectIds.split(",");
            for (String objectId : objectIds) {
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Element item = (Element) nodeList.item(i);

                    String objectIdAttr = item.getAttribute(OBJECTID);
                    if (objectId.equals(objectIdAttr)) {

                        String typeName = item.getAttribute("TYPENAME");
                        String name = item.getAttribute("NAME");

                        System.out.print(typeName + " " + name + " ");
                    }
                }
            }
            System.out.print("\n");
        }
    }
}