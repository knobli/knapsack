/**
 * Created by knobli on 23.04.2015.
 */
public class Chromo {
    boolean[] items;
    int fitness;

    public Chromo() {
    }



    public void mutate(int dimc, int count) {
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
}
