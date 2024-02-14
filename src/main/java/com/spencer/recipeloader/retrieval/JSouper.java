package com.spencer.recipeloader.retrieval;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class JSouper {

    public Document getDoc(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
