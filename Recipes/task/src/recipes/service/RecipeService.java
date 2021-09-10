package recipes.service;

import org.springframework.stereotype.Service;
import recipes.model.Recipe;
import recipes.model.User;
import recipes.repository.RecipeRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepo;
    public RecipeService(RecipeRepository recipeRepo) {
        this.recipeRepo = recipeRepo;
    }

    public boolean deleteRecipe(int id) {
        if (this.recipeRepo.existsById(id)) {
            this.recipeRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public void updateRecipe(int uid, int recipeId, Recipe recipe) throws RuntimeException{
        if (this.recipeRepo.existsById(recipeId)) {
            recipe.setId(recipeId);
            recipe.setDate(LocalDateTime.now());
            recipe.setUserId(uid);
            recipeRepo.save(recipe);
        }
    }

    public Recipe getRecipe(int id) {
        var recipeWrapper = recipeRepo.findById(id);
        return recipeWrapper.orElse(null);
    }

    public int createRecipe(User user, Recipe recipe) {
        recipe.setDate(LocalDateTime.now());
        recipe.setUserId(user.getId());
        Recipe res = this.recipeRepo.save(recipe);
        return res.getId();
    }

    public boolean doesRecipeExist(int id) {
        return this.recipeRepo.existsById(id);
    }

    public List<Recipe> findRecipesByName(String name) {
        List<Recipe> recipes = this.recipeRepo.getAllRecipes();
        List<Recipe> resList = new ArrayList<>();

        for(var recipe: recipes) {
            if (recipe.getName().toLowerCase().contains(name.toLowerCase())) {
               resList.add(recipe);
            }
        }
        resList.sort((o1, o2) -> -1 * o1.getDate().compareTo(o2.getDate()));
        return resList;
    }

    public List<Recipe> findRecipesByCategory(String category) {
        List<Recipe> recipes = this.recipeRepo.getAllRecipes();
        List<Recipe> resList = new ArrayList<>();

        for(var recipe: recipes) {
            if (recipe.getCategory().equalsIgnoreCase(category)) {
                resList.add(recipe);
            }
        }
        resList.sort((o1, o2) -> -1 * o1.getDate().compareTo(o2.getDate()));
        return resList;
    }

}
