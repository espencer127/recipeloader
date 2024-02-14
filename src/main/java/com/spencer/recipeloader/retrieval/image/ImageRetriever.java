package com.spencer.recipeloader.retrieval.image;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.spencer.recipeloader.retrieval.model.recipeml.ImageInfo;

@Service
public class ImageRetriever {

    private ImageRetrieverClient imgClient;

    public ImageRetriever(ImageRetrieverClient imgClient) {
        this.imgClient = imgClient;
    }

    public ImageInfo downloadImage(String url) {
        ImageInfo result = new ImageInfo();
        String desiredFileName = generateDesiredFileName();

        String home = System.getProperty("user.home");

        String localPath = home+"\\Downloads\\" + desiredFileName  + ".jpg";

        imgClient.executeRetrieval(url, localPath);

        result.setFileName(desiredFileName);
        result.setLocalPath(localPath);
        result.setUrl(url);

        return result;
    }

    private String generateDesiredFileName() {
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("America/New_York"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmmss", Locale.ENGLISH);
        
        String time = zdt.format(formatter);

        String fileName = "recipepicture" + time;
        return fileName;
    }

}
