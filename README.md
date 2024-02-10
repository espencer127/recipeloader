# Graphy-Loader

This app shoves RecipeML recipes into a docker-run grocy app.

## Application process for importing a single recipe

- Read RecipeML file
- Convert XML recipe into grocy format POJO object
- API: GET the user's existing ingredients ("products")
- API: GET the user's existing quantities ("quantity_units")
- API: POST make any necessary quantities ("quantity_units")
- API: POST make any necessary ingredients ("products")
- (add that ingredient to the internal ingredient list object)
- API: POST make the recipe ("recipe")
- API: POST add each ingredient to the recipe ("recipe_pos")

## Future Enhancement: process for importing multiple recipes

- Read all files in a folder
- convert XML recipes into list of grocy format
- API: GET the user's existing ingredients
- whittle down to unique ingredients in all the recipes
- API: GET the user's existing quantities
- API: POST make any necessary quantities
- API: POST make any necessary ingredients
- (add that ingredient to the internal ingredient list object)
- API: POST make the recipe
- API: POST add each ingredient to the recipe
