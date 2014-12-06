package BaseObjects;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Seva
 */
public class OldChromosome extends Chromosome {

    public OldChromosome(int... input) {
        inputs = Arrays.copyOfRange(input, 0, input.length);
        parts = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            ArrayList<GeneticData> tempList = new ArrayList<>();
            int length = input[i];
            int repetition = input[++i];
            for (int j = 0; j < repetition; j++) {
                tempList.add(new GeneticData(length, 0, 1));
            }
            parts.add(tempList);
        }
    }

    public OldChromosome(ArrayList<ArrayList<GeneticData>> input) {
        parts = input;
    }

    @Override
    public String toString() {
        return "|" + getGeneticData();
    }

    @Override
    public boolean GeneticDataEqual(Chromosome other) {
        if (other instanceof OldChromosome) {
            return this.getGeneticData().equals(other.getGeneticData());
        }
        return false;
    }
}