package com.spencer.recipeloader.grocy.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.spencer.recipeloader.grocy.model.Product;
import com.spencer.recipeloader.grocy.model.QuantityUnit;
import com.spencer.recipeloader.grocy.model.Recipe;
import com.spencer.recipeloader.grocy.model.RecipesPos;
import com.spencer.recipeloader.mapper.RecipeMapper;
import com.spencer.recipeloader.recipeml.model.Ing;
import com.spencer.recipeloader.recipeml.model.RecipeDto;
import com.spencer.recipeloader.recipeml.service.RecipeMLService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GrocyService {

    String recipeFilePath;
    String recipesFolderPath;

    public RecipeMLService recipeMLService;
    public RecipeMapper recipeMapper;
    public GrocyClient grocyClient;

    public GrocyService(@Value("${paths.recipe-file}") String recipeFilePath, @Value("${paths.recipes-folder}") String recipesFolderPath, RecipeMLService recipeMLService, RecipeMapper recipeMapper, GrocyClient grocyClient) {
        this.recipeFilePath = recipeFilePath;
        this.recipesFolderPath = recipesFolderPath;
        this.recipeMLService = recipeMLService;
        this.recipeMapper = recipeMapper;
        this.grocyClient = grocyClient;
    }

    /**
     * Does the whole shebang:
     * <ul>
     * Read RecipeML file
     * </ul>
     * <li>Convert XML recipe into grocy format POJO object</li>
     * <li>API: GET the user's existing ingredients ("products")</li>
     * <li>API: GET the user's existing quantities ("quantity_units")</li>
     * <li>API: POST make any necessary quantities ("quantity_units")</li>
     * <li>API: POST make any necessary ingredients ("products")</li>
     * <li>(add that ingredient to the internal ingredient list object)</li>
     * <li>API: POST make the recipe ("recipe")</li>
     * <li>API: POST add each ingredient to the recipe ("recipe_pos")</li>
     */
    public void execute() {
        List<File> files = new ArrayList<>();

        if (StringUtils.isNotBlank(recipesFolderPath)) {
            File folder = new File(recipesFolderPath);
            File[] listOfFiles = folder.listFiles();
            files.addAll(Arrays.asList(listOfFiles));
        } else {
            File file = new File(recipeFilePath);
            files.add(file);
        }

        for (File file : files) {
            RecipeDto recipeDto = recipeMLService.retrieveRecipe(file).getRecipeml().getRecipe();

            parseDtoAndSendToGrocy(recipeDto);
        }
    }

    public void parseDtoAndSendToGrocy(RecipeDto recipeDto) {
        Recipe recipe = recipeMapper.toRecipe(recipeDto);

        log.debug("got the recipe {}", recipe);

        updateQuantityMasterData(recipeDto);

        List<QuantityUnit> updatedUserQuantityUnits = grocyClient.getQuantityUnits();

        updateProductMasterData(recipeDto, updatedUserQuantityUnits);

        List<Product> updatedUserIngredients = grocyClient.getProducts();

        // add recipe (all you need to include is name and description)
        // get back object w/ id attached
        grocyClient.createRecipes(recipe);

        // update the recipe_pos table; add records w/ addl mappings
        // for each ingredient in the recipe, make a "recipe_pos" record
        List<RecipesPos> recipePosWeNeedToAdd = generateRecipePosList(recipeDto, recipe, updatedUserIngredients, updatedUserQuantityUnits);

        log.debug("we're gonna add the recipePos objects: {}", recipePosWeNeedToAdd);

        grocyClient.createRecipePos(recipePosWeNeedToAdd);
    }

    /**
     * Figure out which ingredients you need to add and then do it. Doing so relies
     * on knowledge about the id's of the quantities in the master data store.
     * 
     * @param recipeDto
     * @param updatedUserQuantityUnits
     */
    private void updateProductMasterData(RecipeDto recipeDto, List<QuantityUnit> updatedUserQuantityUnits) {
        List<Product> existingUserIngredients = grocyClient.getProducts();

        List<Product> addedProducts = new ArrayList<>();

        log.debug("got the existing ingredients {}", existingUserIngredients);

        List<String> ingredientNamesWeNeedToAdd = findIngredientsWeNeedToAdd(recipeDto, existingUserIngredients);
        // FIXME: products need the qty's before we add them - after we do that, should
        // fix other fixme below?

        List<Product> ingredientsWeNeedToAdd = addQtyUnitsToIngredients(ingredientNamesWeNeedToAdd,
                updatedUserQuantityUnits, recipeDto);

        if (ingredientsWeNeedToAdd.size() > 0) {
            addedProducts = grocyClient.createProducts(ingredientsWeNeedToAdd);
        }

        List<Product> updatedProductsList = new ArrayList<>();
        updatedProductsList.addAll(existingUserIngredients);
        updatedProductsList.addAll(addedProducts);
    }

    /**
     * Is this method optimal? no. Does it work? I hope it will.
     * 
     * @param ingredientNamesWeNeedToAdd
     * @param updatedUserQuantityUnits
     * @param recipeDto
     * @return
     */
    private List<Product> addQtyUnitsToIngredients(List<String> ingredientNamesWeNeedToAdd,
            List<QuantityUnit> updatedUserQuantityUnits, RecipeDto recipeDto) {

        List<Ing> ingredientsInRecipe = Arrays.asList(recipeDto.getIngredients().getIng());

        List<Product> result = new ArrayList<>();

        for (String ing : ingredientNamesWeNeedToAdd) {
            Optional<Ing> matchingIngMaybe = ingredientsInRecipe.stream()
            .filter(x -> StringUtils.equalsIgnoreCase(x.getItem(), ing))
            .findFirst();

            log.info("Ing {}", ing);
            Ing matchingIng = matchingIngMaybe.get();

            Optional<QuantityUnit> unitToAddToProductMaybe = updatedUserQuantityUnits.stream()
                    .filter(x -> StringUtils.equalsIgnoreCase(matchingIng.getAmt().getUnit(), x.getName()))
                    .findFirst();

            QuantityUnit unitToAddToProduct = new QuantityUnit();

            if (unitToAddToProductMaybe.isPresent()) {
                unitToAddToProduct = unitToAddToProductMaybe.get();
            } else {
                // check if there's a qty in "updatedQtyUnits" where the ing.amt.unit + s =
                // x.getName. If YES, then that's the 'matchingQuantityUnit'. If not, error log
                // + continue with the next ingredient
                Optional<QuantityUnit> unitToAddToProductMaybe2 = updatedUserQuantityUnits.stream()
                        .filter(x -> StringUtils.equalsIgnoreCase(matchingIng.getAmt().getUnit(), madePlural(x.getName())))
                        .findFirst();
                if (unitToAddToProductMaybe2.isPresent()) {
                    QuantityUnit unitToAddToProduct2 = new QuantityUnit();
                    unitToAddToProduct2 = unitToAddToProductMaybe2.get();
                    unitToAddToProduct = unitToAddToProduct2;
                } else {
                    log.error("we in trouble...this ing doesn't have any quantity unit?", matchingIng);
                    continue;
                }
            }

            log.info("For ingredient {} the unitToAddToProduct is {}", ing, unitToAddToProduct);

            // TODO: do we gotta worry about plurals up there? ^
            Integer qtyId = unitToAddToProduct.getId();

            log.info("For ingredient {} we're gonna make a product with qtyId {}", ing, qtyId);

            Product product = recipeMapper.toProductPostBody(ing, qtyId);

            result.add(product);
        }

        return result;
    }

    /**
     * Figure out which qty's you need to add and then do it
     * 
     * @param recipeDto
     */
    private void updateQuantityMasterData(RecipeDto recipeDto) {
        List<QuantityUnit> existingUserQuantityUnits = grocyClient.getQuantityUnits();

        log.debug("got the existing quantity units: {}", existingUserQuantityUnits);

        List<String> quantitiesWeNeedToAdd = findQuantitiesWeNeedToAdd(recipeDto, existingUserQuantityUnits);

        // add quantities+products
        List<QuantityUnit> addedQuantityUnits = new ArrayList<>();

        if (quantitiesWeNeedToAdd.size() > 0) {
            addedQuantityUnits = grocyClient.createQuantityUnits(quantitiesWeNeedToAdd);
        }

        List<QuantityUnit> updatedQuantityUnitsList = new ArrayList<>();
        updatedQuantityUnitsList.addAll(existingUserQuantityUnits);
        updatedQuantityUnitsList.addAll(addedQuantityUnits);
    }

    /**
     * For each ingredient in the recipe, make a "recipe_pos" record w/ the
     * associated product's product_id, the recipe's recipe_id, amt and the
     * associated quantity unit's qu_id and the recipe ingredient's amount.
     * 
     * @param recipe
     * @param updatedProductsList
     * @return
     */
    public List<RecipesPos> generateRecipePosList(RecipeDto recipeDto, Recipe recipe,
            List<Product> updatedProductsList, List<QuantityUnit> updatedQuantityUnits) {

        List<RecipesPos> finalResult = new ArrayList<>();

        List<Ing> neededIngredients = Arrays.asList(recipeDto.getIngredients().getIng());

        for (Ing ing : neededIngredients) {
            Product matchingProduct = updatedProductsList.stream()
                    .filter(x -> StringUtils.equalsIgnoreCase(ing.getItem().trim(), x.getName()))
                    .findFirst().get();

            Optional<QuantityUnit> matchingQuantityUnitOpt = updatedQuantityUnits.stream()
                    .filter(x -> (
                        StringUtils.equalsIgnoreCase(ing.getAmt().getUnit(), x.getName()) ||
                        StringUtils.equalsIgnoreCase(ing.getAmt().getUnit(), madePlural(x.getName()))
                        )
                    ).findFirst();

            QuantityUnit matchingQuantityUnit = new QuantityUnit();

            if (matchingQuantityUnitOpt.isPresent()) {
                matchingQuantityUnit = matchingQuantityUnitOpt.get();
            } else {
/*                 // check if there's a qty in "updatedQtyUnits" where the ing.amt.unit + s =
                // x.getName. If YES, then that's the 'matchingQuantityUnit'. If not, error log
                // + continue with the next ingredient
                Optional<QuantityUnit> matchingQuantityUnitOpt2 = updatedQuantityUnits.stream()
                        .filter(x -> 
                        )
                        .findFirst();
                if (matchingQuantityUnitOpt2.isPresent()) {
                    matchingQuantityUnit = matchingQuantityUnitOpt2.get();
                } else { */
                    log.error(
                            "we in trouble...this ing doesn't have any quantity unit? Can't add it to the recipe {} for {}: {}",
                            recipe.getId(), recipe.getName(), ing);
                    continue;
                //}
            }

            String neededIngQty = ing.getAmt().getQty();

            RecipesPos postBody = new RecipesPos();

            try {
                Integer neededInt = Integer.valueOf(neededIngQty);
                postBody = recipeMapper.toRecipePosPostBodyWithAmount(matchingProduct.getId(), recipe.getId(),
                        neededInt, matchingQuantityUnit.getId());
            } catch (NumberFormatException e) {
                try {
                    postBody = recipeMapper.toRecipePosPostBodyWithVariableAmount(matchingProduct.getId(), recipe.getId(),
                            parse(neededIngQty), neededIngQty, matchingQuantityUnit.getId());
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            finalResult.add(postBody);
        }

        return finalResult;
    }

    //FIXME: need to be able to handle strings like 1 1/2
    /**
     * Returns a whole number for the inputted ratio or decimal, rounded up.
     * 
     * @param ratio
     * @return
     * @throws Exception 
     */
    public Integer parse(String ratio) throws Exception {
        Integer returnValue = 0;
        Integer nominator = 0;
        Integer denominator = 0;
        //if there's a space, split out the bit before it
        Integer whole = 0;

        if (StringUtils.equals(ratio, "")) {
            return 1;
        }

        if (ratio.contains(" ")) {
            String[] theParts = StringUtils.split(ratio);

            if (theParts.length != 2) {
                throw new Exception("can't handle the qty " + ratio);
            }

            whole = Integer.valueOf(theParts[0]);
            ratio = theParts[1];
        }

        if (ratio.contains("/")) {
            String[] rat = ratio.split("/");
            nominator = Integer.parseInt(rat[0]);
            denominator = Integer.parseInt(rat[1]);

            if ((whole != 0) && (denominator != 0)) {
                Integer addlValue = whole * denominator;
                nominator = nominator + addlValue;
            }

            returnValue = Math.ceilDiv(nominator, denominator);
        } else {
            returnValue = Integer.parseInt(Double.toString(Math.ceil(Double.parseDouble(ratio))));
        }

        return returnValue;
    }

    private List<String> findQuantitiesWeNeedToAdd(RecipeDto recipeDto, List<QuantityUnit> existingUserQuantityUnits) {

        List<Ing> ingredients = Arrays.asList(recipeDto.getIngredients().getIng());

        List<String> neededQuantityUnits = ingredients
                .stream().map(x -> x.getAmt().getUnit().trim().toLowerCase())
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(existingUserQuantityUnits)) {
            return neededQuantityUnits;
        }

        List<String> existingUnits = existingUserQuantityUnits
                .stream()
                .map(x -> x.getName().trim().toLowerCase())
                .collect(Collectors.toList());

        existingUnits.addAll(existingUserQuantityUnits.stream()
                .map(x -> x.getName_plural().trim().toLowerCase())
                .collect(Collectors.toList()));

        neededQuantityUnits.removeIf(StringUtils::isEmpty);

        removePlurals(neededQuantityUnits);

        neededQuantityUnits.removeAll(existingUnits);

        // TODO: need to be smarter here...if there's an existing "teaspoons" and the
        // recipe calls for "teaspoon", we shouldn't try to add "teaspoon" to the db

        List<String> dedupedList = new ArrayList<>(
                new HashSet<>(neededQuantityUnits));

        return dedupedList;
    }

    //TODO: need to match on both qty and name
    public List<String> findIngredientsWeNeedToAdd(RecipeDto recipeDto, List<Product> existingUserIngredients) {
        List<String> existingIngredients = existingUserIngredients
                .stream().map(x -> x.getName().trim().toLowerCase())
                .collect(Collectors.toList());

        List<Ing> neededIngredients = Arrays.asList(recipeDto.getIngredients().getIng());

        List<String> neededIngredientStrings = neededIngredients
                .stream().map(x -> x.getItem().trim().toLowerCase())
                .collect(Collectors.toList());

        neededIngredientStrings.removeAll(existingIngredients);

        // TODO: need to be smarter here...if there's an existing "almonds" and the
        // recipe calls for "almond", we shouldn't try to add "almond" to the db

        removePlurals(neededIngredientStrings);

        List<String> dedupedList = new ArrayList<>(
                new HashSet<>(neededIngredientStrings));

        return dedupedList;
    }

    /**
     * For example, if a list contains both "teaspoon" and "teaspoons", it'll remove
     * the "teaspoons"
     * 
     * @param neededItems
     */
    private void removePlurals(List<String> neededItems) {
        neededItems.removeIf(x -> neededItems.stream().anyMatch(y -> StringUtils.equals(y, StringUtils.chop(x))));

        neededItems.removeIf(x -> neededItems.stream().anyMatch(y -> StringUtils.equals(x, StringUtils.chop(y))));
    }

    /**
     * Adds an 's' to the end of a word.
     * 
     * @param word
     * @return word + s
     */
    private String madePlural(String word) {
        String pluralWord = word + "s";
        return pluralWord;
    }

}
