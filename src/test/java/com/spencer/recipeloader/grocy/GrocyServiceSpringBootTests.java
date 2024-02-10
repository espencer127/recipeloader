package com.spencer.recipeloader.grocy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spencer.recipeloader.grocy.service.GrocyService;

@SpringBootTest
@Disabled("this will only work if you've got grocy running locally")
public class GrocyServiceSpringBootTests {

    @Autowired
    private GrocyService grocyService;

    @Test
    public void runGrocyService() {
        grocyService.execute();
    }

}
