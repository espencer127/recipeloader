package com.spencer.recipeloader.retrieval.image;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import com.spencer.recipeloader.retrieval.JSouper;
import com.spencer.recipeloader.retrieval.model.scraper.ImageInfo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageRetriever {

    private ImageRetrieverClient imgClient;
    JSouper jsouper;

    public ImageRetriever(ImageRetrieverClient imgClient, JSouper jsouper) {
        this.imgClient = imgClient;
        this.jsouper = jsouper;
    }
    
    public ImageInfo downloadImage(String url) {

        ImageInfo imgInfo = new ImageInfo();
        
        try {
            Document doc;
            doc = jsouper.getDoc(url);

            String imgSrc = doc.select("img").first().attr("data-src");

            if (!StringUtils.isEmpty(imgSrc)) {
                imgInfo = saveImage(imgSrc);
            } else {
                log.error("Couldn't find any image URL for this recipe");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return imgInfo;
    }

    private ImageInfo saveImage(String url) {
        ImageInfo result = new ImageInfo();
        String desiredFileName = generateDesiredFileName();
        String encodedFileName = Base64.getEncoder().encodeToString(desiredFileName.getBytes());

        String home = System.getProperty("user.home");

        String localPath = home+"\\Downloads\\" + desiredFileName;

        imgClient.executeRetrieval(url, localPath);

        result.setBase64FileName(encodedFileName);
        result.setFileName(desiredFileName);
        result.setLocalPath(localPath);
        result.setUrl(url);

        return result;
    }

    private String generateDesiredFileName() {
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("America/New_York"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmmss", Locale.ENGLISH);
        
        String time = zdt.format(formatter);

        String fileName = "recipepicture" + time + ".jpg";
        return fileName;
    }

}
