import java.util.*;

/**
 * run: http://java.dzone.com/articles/knapsack-problem
 * run2: Code converted from C++ http://cg.tuwien.ac.at/~zsolnai/gfx/knapsack_genetic/ to JAVA
 */
public class KnapsackSolver {

    private static final int MAX_SAME_RESULT = 100;

    private final ConnectionHandler connectionHandler;
    private final List<Item> items;
    private final int maxWeight;
    private double random = 0;

    public KnapsackSolver(ConnectionHandler connectionHandler, KnapsackInput knapsackInput) {
        this.connectionHandler = connectionHandler;
        this.items = knapsackInput.getItems();
        this.maxWeight = knapsackInput.getMaxWeight();
        Random random = new Random();
        this.random = random.nextInt(10000) / 10000;
        System.out.println("random:" + this.random);
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
        int pop = 250; // chromosome population size
        int gens = Integer.MAX_VALUE; // maximum number of generations
        int disc = (int) (Math.ceil(pop * 0.8)); // chromosomes discarded via elitism
        int size = items.size();
        int ind = 0, ind2 = 0; // a few helpers for the main()
        int parc = 0; // parent index for crossover
        double avg = 0, crp = 0.35; // crossover probability

        int sameResultCounter = 0;
        Chromo lastResult = new Chromo(size);
        Chromo best = new Chromo(size);

        List<Chromo> chromos = new ArrayList<>();
        for (int i = 0; i < pop; i++) {
            chromos.add(new Chromo(size));
        }
        boolean[][] c = new boolean[pop][size];
        System.out.println("Initializing population with a greedy algorithm...");
        initpopg(c, items, size, maxWeight, pop);
        System.out.println("done!");
        for (int i = 0; i < pop; i++) {
            chromos.get(i).items = c[i];
            chromos.get(i).fitness = fitness(chromos.get(i).items, size, items, maxWeight);
        }
        System.out.println("\n\n");

        for (int generationNumber = 0; generationNumber < gens; generationNumber++) {
            Collections.sort(chromos, new Comparator<Chromo>() {
                @Override
                public int compare(Chromo c1, Chromo c2) {
                    return c2.fitness - c1.fitness;
                }
            });
            for (int populationNumber = 0; populationNumber < pop; populationNumber++) {
                if (populationNumber > pop - disc) { // elitism - only processes the discarded chromosomes
                    if (coin(crp) == 1) { // crossover section
                        ind = parc + (int) Math.round(10 * random); // choosing parents for crossover
                        ind2 = parc + 1 + (int) Math.round(10 * random);
                        // choose a crossover strategy here
                        crossover1p(chromos.get(ind % pop), chromos.get(ind2 % pop), chromos.get(populationNumber), size, (int) Math.round(random * (size - 1)));
                       /*
                        crossover1p_b(chromos.get(ind % pop), chromos.get(ind2 % pop), chromos.get(populationNumber), size, (int) Math.round(random * (size - 1)));
                        crossoverrand(chromos.get(ind),chromos.get(ind2),chromos.get(populationNumber),size);
                        crossoverarit(chromos.get(0),chromos.get(1),chromos.get(populationNumber),size);
                         */
                        chromos.get(populationNumber).fitness = fitness(chromos.get(populationNumber).items, size, items, maxWeight);
                        parc += 1;
                    } else { // mutation section
                        chromos.get(populationNumber).mutate(size, 1);
                        chromos.get(populationNumber).fitness = fitness(chromos.get(populationNumber).items, size, items, maxWeight);
                    }
                }
                avg += chromos.get(populationNumber).fitness;
                if (chromos.get(populationNumber).fitness > best.fitness) {
                    best = chromos.get(populationNumber);
                }
            }
            parc = 0;
            if (best.equals(lastResult)) {
                sameResultCounter++;
            } else {
                lastResult = best;
                sameResultCounter = 0;
            }
            if (generationNumber % 5 == 0) {
                connectionHandler.saveResult(best, size);
                System.out.println("" + generationNumber);
                System.out.println("best fitness: " + best.fitness);
                System.out.println("avg fitness: " + (avg / pop));
            }
            if(sameResultCounter == MAX_SAME_RESULT){
                System.out.println("Limit reached!");
                break;
            }
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

        private void crossoverrand(Chromo c1, Chromo c2, Chromo c3, int dimc) {
            for (int i = 0; i < dimc; i++) {
                if ((int)Math.round(random) == 1) {
                    c3.items[i] = c1.items[i];
                } else {
                    c3.items[i] = c2.items[i];
                }
            }
        }

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
            rvals.add(new Pair<>(i, (double) items.get(i).getProfit() / (double) items.get(i).getWeight()));
        }
        Collections.sort(rvals, new Comparator<Pair<Integer, Double>>() {
            @Override
            public int compare(Pair<Integer, Double> r1, Pair<Integer, Double> r2) {
                return (int) Math.round(r2.second - r1.second);
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
