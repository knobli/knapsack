import service.ConnectionHandler;
import solver.KnapsackInput;
import solver.KnapsackSolver;

public class Main {

    public static void main(String[] args) {
        String problemId = "1";
        KnapsackSolver knapsackSolver = new KnapsackSolver();
        knapsackSolver.setConnectionHandler(new ConnectionHandler());
        knapsackSolver.run2(problemId);
    }


}
