import java.util.List;

public class Main {

    public static void main(String[] args) {
        ConnectionHandler connectionHandler = new ConnectionHandler();
        KnapsackInput knapsackInput = connectionHandler.getKnapsackInput();
        KnapsackSolver knapsackSolver = new KnapsackSolver(connectionHandler, knapsackInput);
        knapsackSolver.run2();
    }


}
