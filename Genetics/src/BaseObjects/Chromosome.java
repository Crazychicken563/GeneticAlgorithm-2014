/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BaseObjects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Seva
 */
public abstract class Chromosome implements Comparable, Serializable {

    protected double fitness = 0d;
    protected ArrayList<ArrayList<GeneticData>> parts;
    protected int[] inputs;
    protected boolean fitnessSet = false;

    @Override
    public int compareTo(Object o) {
        if (o instanceof Chromosome) {
            Chromosome other = (Chromosome) o;
            if (fitness > other.getFitness()) {
                return -1;
            } else if (fitness == other.getFitness()) {
                return 0;
            } else {
                return 1;
            }
        }
        return -1;
    }

    public int[] getInputs() {
        return inputs;
    }

    public ArrayList<ArrayList<GeneticData>> getGeneticDataByParts() {
        return parts;
    }

    public ArrayList<ArrayList<GeneticData>> getDeepGeneticDataByParts() {
        ArrayList<ArrayList<GeneticData>> toReturn = new ArrayList<>();
        for (ArrayList<GeneticData> temp : parts) {
            ArrayList<GeneticData> deep = new ArrayList<>();
            for (GeneticData g : temp) {
                GeneticData deeper = new GeneticData(g.getDeepGeneralDataList2(), g.getSpecificData2(), g.getNumInstances());
                deep.add(deeper);
            }
            toReturn.add(deep);
        }
        return toReturn;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
        fitnessSet = true;
    }

    public double getFitness() {
        return fitness;
    }

    public boolean isFit() {
        return fitnessSet;
    }

    public String getGeneticData() {
        String toReturn = "";
        for (ArrayList<GeneticData> temp : parts) {
            for (GeneticData g : temp) {
                toReturn += g.getGenericDataList2();
            }
        }
        return toReturn;
    }

    public abstract boolean GeneticDataEqual(Chromosome other);
}
