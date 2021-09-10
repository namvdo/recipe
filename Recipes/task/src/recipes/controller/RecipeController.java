package recipes.controller;

import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import recipes.model.Recipe;
import recipes.model.User;
import recipes.service.RecipeService;
import recipes.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Validated
public class RecipeController {

    enum AcceptedParam {
        CATEGORY,
        NAME
    }

    private final RecipeService recipeService;
    private final UserService userService;

    public RecipeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable @Min(1) int id) {
        var recipe = this.recipeService.getRecipe(id);
        if (recipe != null) {
            return ResponseEntity.ok(recipe);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<String> createNewRecipe(@Valid @RequestBody Recipe recipe) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> userOptional = this.userService.getUserByEmail(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            int id = this.recipeService.createRecipe(user, recipe);
            var jsonObject = new JsonObject();
            jsonObject.addProperty("id", id);
            return ResponseEntity.status(HttpStatus.OK).body(jsonObject.toString());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable("id") int recipeId) {
        Optional<User> userOptional = getCurrentUserEmail();
        if (userOptional.isPresent()) {
            if (this.userService.isOwner(userOptional.get().getEmail(), recipeId)) {
                boolean deleted = recipeService.deleteRecipe(recipeId);
                if (deleted) {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            } else {
                // first not found, second forbidden
                if (!this.recipeService.doesRecipeExist(recipeId)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<Void> updateRecipe(@PathVariable("id") int recipeId, @Valid @RequestBody Recipe recipe) {
        Optional<User> userOptional = getCurrentUserEmail();
        if (userOptional.isPresent()) {
            if (this.userService.isOwner(userOptional.get().getEmail(), recipeId)) {
                if (!this.recipeService.doesRecipeExist(recipeId)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                } else {
                    try {
                        User user = userOptional.get();
                        this.recipeService.updateRecipe(user.getId(), recipeId, recipe);
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                    } catch (RuntimeException e) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
                }
            } else {
                if (!this.recipeService.doesRecipeExist(recipeId)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @GetMapping("/api/recipe/search")
    public ResponseEntity<List<Recipe>> getByCategoryOrName(@RequestParam Map<String, String> params) {
        if (params.size() != 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        for (var param : params.entrySet()) {
            if (!isValidParam(param.getKey())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        if (params.get("name") != null) {
            String name = params.get("name");
            List<Recipe> recipes = this.recipeService.findRecipesByName(name);
            return ResponseEntity.ok(recipes);
        }
        String category = params.get("category");
        List<Recipe> recipes = this.recipeService.findRecipesByCategory(category);
        return ResponseEntity.ok(recipes);
    }

    @PostMapping("/api/register")
    public ResponseEntity<Void> register(@Valid @RequestBody User user, HttpServletRequest request) throws ServletException {
        if (this.userService.doesUserExist(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            try {
                String password = user.getPassword();
                this.userService.saveUser(user);
                request.login(user.getEmail(), password);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    private boolean isValidParam(String param) {
        return Arrays.stream(AcceptedParam.values()).anyMatch(p -> p.name().equalsIgnoreCase(param));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Void> handleValidationExceptions(ConstraintViolationException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    private Optional<User> getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return this.userService.getUserByEmail(email);
    }


}

