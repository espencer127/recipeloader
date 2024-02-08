package com.spencer.recipeloader.grocy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spencer.recipeloader.grocy.service.GrocyService;

@SpringBootTest
public class GrocyServiceTests {

    @Autowired
    private GrocyService grocyService;

    @Test
    public void runGrocyService() {
        grocyService.execute();
    }

}
