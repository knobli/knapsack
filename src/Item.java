/**
 * Created by knobli on 22.04.2015.
 */
public class Item {

    private Integer weight;
    private Integer profit;

    public Item(Integer weight, Integer profit) {
        this.weight = weight;
        this.profit = profit;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getProfit() {
        return profit;
    }
}
