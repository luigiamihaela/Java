package com.example.live.recipe;

import com.example.live.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getReciperById(@PathVariable Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            return ResponseEntity.ok(recipe.get());
        } else {
            return ResponseEntity.status(404).body(null);  // Return 404 if not found
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recipe>> getRecipesByUserId(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(recipeRepository.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<?> createRecipe(@RequestBody Recipe recipe) {
        if (recipe.getUser() == null || !userRepository.existsById(recipe.getUser().getId())) {
            return ResponseEntity.badRequest().body("User ID is invalid or missing.");
        }
        return ResponseEntity.ok(recipeRepository.save(recipe));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable Long id, @RequestBody Recipe updatedRecipe) {
        if (!recipeRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Recipe not found.");
        }
        Recipe existingRecipe = recipeRepository.findById(id).get();
        existingRecipe.setTitle(updatedRecipe.getTitle());
        existingRecipe.setDescription(updatedRecipe.getDescription());
        existingRecipe.setUser(updatedRecipe.getUser());
        return ResponseEntity.ok(recipeRepository.save(existingRecipe));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long id) {
        if (!recipeRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Recipe not found.");
        }
        recipeRepository.deleteById(id);
        return ResponseEntity.ok("Recipe deleted successfully.");
    }
}
