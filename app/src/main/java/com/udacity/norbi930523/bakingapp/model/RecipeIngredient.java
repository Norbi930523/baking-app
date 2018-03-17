package com.udacity.norbi930523.bakingapp.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.udacity.norbi930523.bakingapp.R;

import java.text.DecimalFormat;

/**
 * Created by Norbert on 2018. 03. 12..
 */

public class RecipeIngredient implements Parcelable {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    private Double quantity;

    private String measure;

    private String ingredient;

    protected RecipeIngredient(Parcel in) {
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readDouble();
        }
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<RecipeIngredient> CREATOR = new Creator<RecipeIngredient>() {
        @Override
        public RecipeIngredient createFromParcel(Parcel in) {
            return new RecipeIngredient(in);
        }

        @Override
        public RecipeIngredient[] newArray(int size) {
            return new RecipeIngredient[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (quantity == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(quantity);
        }
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }

    public String getAsFormattedString(Context context){
        String measureStr = measure;

        int measureResId = getMeasureResourceId();
        if(measureResId != -1){
            measureStr = context.getResources().getQuantityString(measureResId, getQuantityPlural());
        }

        return context.getString(R.string.ingredient_item_pattern, getFormattedQuantity(), measureStr, ingredient);
    }

    /* Converts a double quantity to an int, based on which the correct plural string can be chosen.
     * E.g. 0.5 is plural, 1.0 is singular, 1.01 is plural.  */
    private int getQuantityPlural(){
        double quantityDiff = quantity - 1.0;

        if(quantityDiff >= 0 && quantityDiff < 0.01){
            return 1;
        }

        return 2;
    }

    private String getFormattedQuantity(){
        return DECIMAL_FORMAT.format(quantity);
    }

    private int getMeasureResourceId(){
        switch (measure){
            case "CUP":
                return R.plurals.ingredient_measure_cup;
            case "TBLSP":
                return R.plurals.ingredient_measure_tblsp;
            case "TSP":
                return R.plurals.ingredient_measure_tsp;
            case "G":
                return R.plurals.ingredient_measure_g;
            case "K":
                return R.plurals.ingredient_measure_k;
            case "OZ":
                return R.plurals.ingredient_measure_oz;
            case "UNIT":
                return R.plurals.ingredient_measure_unit;
            default:
                return -1;
        }
    }
}
