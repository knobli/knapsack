package solver;

/**
 * Created by knobli on 22.04.2015.
 */
public class Item {

    private Integer id;
    private Integer weight;
    private Integer profit;

    public Item(Integer id, Integer weight, Integer profit) {
        this.id = id;
        this.weight = weight;
        this.profit = profit;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getProfit() {
        return profit;
    }

    @Override
    public String toString() {
        return "{id: " + id + ", weight: " + weight + ", profit: " + profit + "}";
    }
}
