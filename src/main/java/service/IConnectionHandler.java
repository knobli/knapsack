package service;

import solver.Chromo;
import solver.Item;
import solver.KnapsackInput;

import java.util.List;

/**
 * Created by knobli on 24.04.2015.
 */
public interface IConnectionHandler {

    KnapsackInput getKnapsackInput(String problemId);

    void saveResult(Chromo result, List<Item> items);

    void saveDraftResult(Chromo result, List<Item> items);
}
