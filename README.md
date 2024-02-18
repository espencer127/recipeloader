# Graphy-Loader

This app shoves RecipeML recipes into a docker-run grocy app.

## Current Features

- Thymeleaf UI interface for users to scrape from a website, cleanse the response object, and then send it to Grocy
- API interfaces for the same capabilities
- Scraping process to save images to local 'Downloads' folder, upload image to grocy and associates it to the recipe

## In Progress

- Scraping process captures categories and attaches them to the recipes saved in grocy
- Same with cook times
- Same with images
- leverage python scraper wild west mode

## Future Enhancements

- resolve issues with product qty's and qty conversion when uploading to grocy
- if the category userval doesn't exist in grocy first, make it
- api/UI to do same 'scrape functionality' but for local files
- Import recipes from websites (besides allrecipes)
- Return back to the UI user the stuff we made?
- UI interface for importing files
- When importing from AllRecipes, map the prep/cook/total time to user fields
- Support for \<ing-div\> tag in ingredients block
- the 'ScrapedRecipe' page should show you the image in a box, and optionally provide you a way to edit the image path
- refactor the process for importing multiple recipes (ie whittle down to unique ingredients in all the recipes), not just one recipe at a time

## Sundry Stuff

- Lombok and Jackson don't play nice? Adding the @Builder annotation to certain classes (allrecipesinstructions/recipe etc) causes errors to throw when deserializing. I'd like to use the builder.
