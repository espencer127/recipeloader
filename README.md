# Graphy-Loader

This app shoves RecipeML recipes into a docker-run grocy app.

## In Progress

- improve end-user experience; support command line app

## Future Enhancements

- support adding RecipeML categories to the recipes saved in grocy
- if the category userval doesn't exist in grocy first, make it
- api/UI to do same 'scrape functionality' but for local files
- Import recipes from websites (besides allrecipes)
- Return back to the UI user the stuff we made?
- UI interface for importing files
- When importing from AllRecipes, map the prep/cook/total time to user fields
- Support for \<ing-div\> tag in ingredients block
- scrape images from websites
- the 'ScrapedRecipe' page should show you the image in a box, and optionally provide you a way to edit the image path
- add images to recipes in grocy
- refactor the process for importing multiple recipes (ie whittle down to unique ingredients in all the recipes), not just one recipe at a time

## Sundry Stuff

- Lombok and Jackson don't play nice? Adding the @Builder annotation to certain classes (allrecipesinstructions/recipe etc) causes errors to throw when deserializing. I'd like to use the builder.
