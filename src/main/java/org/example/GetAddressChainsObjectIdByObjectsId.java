package org.example;

import lombok.SneakyThrows;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;


public class GetAddressChainsObjectIdByObjectsId implements Runnable {
    private static final String OBJECTID = "OBJECTID";

    private static final String PARENTOBJID = "PARENTOBJID";

    private Document document;

    private BlockingQueue<String> bufferForObjectIdHavingTypeName;

    private BlockingQueue<String> bufferForAddressChainsObjectId;

    private NodeList nodeList;

    public GetAddressChainsObjectIdByObjectsId(Document document, BlockingQueue<String> bufferForObjectIdHavingTypeName,
                                               BlockingQueue<String> bufferForAddressChainsObjectId) {
        this.document = document;
        this.bufferForObjectIdHavingTypeName = bufferForObjectIdHavingTypeName;
        this.bufferForAddressChainsObjectId = bufferForAddressChainsObjectId;

        nodeList = document.getElementsByTagName("ITEM");
    }

    @SneakyThrows
    @Override
    public void run() {

        while(true ) {
            StringBuilder res = new StringBuilder();
            String objectId = bufferForObjectIdHavingTypeName.take();
            if( objectId.equals("THE_END")) {
                break;
            }
            for (int i = 0; i < nodeList.getLength(); i++) {

                Element item = (Element) nodeList.item(i);

                boolean flat = false;

                do {
                    flat = false;
                    String objectIdAttr = item.getAttribute(OBJECTID);
                    if (objectIdAttr.equals(objectId)) {
                        res.append(objectId).append(",");
                        objectId = item.getAttribute(PARENTOBJID);
                        flat = true;
                        i = 0;
                    }
                } while (flat);
            }
            res.append(objectId);
            bufferForAddressChainsObjectId.put(res.toString());
        }
        bufferForAddressChainsObjectId.put("THE_END");
    }
}


