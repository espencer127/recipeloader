package com.spencer.recipeloader.image.retrieval;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

@Service
public class ImageRetrieverClient {

    public void executeRetrieval(String url, String desiredFileName) {
        int CONNECT_TIMEOUT = 10000;
        int READ_TIMEOUT = 10000;
        try {
            FileUtils.copyURLToFile(new URL(url), new File(desiredFileName), CONNECT_TIMEOUT, READ_TIMEOUT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
