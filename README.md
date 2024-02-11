# Graphy-Loader

This app shoves RecipeML recipes into a docker-run grocy app.

## In Progress

- improve end-user experience; support command line app

## Future Enhancements

- support adding RecipeML categories to the recipes
- Import recipes from websites
- When importing from AllRecipes, map the prep/cook/total time to user fields
- Support for \<ing-div\> tag in ingredients block
- refactor the process for importing multiple recipes (ie whittle down to unique ingredients in all the recipes), not just one recipe at a time

## Sundry Stuff

- Lombok and Jackson don't play nice? Adding the @Builder annotation to certain classes (allrecipesinstructions/recipe etc) causes errors to throw when deserializing. I'd like to use the builder.
