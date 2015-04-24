package service;

import solver.Chromo;
import solver.Item;
import solver.KnapsackInput;

import java.util.List;
import java.util.Random;

/**
 * Created by knobli on 22.04.2015.
 */
public class ConnectionHandler implements IConnectionHandler {

    @Override
    public KnapsackInput getKnapsackInput(String problemId) {
        KnapsackInput knapsackInput = new KnapsackInput(120);
        Random rnd = new Random();
        for (int i = 1; i < 1000; i++) {
            knapsackInput.getItems().add(new Item(i, rnd.nextInt(), rnd.nextInt()));
        }
    /*
        knapsackInput.getItems().add(new Item(1, 5, 10));
        knapsackInput.getItems().add(new Item(2, 4, 40));
        knapsackInput.getItems().add(new Item(3, 6, 30));
        knapsackInput.getItems().add(new Item(4, 3, 50));
        knapsackInput.getItems().add(new Item(5, 7, 45));
        knapsackInput.getItems().add(new Item(6, 5, 10));
        knapsackInput.getItems().add(new Item(7, 4, 40));
        knapsackInput.getItems().add(new Item(8, 6, 30));
        knapsackInput.getItems().add(new Item(9, 3, 50));
        knapsackInput.getItems().add(new Item(10, 7, 45));
*/
        return knapsackInput;
    }

    @Override
    public void saveResult(Chromo result, List<Item> items) {
        System.out.println("Saved FINAL result: {profit: " + result.getFitness() + ", items: [" + result.toString(items) + "]}");
    }


    @Override
    public void saveDraftResult(Chromo result, List<Item> items) {
        System.out.println("Saved draft result: {profit: " + result.getFitness() + ", items: [" + result.toString(items) + "]}");
    }
}
