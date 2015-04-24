package solver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import service.IConnectionHandler;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KnapsackSolverTest {

    private static final String PROBLEM_ID = "asbd-1asr";

    private KnapsackSolver knapsackSolver = new KnapsackSolver();

    @Mock
    private IConnectionHandler connectionHandler;

    @Before
    public void init() throws IOException, URISyntaxException {
        MockitoAnnotations.initMocks(this);
        knapsackSolver.setConnectionHandler(connectionHandler);

        when(connectionHandler.getKnapsackInput(PROBLEM_ID)).thenReturn(createKnapsackInput_10000());
    }

    @After
    public void verifyMocks() {
        Mockito.verifyNoMoreInteractions(connectionHandler);
    }

    @Test
    public void testRun() {
        when(connectionHandler.getKnapsackInput(PROBLEM_ID)).thenReturn(createKnapsackInput());

        int best = knapsackSolver.run(PROBLEM_ID);
        assertThat(best).isEqualTo(90);
        verify(connectionHandler).getKnapsackInput(PROBLEM_ID);
    }

    @Test
    public void testRun_10000() {
        int best = knapsackSolver.run(PROBLEM_ID);
        assertThat(best).isEqualTo(750);
        verify(connectionHandler).getKnapsackInput(PROBLEM_ID);
    }

    @Test
    public void testRun2() {
        when(connectionHandler.getKnapsackInput(PROBLEM_ID)).thenReturn(createKnapsackInput());

        Chromo best = knapsackSolver.run2(PROBLEM_ID);
        assertThat(best.getFitness()).isEqualTo(90);
        verify(connectionHandler).getKnapsackInput(PROBLEM_ID);
        verify(connectionHandler, times(6)).saveDraftResult(any(Chromo.class), anyListOf(Item.class));
        verify(connectionHandler).saveResult(any(Chromo.class), anyListOf(Item.class));
    }

    @Test
    public void testRun2_10000() {
        Chromo best = knapsackSolver.run2(PROBLEM_ID);
        assertThat(best.getFitness()).isEqualTo(750);
        verify(connectionHandler).getKnapsackInput(PROBLEM_ID);
        verify(connectionHandler, times(6)).saveDraftResult(any(Chromo.class), anyListOf(Item.class));
        verify(connectionHandler).saveResult(any(Chromo.class), anyListOf(Item.class));
    }

    private KnapsackInput createKnapsackInput(){
        KnapsackInput knapsackInput = new KnapsackInput(10);
        knapsackInput.getItems().add(new Item(1, 5, 10));
        knapsackInput.getItems().add(new Item(2, 4, 40));
        knapsackInput.getItems().add(new Item(3, 6, 30));
        knapsackInput.getItems().add(new Item(4, 3, 50));
        return knapsackInput;
    }

    private KnapsackInput createKnapsackInput_10000() throws URISyntaxException, IOException {
        List<Integer> values = getValuesFromResourceFile("10000_values.txt");
        List<Integer> weights = getValuesFromResourceFile("10000_weights.txt");

        KnapsackInput knapsackInput = new KnapsackInput(50);
        for (int i = 0; i < values.size(); i++) {
            knapsackInput.getItems().add(new Item(i, weights.get(i), values.get(i)));
        }
        return knapsackInput;
    }

    private List<Integer> getValuesFromResourceFile(String filename) throws URISyntaxException, IOException {
        URL resource = getClass().getResource("/" + filename);
        assertThat(resource).isNotNull();
        File file = new File(resource.toURI());
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        List<Integer> values = new ArrayList<>();
        String strLine;
        Pattern p = Pattern.compile("(\\d+)");
        while ((strLine = br.readLine()) != null) {
            Matcher m = p.matcher(strLine);
            while(m.find()) {
                values.add(Integer.parseInt(m.group(1)));
            }
        }
        assertThat(values).hasSize(10000);
        return values;
    }
}