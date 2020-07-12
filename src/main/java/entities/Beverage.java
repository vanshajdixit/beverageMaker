package entities;

import java.util.Set;

public class Beverage {
    
    private final String name;
    private final Set<Ingredient> ingredientList;
    
    public Beverage(final String name, final Set<Ingredient> ingredientList) {
        this.name = name;
        this.ingredientList = ingredientList;
    }
    
    public String getBeverageType() {
        return name;
    }
    
    public Set<Ingredient> getIngredientList() {
        return ingredientList;
    }
}
