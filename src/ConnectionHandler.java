import java.util.List;

/**
 * Created by knobli on 22.04.2015.
 */
public class ConnectionHandler {

    public ConnectionHandler() {
    }


    public void saveResult(int i) {
        System.out.println("Result: " + i);
    }

    public KnapsackInput getKnapsackInput() {
        KnapsackInput knapsackInput = new KnapsackInput(10);
        knapsackInput.getItems().add(new Item(5, 10));
        knapsackInput.getItems().add(new Item(4, 40));
        knapsackInput.getItems().add(new Item(6, 30));
        knapsackInput.getItems().add(new Item(3, 50));
        return knapsackInput;
    }
}
