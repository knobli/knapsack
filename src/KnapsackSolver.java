import java.util.*;

/**
 * run: http://java.dzone.com/articles/knapsack-problem
 * run2: Code converted from C++ http://cg.tuwien.ac.at/~zsolnai/gfx/knapsack_genetic/ to JAVA
 */
public class KnapsackSolver {

    private final ConnectionHandler connectionHandler;
    private final List<Item> items;
    private final int maxWeight;
    private int random = 0;

    public KnapsackSolver(ConnectionHandler connectionHandler, KnapsackInput knapsackInput) {
        this.connectionHandler = connectionHandler;
        this.items = knapsackInput.getItems();
        this.maxWeight = knapsackInput.getMaxWeight();
        Random random = new Random();
        this.random = random.nextInt(10000)/10000;
    }

    public void run() {
        int size = items.size(); // Get the total number of items. Could be wt.length or val.length. Doesn't matter
        int[][] vector = new int[size + 1][maxWeight + 1]; //Create a matrix. Items are in rows and weight at in columns +1 on each side
        //What if the knapsack's capacity is 0 - Set all columns at row 0 to be 0
        for (int col = 0; col <= maxWeight; col++) {
            vector[0][col] = 0;
        }
        //What if there are no items at home.  Fill the first row with 0
        for (int row = 0; row <= size; row++) {
            vector[row][0] = 0;
        }
        for (int item = 1; item <= size; item++) {
            //Let's fill the values row by row
            for (int weight = 1; weight <= maxWeight; weight++) {
                //Is the current items weight less than or equal to running weight
                if (items.get(item - 1).getWeight() <= weight) {
                    //Given a weight, check if the value of the current item + value of the item that we could afford with the remaining weight
                    //is greater than the value without the current item itself
                    vector[item][weight] = Math.max(items.get(item - 1).getProfit() + vector[item - 1][weight - items.get(item - 1).getWeight()], vector[item - 1][weight]);
                } else {
                    //If the current item's weight is more than the running weight, just carry forward the value without the current item
                    vector[item][weight] = vector[item - 1][weight];
                }

            }
        }
        //Printing the matrix
        for (int[] rows : vector) {
            for (int col : rows) {
                System.out.format("%5d", col);
            }
            System.out.println();
        }
        connectionHandler.saveResult(vector[size][maxWeight]);
    }

    public void run2() {
        int limit = 50; // knapsack weight limit
        int pop = 250; // chromosome population size
        int gens = Integer.MAX_VALUE; // maximum number of generations
        int disc = (int) (Math.ceil(pop * 0.8)); // chromosomes discarded via elitism
        int dimw = items.size();
        int best = 0, ind = 0, ind2 = 0; // a few helpers for the main()
        int parc = 0; // parent index for crossover
        double avg = 0, crp = 0.35; // crossover probability
        List<Chromo> ch = new ArrayList<>();
        for (int i = 0; i < pop; i++) {
            ch.add(new Chromo(dimw));
        }
        boolean[][] c = new boolean[pop][dimw];
        System.out.println("Initializing population with a greedy algorithm...");
        initpopg(c, items, dimw, limit, pop);
        System.out.println("done!");
        for (int i = 0; i < pop; i++) {
            ch.get(i).items = c[i];
            ch.get(i).f = fitness(ch.get(i).items, dimw, items, limit);
        }
        System.out.println("\n\n");

        for (int p = 0; p < gens; p++) {
            Collections.sort(ch, new Comparator<Chromo>() {
                @Override
                public int compare(Chromo c1, Chromo c2) {
                    return c1.f - c2.f;
                }
            });
            for (int i = 0; i < pop; i++) {
                if (i > pop - disc) { // elitism - only processes the discarded chromosomes
                    if (coin(crp) == 1) { // crossover section
                        ind = parc + Math.round(10 * random); // choosing parents for crossover
                        ind2 = parc + 1 + Math.round(10 * random);
                        // choose a crossover strategy here
                        crossover1p(ch.get(ind % pop), ch.get(ind2 % pop), ch.get(i), dimw, Math.round(random * (dimw - 1)));
                        /*
                        crossoverrand(ch[ind],ch[ind2],ch[i],dimw);
                        crossoverarit(ch[0],ch[1],ch[i],dimw);
                         */
                        ch.get(i).f = fitness(ch.get(i).items, dimw, items, limit);
                        parc += 1;
                    } else { // mutation section
                        ch.get(i).mutate(dimw, 1);
                        ch.get(i).f = fitness(ch.get(i).items, dimw, items, limit);
                    }
                }
                avg += ch.get(i).f;
                if (ch.get(i).f > best) {
                    best = ch.get(i).f;
                }
            }
            parc = 0;
            if (p % 5 == 0) {
                System.out.println("" + p);
                System.out.println("best fitness: " + best);
                System.out.println("avg fitness: " + (avg / pop));
                if (best == 675) {
                    return;
                }
            }
            best = 0;
            avg = 0;
        }
    }

    private int fitness(boolean[] x, int dimc, List<Item> items, int limit) {
        int fit = 0, wsum = 0;
        for (int i = 0; i < dimc; i++) {
            int val = (x[i]) ? 1 : 0;
            wsum += val * items.get(i).getWeight();
            fit += val * items.get(i).getProfit();
        }
        if (wsum > limit) {
            fit -= 7 * (wsum - limit); // penalty for invalid solutions
        }
        return fit;
    }

    private void crossover1p(Chromo c1, Chromo c2, Chromo c3, int dimc, int cp) {
        for (int i = 0; i < dimc; i++) {
            if (i < cp) {
                c3.items[i] = c1.items[i];
            } else {
                c3.items[i] = c2.items[i];
            }
        }
    }

    private void crossover1p_b(Chromo c1, Chromo c2, Chromo c3, int dimc, int cp) {
        for (int i = 0; i < dimc; i++) {
            if (i >= cp) {
                c3.items[i] = c1.items[i];
            } else {
                c3.items[i] = c2.items[i];
            }
        }
    }
/*
    private void crossoverrand(Chromo c1, Chromo c2, Chromo c3, int dimc) {
        for (int i = 0; i < dimc; i++) {
            if (Math.round(random)) {
                c3.items[i] = c1.items[i];
            } else {
                c3.items[i] = c2.items[i];
            }
        }
    }
*/
    private void crossoverarit(Chromo c1, Chromo c2, Chromo c3, int dimc) {
        for (int i = 0; i < dimc; i++) {
            c3.items[i] = (c1.items[i] ^ c2.items[i]);
        }
    }

    private int coin(double crp) { // a cointoss
        if (random < crp) return 1; // crossover
        else return 0; // mutation
    }

    private void initpopg(boolean[][] c, List<Item> items, int dimw, int limit, int pop) {
        List<Pair<Integer, Double>> rvals = new ArrayList<>(dimw);
        int[] index = new int[dimw];
        for (int i = 0; i < dimw; i++) {
            rvals.add(new Pair<>(i, (double) items.get(i).getProfit() / (double) items.get(i).getProfit()));
        }
        Collections.sort(rvals, new Comparator<Pair<Integer, Double>>() {
            @Override
            public int compare(Pair<Integer, Double> r1, Pair<Integer, Double> r2) {
                return (int) Math.round(r1.second - r2.second);
            }
        });
        int currentWeight = 0, k;
        for (int i = 0; i < dimw; i++) {
            k = rvals.get(i).first;
            if (currentWeight + items.get(k).getWeight() <= limit) { // greedy fill
                currentWeight += items.get(k).getWeight();
                index[k] = 1;
            }
        }
        for (int i = 0; i < pop; i++) {
            for (int j = 0; j < dimw; j++) {
                c[i][j] = (index[j] == 1) ? true : false;
            }
        }
    }

    private class Chromo {

        boolean[] items;
        int f;

        public Chromo(int dimc) {
            this.items = new boolean[dimc];
        }

        public void mutate(int dimc, int count) {
            int mi;
            for (int i = 0; i < count; i++) {
                mi = (int) (Math.round(random * (dimc - 1)));
                items[mi] = !items[mi];
            }
        }
    }

    private class Pair<T, V> {
        private T first;
        private V second;

        public Pair(T first, V second) {
            this.first = first;
            this.second = second;
        }
    }
}
