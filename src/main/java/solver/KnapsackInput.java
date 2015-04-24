package solver;

import solver.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by knobli on 22.04.2015.
 */
public class KnapsackInput {
    private List<Item> items = new ArrayList<>();
    private int maxWeight;

    public KnapsackInput(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public KnapsackInput(List<Item> items, int maxWeight) {
        this.items = items;
        this.maxWeight = maxWeight;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }
}
