# Graphy-Loader

This app shoves RecipeML recipes into a docker-run grocy app.

## PROCESS FOR SINGLE RECIPE

- convert XML recipe into grocy format
- API: GET the user's existing ingredients
- API: GET the user's existing quantities
- API: POST make any necessary quantities
- API: POST make any necessary ingredients
- (add that ingredient to the internal ingredient list object)
- API: POST make the recipe
- API: POST add each ingredient to the recipe

## PROCESS FOR MULTIPLE RECIPES

- convert XML recipes into list of grocy format
- API: GET the user's existing ingredients
- whittle down to unique ingredients in all the recipes
- API: GET the user's existing quantities
- API: POST make any necessary quantities
- API: POST make any necessary ingredients
- (add that ingredient to the internal ingredient list object)
- API: POST make the recipe
- API: POST add each ingredient to the recipe
