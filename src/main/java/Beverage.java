import java.util.List;

/**
 * Beverage class represents a beverage that we need to make
 * IT stores name of the beverage and the list of ingredients needed to make the beverage
 */
public class Beverage {
    private String name;
    private List<Ingredient> ingredients;

    public Beverage(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }


}
