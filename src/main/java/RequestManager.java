import java.util.List;

/**
 * RequestManager stores the list of all beverages we need to make
 * Outlets request the next beverage to be made to this class
 */
public class RequestManager {
    List<Beverage> beveragesToMake;

    public RequestManager(List<Beverage> beveragesToMake) {
        this.beveragesToMake = beveragesToMake;
    }

    //Tells the outlet what beverage ios to be made next
    //Takes care that only one outlet(thread) can update the request list at a time
    public Beverage getNextBeverageToMake() {
        Beverage beverage = null;
        synchronized(beveragesToMake) {
            //When all beverages made, return null
            if (beveragesToMake.size() == 0) {
                return null;
            }
            beverage = beveragesToMake.remove(0);
        }
        return beverage;
    }
}
