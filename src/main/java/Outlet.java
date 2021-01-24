import java.util.List;

public class Outlet implements Runnable {
    private String name;
    private RequestManager requestManager;
    private Inventory inventory;

    public Outlet(String name, RequestManager requestManager, Inventory inventory) {
        this.name = name;
        this.requestManager = requestManager;
        this.inventory = inventory;
    }

    //Starts the functioning of an outlet till all beverages are made
    public void run() {
        //Fetch next beverage to make from request manager
        Beverage beverage = requestManager.getNextBeverageToMake();

        //Keep making beverage till no more beverage request
        while (beverage != null) {
            //Check and update inventory to make beverage
            List<String> ingredientsNotPresent = inventory.fetchIngredientsAndUpdateInventory(beverage);

            if (ingredientsNotPresent.size() == 0) { // Beverage made successfully
                System.out.println(beverage.getName() + " is prepared");
            } else {//Some ingredientsa were not there or were not in sufficient quantity
                String error = beverage.getName() + " cannot be prepared because ";
                for (String err : ingredientsNotPresent) {
                    error = error + err + " ";
                }
                System.out.println(error);
            }
            beverage = requestManager.getNextBeverageToMake();
        }
    }
}
