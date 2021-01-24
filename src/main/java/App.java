import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Main Driver class that loads the config and starts the coffee machine 
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        //Loading file data into objects
        JSONParser jsonParser = new JSONParser();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("input.json");
        JSONObject jsonObject = (JSONObject)jsonParser.parse(
            new InputStreamReader(is, "UTF-8"));
        
        JSONObject machine = (JSONObject)jsonObject.get("machine");
        JSONObject outletsJsonObj = (JSONObject)machine.get("outlets");

        //No of outlets
        int count_n = ((Long)(outletsJsonObj.get("count_n"))).intValue();

        JSONObject totalItemsQuantity = (JSONObject)machine.get("total_items_quantity");

        //Inventory of ingredients that the coffee machine has
        HashMap<String, Ingredient> ingredients = new HashMap<>();
        for (Object entry : totalItemsQuantity.entrySet()) {
            Ingredient ingredient = new Ingredient((String)((Entry)entry).getKey(), ((Long)((Entry)entry).getValue()).intValue());
            ingredients.put(ingredient.getName(), ingredient);
        }

        //Creating Inventory which will have all quantities availability info
        Inventory inventory = new Inventory(ingredients);

        JSONObject beveragesJsonObj = (JSONObject)machine.get("beverages");

        //List of bevereages that coffee machine needs to make
        List<Beverage> beverages = new ArrayList<Beverage>();
        for (Object entry : beveragesJsonObj.entrySet()) {
            String beverageName = (String)((Entry)entry).getKey();

            JSONObject beverageIngredientsJsonObj = (JSONObject)((Entry)entry).getValue();
            List<Ingredient> beverageIngredients = new ArrayList<Ingredient>();
            for (Object beverageIngredientsEntry : beverageIngredientsJsonObj.entrySet()) {
                Ingredient ingredient = new Ingredient((String)((Entry)beverageIngredientsEntry).getKey(), ((Long)((Entry)beverageIngredientsEntry).getValue()).intValue());
                beverageIngredients.add(ingredient);
            }
            Beverage beverage = new Beverage(beverageName, beverageIngredients);
            beverages.add(beverage);
        }

        //Has all the requests of beverges we have in input json
        RequestManager requestManager = new RequestManager(beverages);

        //Outlet represent the number of outlets a coffee machine has. Outlet pick up next beverage request from Request Manager
        List<Outlet> outlets = new ArrayList<>();
        for (int index = 1; index <= count_n; index++) {
            Outlet outlet = new Outlet("Outlet " + index, requestManager, inventory);
            outlets.add(outlet);
        }

        //Coffee Machine object. 
        CoffeeMachine coffeeMachine = new CoffeeMachine(outlets);
        //Starting coffe machine
        coffeeMachine.start();
    }
}
