package com.wojnarowicz.sfg.recipe.controller;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import com.wojnarowicz.sfg.recipe.command.RecipeCommand;
import com.wojnarowicz.sfg.recipe.domain.Recipe;
import com.wojnarowicz.sfg.recipe.exception.NotFoundException;
import com.wojnarowicz.sfg.recipe.service.RecipeService;

public class RecipeControllerTest {

    RecipeController recipeController;
    MockMvc mockMvc;
    
    @Mock
    RecipeService recipeService;
    
    @Mock
    Model model;
    
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        recipeController = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(recipeController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void testGetIndexPage() {
        
        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Set<Recipe> recipesSet = new HashSet<>();
        recipesSet.add(recipe);
        
        //when
        when(recipeService.findAll()).thenReturn(recipesSet);
        
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);
        
        //then
        String viewName = recipeController.getRecipes(model);
        Assertions.assertEquals("recipes/list", viewName);
        
        verify(recipeService, times(1)).findAll();
        verify(model, times(1)).addAttribute(eq("recipes"), eq(recipesSet));
        verify(model, times(1)).addAttribute(eq("recipes"), anySet());
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        
        Set<Recipe> recipesSet2 = argumentCaptor.getValue();
        Assertions.assertEquals(1, recipesSet2.size());
    }
    
    @Test
    void testGetRecipeById() throws Exception {
        //given
        Recipe recipe = Recipe.builder()
                .id(1L)
                .name("Test Recipe")
                .build();
        
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
        
        when(recipeService.findById(anyLong())).thenReturn(recipe);
        
        mockMvc.perform(get("/recipe/1/show"))
        .andExpect(status().isOk())
        .andExpect(view().name("recipes/view"))
        .andExpect(model().attributeExists("recipe")) 
        .andExpect(model().attribute("recipe", notNullValue()));
    }
    
    @Test
    void testGetRecipeByIdNotFound() throws Exception {
                
        when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/recipe/1/show"))
        .andExpect(status().isNotFound())
        .andExpect(view().name("/recipes/404error"));
    }
    
    @Test
    void testGetRecipeByIdNumberFormatExc() throws Exception {
                
        mockMvc.perform(get("/recipe/number/show"))
        .andExpect(status().isBadRequest())
        .andExpect(view().name("/recipes/400error"));
    }

    @Test
    public void testMockMVC() throws Exception {        
        mockMvc.perform(get("/recipes/"))
        .andExpect(status().isOk())
        .andExpect(view().name("recipes/list"));
    }
    
    @Test
    public void testGetNewRecipeForm() throws Exception {
        mockMvc.perform(get("/recipe/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("recipes/edit"))
        .andExpect(model().attributeExists("recipe"));
    }
    
    @Test
    public void testPostNewRecipeForm() throws Exception {
        Long longId = 2L;
        String testName = "Test Name";
        
        //given
        RecipeCommand command = new RecipeCommand();
        command.setId(longId);
        command.setName(testName);
        command.setDirections(testName);
        command.setTitle(testName);
        command.setUrl("www.example.com");
        
        //when
        when(recipeService.saveRecipeCommand(any())).thenReturn(command);
        
        //then
        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id",  "2")
                .param("description", "test description")
                .param("name",  "test name")
                .param("directions", "test directions")
                .param("title",  "test title")
                .param("url", "http://www.example.com"))
        
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/recipe/2/show"));
    }

    @Test
    public void testPostNewRecipeFormInvalidData() throws Exception {
        Long longId = 2L;
        
        //given
        RecipeCommand command = new RecipeCommand();
        command.setId(longId);
        
        //when
        when(recipeService.saveRecipeCommand(any())).thenReturn(command);
        
        //then
        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id",  "2"))
        
        .andExpect(status().isOk())
        .andExpect(view().name("recipes/edit"))
        .andExpect(model().attributeExists("recipe"));
    }
    
    @Test
    public void testGetUpdateRecipeForm() throws Exception {
        Long longId = 2L;
        String testName = "Test Name";
        
        //given
        RecipeCommand command = new RecipeCommand();
        command.setId(longId);
        command.setName(testName);
        
        //when
        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        mockMvc.perform(get("/recipe/2/edit"))
        .andExpect(status().isOk())
        .andExpect(view().name("recipes/edit"))
        .andExpect(model().attributeExists("recipe"));
    }
    
    @Test
    public void testDeleteById() throws Exception {
        mockMvc.perform(get("/recipe/2/delete"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/recipes"));
        
        verify(recipeService, times(1)).deleteById(anyLong());
    }
}
