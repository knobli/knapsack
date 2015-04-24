package solver;

import java.util.List;

/**
 * Created by knobli on 23.04.2015.
 */
public class Chromo {
    private boolean[] items;
    private int fitness;

    public Chromo() {
    }

    public boolean[] getItems() {
        return items;
    }

    public void setItems(boolean[] items) {
        this.items = items;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public void mutate(int dimc, int count, double random) {
        int mi;
        for (int i = 0; i < count; i++) {
            mi = (int) (Math.round(random * (dimc - 1)));
            items[mi] = !items[mi];
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Chromo)) {
            return false;
        }
        return this.fitness == ((Chromo) obj).fitness;
    }

    public String toString(List<Item> itemList) {
        StringBuilder combination = new StringBuilder();
        int index = 0;
        for(boolean itemUsed : items){
           if(itemUsed) {
               combination.append(itemList.get(index).toString());
               combination.append(", ");
           }
            index++;
        }
        int length = combination.length();
        combination.delete(length -2, length);
        return combination.toString();
    }
}
