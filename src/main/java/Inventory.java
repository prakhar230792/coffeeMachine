import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Inventory maintains the complete list of remaining inventory
 * inside the coffee machine.
 * 
 */
public class Inventory {

    HashMap<String, Ingredient> ingredients;

    public Inventory(HashMap<String, Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    /**
     * When an outlet has to make a beverage, it calls this function
     * Here inventory takes care that we update the inventory for only
     * one beverage at a time.
     */
    public List<String> fetchIngredientsAndUpdateInventory(Beverage beverage) {

        //This stores all errors we face while creating a beverage
        //If this list is empty, it means that beverage was successfully made
        List<String> ingredientsNotAvailable = new ArrayList<>();
        
        //Only one outlet thread can enter here at a time
        synchronized(ingredients) {
            boolean wereAllIngredientsPresent = true;

            //Start reducing the inventory for all ingredients of a beverage
            List<Ingredient> ingredientsRequired = beverage.getIngredients();
            for (Ingredient ingredient : ingredientsRequired) {
                Ingredient ingredientInInventory = ingredients.get(ingredient.getName());
                if (ingredientInInventory == null) {
                    ingredientsNotAvailable.add(ingredient.getName() + " is not available");
                    wereAllIngredientsPresent = false;
                    continue;
                }

                ingredientInInventory.setQuantity(ingredientInInventory.getQuantity() - ingredient.getQuantity());
                if (ingredientInInventory.getQuantity() < 0) {
                    ingredientsNotAvailable.add(ingredient.getName() + " is " + (ingredientInInventory.getQuantity() + ingredient.getQuantity()));
                    wereAllIngredientsPresent = false;
                }
            }

            //In case all ingredients were not present, we need to revert all the ingredients
            //quantity that we had deducted for this beverage
            if (!wereAllIngredientsPresent) {
                for (Ingredient ingredient : ingredientsRequired) {
                    Ingredient ingredientInInventory = ingredients.get(ingredient.getName());
                    if (ingredientInInventory == null) {
                        continue;
                    }
    
                    ingredientInInventory.setQuantity(ingredientInInventory.getQuantity() + ingredient.getQuantity());
                }
            }
        }
        return ingredientsNotAvailable;
    }
}
