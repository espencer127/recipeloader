package com.spencer.recipeloader.image.retrieval;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.spencer.recipeloader.universal.model.ImageInfo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageRetriever {
    
    public ImageInfo downloadImage(String url) {
        ImageInfo imgInfo = new ImageInfo();

        if (!StringUtils.isEmpty(url)) {
            imgInfo = saveImage(url);
        } else {
            log.error("Couldn't find any image URL for this recipe");
        }
        
        return imgInfo;
    }

    public ImageInfo saveImage(String url) {
        ImageRetrieverClient imgClient = new ImageRetrieverClient();
        String desiredFileName = generateDesiredFileName();
        String encodedFileName = Base64.getEncoder().encodeToString(desiredFileName.getBytes());

        String home = System.getProperty("user.home");

        String localPath = home+"\\Downloads\\" + desiredFileName;

        imgClient.executeRetrieval(url, localPath);

        return new ImageInfo(desiredFileName, encodedFileName, url, localPath);
    }

    private String generateDesiredFileName() {
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("America/New_York"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmmss", Locale.ENGLISH);
        
        String time = zdt.format(formatter);

        String fileName = "recipepicture" + time + ".jpg";
        return fileName;
    }

}
