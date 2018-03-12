package com.udacity.norbi930523.bakingapp.model;

/**
 * Created by Norbert on 2018. 03. 12..
 */

public class RecipeIngredient {

    private Double quantity;

    private String measure;

    private String ingredient;

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
