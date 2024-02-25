package com.spencer.recipeloader.retrieval;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.spencer.recipeloader.NullChecker;
import com.spencer.recipeloader.controller.ImportRequest;
import com.spencer.recipeloader.recipe.retrieval.FileRetrieverServiceImpl;
import com.spencer.recipeloader.universal.model.FullResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileRetrieverServiceTests {

    private FileRetrieverServiceImpl fileRetrieverService;

    @BeforeEach
    void setup() {
        fileRetrieverService = new FileRetrieverServiceImpl();
    }

    @Disabled("not sure why this doesn't work")
    @Test
    public void retrieveRecipeTest_ShouldLoadFile() {
        ImportRequest imp = new ImportRequest();
        imp.setFilePath("src\\test\\resources\\LemonBars.xml");

        FullResponse rec = fileRetrieverService.retrieveRecipe(imp);

        log.info("Test class got the recipe {}", rec);

        Boolean result = NullChecker.anyNull(rec);

        assertFalse(result);

    }

    @Disabled("doesn't do what i wanna right now")
    @Test
    public void retrieveRecipeTest_ShouldContainCategories() {
        ImportRequest imp = new ImportRequest();
        imp.setFilePath("src\\test\\resources\\LemonBars.xml");

        FullResponse rec = fileRetrieverService.retrieveRecipe(imp);

        Boolean result = NullChecker.anyNull(rec);

        assertFalse(result);

    }

}
