package com.example.plater.utils;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.plater.Category;
import com.example.plater.Ingrediente;
import com.example.plater.PassoPreparo;
import com.example.plater.Recipe;

import java.util.List;

@Dao
public interface MyDao {
    @Insert
    void insertRecipe (Recipe recipe);

    @Insert
    void insertPassoPreparo (PassoPreparo passoPreparo);

    @Insert
    void insertIngrediente (Ingrediente ingrediente);

    @Insert
    void insertCategory(Category category);

    @Query("SELECT * FROM recipe WHERE id=:idReceita")
    Recipe getRecipe(int idReceita);

    @Query("SELECT * FROM recipe ORDER BY id")
    List<Recipe> getAllRecipes();

    @Query("SELECT * FROM passopreparo WHERE idReceita=:idReceita")
    List<PassoPreparo> getRecipeInstructions(int idReceita);

    @Query("SELECT * FROM ingrediente WHERE idReceita=:idReceita")
    List<Ingrediente> getRecipeIngredients(int idReceita);

    @Query("SELECT * FROM category ORDER BY id")
    List<Category> getAllCategories();

    @Query(("SELECT * FROM recipe WHERE idCategoria=:idCategoria"))
    List<Recipe> getCategoryRecipes(int idCategoria);

    @Query("SELECT * FROM recipe WHERE titulo LIKE '%' || :pesquisa || '%' OR descricao LIKE '%' || :pesquisa || '%' OR categoria LIKE '%' || :pesquisa || '%'")
    List<Recipe> searchRecipes(String pesquisa);
}
