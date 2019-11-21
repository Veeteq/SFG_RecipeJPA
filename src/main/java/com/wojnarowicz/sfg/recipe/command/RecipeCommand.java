package com.wojnarowicz.sfg.recipe.command;

import java.util.HashSet;
import java.util.Set;

import com.wojnarowicz.sfg.recipe.domain.Difficulty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand {
    private Long id;
    private String name;
    private String title;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private Set<IngredientCommand> ingredients = new HashSet<>();
    private Difficulty difficulty;
    private NotesCommand notes;
    private Set<RecipeCategoryCommand> categories = new HashSet<>();
}