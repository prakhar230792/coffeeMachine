import java.util.ArrayList;
import java.util.List;

public class CoffeeMachine {

    private List<Outlet> outlets;

    public CoffeeMachine(List<Outlet> outlets) {
        this.outlets = outlets;
    }

    public void start() {
        //System.out.println("------- Coffee Machine Started --------");

        //Attaching all outlets to the coffee machine
        List<Thread> outletThreads = new ArrayList<>();
        for (int index = 0; index < outlets.size(); index++) {
            Thread t = new Thread(outlets.get(index));
            outletThreads.add(t);
            //Outlets to start making beverage.
            t.start();
        }
        for (int index = 0; index < outletThreads.size(); index++) {
            try {
                //We wait till all outlets are done making beverages
                outletThreads.get(index).join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //System.out.println("------- Coffee Machine Stopped --------");

    }
}
