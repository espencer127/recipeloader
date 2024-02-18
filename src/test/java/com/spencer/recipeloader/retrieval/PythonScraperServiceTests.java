package com.spencer.recipeloader.retrieval;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PythonScraperServiceTests {

    @Test
    public void givenPythonScript_whenPythonProcessExecuted_thenSuccess()
            throws IOException {

        String url = "https://www.allrecipes.com/recipe/235158/worlds-best-honey-garlic-pork-chops/";

        String line = "python C:/users/evani/code/recipeloader/src/main/resources/python/recipe-scrapers/scraper.py " + url;
        CommandLine cmdLine = CommandLine.parse(line);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);

        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(streamHandler);


        int exitCode = executor.execute(cmdLine);
        
        
        
        log.error(outputStream.toString().trim());
        
        
        //assertEquals(0, exitCode, "No errors should be detected");
        // assertEquals("Should contain script output: ", "Hello Baeldung Readers!!",
        // outputStream.toString().trim());
    }


    @Test
    public void givenPythonScript_whenPythonProcessExecuted_thenSuccess2()
            throws IOException {
        
        Runtime rt = Runtime.getRuntime();
        String url = "https://www.allrecipes.com/recipe/235158/worlds-best-honey-garlic-pork-chops/";

        String line = "python C:/users/evani/code/recipeloader/src/main/resources/python/recipe-scrapers/scraper.py " + url;


    Process proc = rt.exec(line);

    BufferedReader stdInput = new BufferedReader(new
            InputStreamReader(proc.getInputStream()));

    BufferedReader stdError = new BufferedReader(new
            InputStreamReader(proc.getErrorStream()));

// Read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

// Read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }

    }

}
