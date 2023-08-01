package org.example;

import lombok.SneakyThrows;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final String HIERARCHY_FILE_PATH = "AS_ADM_HIERARCHY.xml";
    private static final String ADDR_FILE_PATH = "AS_ADDR_OBJ.XML";
    private static final int BUFFER_HAVING_TYPE_NAME_SIZE = 100;
    private static final int BUFFER_ADDRESS_SIZE = 100;

    @SneakyThrows
    public static void main(String[] args) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documentHierarchy = builder.parse(new File(HIERARCHY_FILE_PATH));
        Document documentAddress = builder.parse(new File(ADDR_FILE_PATH));

        BlockingQueue<String> sharedBufferForObjectIdHavingTypeName = new ArrayBlockingQueue<>(BUFFER_HAVING_TYPE_NAME_SIZE);
        BlockingQueue<String> sharedBufferForAddressChainsObjectId = new ArrayBlockingQueue<>(BUFFER_ADDRESS_SIZE);

        GetObjectIDsByTypeName getObjectIDsByTypeName =
                new GetObjectIDsByTypeName(documentAddress, "проезд", sharedBufferForObjectIdHavingTypeName);
        GetAddressChainsObjectIdByObjectsId getAddressChainsObjectIdByObjectsId =
                new GetAddressChainsObjectIdByObjectsId(documentHierarchy, sharedBufferForObjectIdHavingTypeName,
                        sharedBufferForAddressChainsObjectId);
        OutputFullAddress outputFullAddress =
                new OutputFullAddress(documentAddress, sharedBufferForAddressChainsObjectId);

        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.submit(getObjectIDsByTypeName);
        executor.submit(getAddressChainsObjectIdByObjectsId);
        executor.submit(outputFullAddress);

        executor.shutdown();
    }

}
