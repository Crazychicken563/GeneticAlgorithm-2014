package GeneticAlgorithm;

import BaseObjects.Chromosome;
import BaseObjects.OldChromosome;
import BaseObjects.GeneticData;
import BaseObjects.Parameter;
import BaseObjects.VariableLengthChromosome;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Seva
 */
public class GeneticAlgorithm {

    private ArrayList<Chromosome> population;
    private static int numChildren = 2;
    private static double mutationPercent = 10; //rate at which mutation happens
    private static double mutationTypeChance = 50;//higher means more rapid mutation
    private static double deathRate = 50; //proportion that die
    private static int populationLimit = 20;//pre children
    private static double numNewCreaturesPerCycle = 1;
    private static double reproductionTypeChance = 20;  //higher means more sexual
    private static double increaseVariableLength = 20;  //Are we going to grow another bodypart? perhaps a tail
    private static double decreaseVariableLength = 20;  //Are we going to amputate a bodypart? perhaps 2?
    private static double alterGeneticData = 15; //Are we going to duplicate a previous bodypart? maybe a third arm?
    private static double addGeneticData = 50; //Are we going to devolve a previous bodypart? maybe a remove an arm?
    //these comments are terrible
    private String startingParameters = "";
    private Random r = new Random();
    private boolean variableLength;
    private int[] params;

    public GeneticAlgorithm(int startingNum, boolean variableLength, int... params) throws Exception {
        population = new ArrayList<>();
        this.params = Arrays.copyOfRange(params, 0, params.length);
        this.variableLength = variableLength;
        int[] passParams = Arrays.copyOfRange(params, 0, params.length);
        if (variableLength) {
            //the true parameters, post-inquisition
            for (int i = 0; i < startingNum; i++) {
                population.add(new VariableLengthChromosome(passParams));
            }
        } else {
            for (int i = 0; i < startingNum; i++) {
                population.add(new OldChromosome(passParams));
            }
        }
        for (int temp : params) {
            startingParameters += "_" + temp;
        }
    }

    public GeneticAlgorithm(ArrayList<Chromosome> startingPop, boolean variableLength, int[] params) {
        System.out.println(params);
        this.params = Arrays.copyOfRange(params, 0, params.length);
        this.variableLength = variableLength;
        population = startingPop;
    }

    public void setStartingParamenters(String input) {
        startingParameters = input;
    }

    public String getStartingParameters() {
        return startingParameters;
    }

    public void evolve() {
        cull();
        ArrayList<Chromosome> newBabies = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            if (i == population.size() - 1) { //last must be asexual if it gets to roll at all
                newBabies.addAll(reproduce(population.get(i)));
            } else if (generateProbability(reproductionTypeChance)) { //sexually reproduce
                newBabies.addAll(sexuallyReproduce(population.get(i), population.get(i + 1)));
                i++;
            } else { //asexually reproduce
                newBabies.addAll(reproduce(population.get(i)));
            }
        }
        for (int i = 0; i < newBabies.size(); i++) {
            newBabies.set(i, damageControl(alterLength(newBabies.get(i))));
        }
        int[] passParams = Arrays.copyOfRange(params, 0, params.length);
        for (int i = 0; i < numNewCreaturesPerCycle; i++) {
            if (this.variableLength) {
                try {
                    newBabies.add(new VariableLengthChromosome(passParams));
                } catch (Exception ex) {
                }
            } else {
                newBabies.add(new OldChromosome(passParams));
            }
        }
        population.addAll(newBabies);
        population = killClones(population);
    }

    private Chromosome alterLength(Chromosome c) {
        if (c instanceof VariableLengthChromosome) {
            VariableLengthChromosome chrom = (VariableLengthChromosome) c;
            ArrayList<Parameter> paramList = chrom.getDeepParameters();
            ArrayList<ArrayList<GeneticData>> parts = chrom.getDeepGeneticDataByParts();
            int currAmount;
            int gI = 0;
            for (ArrayList<GeneticData> currTypeList : parts) {
                if (!paramList.get(gI).dependant()) {
                    ////System.out.println("Param " + gI + " is independent");
                    currAmount = 0;
                    ////System.out.println("First thing we do is decide whether to add a new type");
                    for (GeneticData temp : currTypeList) {
                        currAmount += temp.getNumInstances();
                    }
                    int upperLimit = paramList.get(gI).getLegitUpperLimit();
                    int lowerLimit = paramList.get(gI).getLegitLowerLimit();
                    ////System.out.println("The upper limit for param " + gI + " is " + upperLimit);
                    ////System.out.println("The lower limit for param " + gI + " is " + lowerLimit);
                    ////System.out.println("The current amount of instances is " + currAmount);
                    int numWeCanAdd = upperLimit - currAmount;
                    ////System.out.println("Initially, there are " + currTypeList.size() + " types");
                    for (int i = 0; i < currTypeList.size(); i++) {
                        ////System.out.println("We can add this many more types: " + numWeCanAdd);
                        if (numWeCanAdd > 0 && generateProbability(increaseVariableLength)) {//we could add...
                            //System.out.println("We have decided to add a new type");
                            GeneticData template = currTypeList.get(0);
                            currTypeList.add(new GeneticData(template.getGenericDataList2().get(0).length(), template.getSpecificData2().length(), 1));
                            i++;
                            numWeCanAdd--;
                        }
                        if (currTypeList.size() > lowerLimit && generateProbability(decreaseVariableLength)) { //or we could amputate
                            //System.out.println("We have decided to remove type at index " + i);
                            GeneticData remove = currTypeList.remove(i);
                            numWeCanAdd += remove.getNumInstances();
                            i--;
                        } else {
                            ////System.out.println("We will not alter the amount of types");
                        }
                    }
                    ////System.out.println("Now we look at duplicating existing types");
                    for (GeneticData temp : currTypeList) {
                        ////System.out.println("We can duplicate this many more times: " + numWeCanAdd);
                        if (generateProbability(alterGeneticData)) {
                            ////System.out.println("We will try to alter type " + temp + " based on constraints");
                            if (generateProbability(addGeneticData)) {
                                ////System.out.println("We will try to grow");
                                if (numWeCanAdd > 0) {
                                    //System.out.println("We have grown another arm");
                                    temp.setNumInstances(temp.getNumInstances() + 1);
                                    numWeCanAdd--;
                                }
                            } else {
                                ////System.out.println("We will try to amputate");
                                if (temp.getNumInstances() > 1) {
                                    //System.out.println("We have ripped off an arm");
                                    temp.setNumInstances(temp.getNumInstances() - 1);
                                    numWeCanAdd++;
                                }
                            }
                        } else {
                            ////System.out.println("We will not alter type " + temp);
                        }
                    }
                } else {
                    ////System.out.println("Param " + gI + " is dependant. Will be corrected in damage control");
                }
                ////System.out.println("going to next param");
                gI++;//dont forget to increment the ghetto iterator
            }
            Chromosome temp;
            temp = new VariableLengthChromosome(parts, paramList, c.getInputs());
            //System.out.println("POST ALTER: " + temp);
            //System.out.println("What it says it became: " + temp);
            return temp;
        } else if (c instanceof OldChromosome) {
            Chromosome temp;
            ////System.out.println("Its invariable length, cant vary the length");
            temp = new OldChromosome(c.getDeepGeneticDataByParts());
            return temp;
        } else {
            System.out.println("Something is very wrong in alter length");
            return c;
        }
    }

    //lol, time for damage control
    //screw you colton
    private Chromosome damageControl(Chromosome c) {
        //System.out.println("DAMAGE CONTROL");
        if (c instanceof VariableLengthChromosome) {
            VariableLengthChromosome chrom = (VariableLengthChromosome) c;
            //System.out.println("PRE DAMAGE CONTROL: " + chrom);
            ArrayList<Parameter> paramList = chrom.getDeepParameters();
            ArrayList<ArrayList<GeneticData>> parts = chrom.getDeepGeneticDataByParts();
            for (int i = 0; i < parts.size(); i++) {//the first one is free to chill, but now updated for dependancy
                //no matter what, mutation and sexual repdrodcution could have fucked up everything
                ////System.out.println("Check whether param " + i + " is dependant");
                if (paramList.get(i).dependant()) {
                    ////System.out.println("Param " + i + " is dependant");
                    //how many in the prev type?
                    int numPrevType = 0;//<- that many
                    for (GeneticData g : parts.get(i - 1)) {
                        numPrevType += g.getNumInstances();
                    }
                    ////System.out.println("Num Prev Type: " + numPrevType);
                    Parameter currParam = paramList.get(i);
                    int limit = numPrevType + currParam.getUpperLimit();//<- here is how many we should have
                    ////System.out.println("new limit: " + limit);
                    //we need to find out how many we have right now in the curr type
                    int numCurrType = 0;//<- that many
                    for (GeneticData g : parts.get(i)) {
                        numCurrType += g.getNumInstances();
                    }
                    ////System.out.println("Num Curr Type: " + numCurrType);
                    int toAdd = limit - numCurrType;//<- this is how much we need to add or remove if its negative
                    ////System.out.println("We need to add " + toAdd + " in order to restore balance");
                    //we must approach this 2 different ways
                    if (toAdd < 0) {//we have too many, time to remove
                        //time for some reverse iteration
                        //System.out.println("We need to remove some");
                        int toRemove = toAdd * -1;//i like it this way
                        int amountToRemove = 0;
                        int totalSum = 0;
                        int diff = 0;
                        for (int j = parts.get(i).size() - 1; j >= 0; j--) {
                            amountToRemove++;
                            totalSum += parts.get(i).get(j).getNumInstances();
                            diff = totalSum - toRemove;
                            if (diff >= 0) {//found it
                                j = 0;
                            }
                        }
                        ////System.out.println("We are about to remove " + amountToRemove + " types");
                        if (diff != 0) {
                            amountToRemove--;
                            for (int j = 0; j < amountToRemove; j++) {
                                parts.get(i).remove(parts.get(i).size() - 1);
                            }
                            totalSum -= parts.get(i).get(parts.get(i).size() - 1).getNumInstances();
                            ////System.out.println("In reality, we removed " + totalSum + " instances");
                            ////System.out.println("The last type will be handled carefully");
                            amountToRemove = toRemove - totalSum;
                            GeneticData temp = parts.get(i).get(parts.get(i).size() - 1);
                            temp.setNumInstances(temp.getNumInstances() - amountToRemove);
                            ////System.out.println("We removed " + amountToRemove + " instances of the last type");
                        } else {
                            for (int j = 0; j < amountToRemove; j++) {
                                parts.get(i).remove(parts.get(i).size() - 1);
                            }
                            ////System.out.println("We removed " + amountToRemove + " types");
                        }
                        //////System.out.println("It possible that the list only contained 1 type, making it empty");
                        /*if (!parts.get(i).isEmpty()) {
                         ////System.out.println("The list is not empty, we can extend the last type.");

                         ////System.out.println("We added back " + diff + " instances, which is hopefully = to " + (totalSum - toRemove));
                         } else {instances
                         ////System.out.println("The list was empty, we must create a new type.");
                         }*/
                    } else if (toAdd > 0) {//we have too few, we need MOAR
                        //System.out.println("We have too few, we need to add some");
                        while (numCurrType < limit) {
                            int numOfType = r.nextInt(limit - numCurrType) + 1;
                            ////System.out.println("Lets add " + numOfType + " more to the thing");
                            numCurrType += numOfType;
                            ////System.out.println("This will give us " + numCurrType + " objects for this subset");
                            if (numCurrType > limit) {
                                ////System.out.println("We have exceeded the limit of objects, reseting numOfType to 1");
                                numOfType = 1;
                            }
                            if (r.nextBoolean()) {//we can add a new type, or we can extend an existing one
                                ////System.out.println("We will add " + numOfType + " to a new type");
                                GeneticData temp = new GeneticData(currParam.getGeneralDataLength2(), currParam.getSpecificDataLength2(), numOfType);
                                parts.get(i).add(temp);
                            } else {
                                int index = r.nextInt(parts.get(i).size());
                                ////System.out.println("We will add " + numOfType + " to an existing type at index " + index);
                                parts.get(i).get(index).setNumInstances(parts.get(i).get(index).getNumInstances() + numOfType);
                            }
                        }
                    }
                } else {
                    ////System.out.println("PARAMETER CORRECTION");
                    int numCurrType = 0;//<- that many
                    for (GeneticData g : parts.get(i)) {
                        numCurrType += g.getNumInstances();
                    }
                    Parameter p = paramList.get(i);
                    int toAdd = 0;
                    if (numCurrType < p.getLegitLowerLimit()) {
                        toAdd = p.getLegitLowerLimit() - numCurrType;
                    } else if (numCurrType > p.getLegitUpperLimit()) {
                        toAdd = p.getLegitUpperLimit() - numCurrType;
                    }
                    if (toAdd < 0) {//we have too many, time to remove
                        //time for some reverse iteration
                        ////System.out.println("We need to remove some");
                        int toRemove = toAdd * -1;//i like it this way
                        int amountToRemove = 0;
                        int totalSum = 0;
                        int diff = 0;
                        for (int j = parts.get(i).size() - 1; j >= 0; j--) {
                            amountToRemove++;
                            totalSum += parts.get(i).get(j).getNumInstances();
                            diff = totalSum - toRemove;
                            if (diff >= 0) {//found it
                                j = 0;
                            }
                        }
                        ////System.out.println("We are about to remove " + amountToRemove + " types");
                        if (diff != 0) {
                            amountToRemove--;
                            for (int j = 0; j < amountToRemove; j++) {
                                parts.get(i).remove(parts.get(i).size() - 1);
                            }
                            totalSum -= parts.get(i).get(parts.get(i).size() - 1).getNumInstances();
                            ////System.out.println("In reality, we removed " + totalSum + " instances");
                            ////System.out.println("The last type will be handeled carefully");
                            //gotta add back a few if we removed too many
                            amountToRemove = toRemove - totalSum;
                            GeneticData temp = parts.get(i).get(parts.get(i).size() - 1);
                            temp.setNumInstances(temp.getNumInstances() - amountToRemove);
                            ////System.out.println("We removed " + amountToRemove + " instances of the last type");
                        } else {
                            for (int j = 0; j < amountToRemove; j++) {
                                parts.get(i).remove(parts.get(i).size() - 1);
                            }
                            ////System.out.println("We removed " + amountToRemove + " types");
                        }
                        //////System.out.println("It possible that the list only contained 1 type, making it empty");
                        /*if (!parts.get(i).isEmpty()) {
                         ////System.out.println("The list is not empty, we can extend the last type.");

                         ////System.out.println("We added back " + diff + " instances, which is hopefully = to " + (totalSum - toRemove));
                         } else {instances
                         ////System.out.println("The list was empty, we must create a new type.");
                         }*/
                    } else if (toAdd > 0) {//we have too few, we need MOAR
                        ////System.out.println("We have too few, we need to add some");
                        while (numCurrType < p.getLegitLowerLimit()) {
                            int numOfType = r.nextInt(p.getLegitLowerLimit() - numCurrType) + 1;
                            ////System.out.println("Lets add " + numOfType + " more to the thing");
                            numCurrType += numOfType;
                            ////System.out.println("This will give us " + numCurrType + " objects for this subset");
                            if (numCurrType > p.getLegitLowerLimit()) {
                                ////System.out.println("We have exceeded the limit of objects, reseting numOfType to 1");
                                numOfType = 1;
                            }
                            if (r.nextBoolean()) {//we can add a new type, or we can extend an existing one
                                ////System.out.println("We will add " + numOfType + " to a new type");
                                GeneticData temp = new GeneticData(p.getGeneralDataLength2(), p.getSpecificDataLength2(), numOfType);
                                parts.get(i).add(temp);
                            } else {
                                int index = r.nextInt(parts.get(i).size());
                                ////System.out.println("We will add " + numOfType + " to an existing type at index " + index);
                                parts.get(i).get(index).setNumInstances(parts.get(i).get(index).getNumInstances() + numOfType);
                            }
                        }
                    } else {
                        ////System.out.println("No need to correct parameters");
                    }
                }
                ////System.out.println("going to next param");
            }
            Chromosome temp;
            temp = new VariableLengthChromosome(parts, paramList, c.getInputs());
            //System.out.println("POST DAMAGE CONTROL: " + temp);
            //System.out.println("What it says it became: " + temp);
            return temp;
        } else if (c instanceof OldChromosome) {
            Chromosome temp;
            ////System.out.println("cant damage the rock solid invariable chromosome");
            temp = new OldChromosome(c.getDeepGeneticDataByParts());
            return temp;
        } else {
            System.out.println("Something is very wrong in damage control");
            return c;
        }
    }

    private ArrayList<Chromosome> reproduce(Chromosome c) {
        //System.out.println("ASEXUAL REPRODUCTION");
        ArrayList<Chromosome> newChromosomes = new ArrayList();
        ArrayList<ArrayList<GeneticData>> parts = c.getGeneticDataByParts();
        //System.out.println("Parent chromosome: " + parts);
        for (int j = 0; j < numChildren; j++) {
            ArrayList<ArrayList<GeneticData>> offspringGeneticData = new ArrayList<>();
            for (int i = 0; i < parts.size(); i++) {
                ArrayList<GeneticData> currSubPart = parts.get(i);
                ArrayList<GeneticData> newSubPart = new ArrayList<>();
                for (int k = 0; k < currSubPart.size(); k++) {
                    ArrayList<String> temp = new ArrayList<>();
                    for (String data : currSubPart.get(k).getGenericDataList2()) {
                        String result = "";
                        for (char derp : data.toCharArray()) {
                            result += mutate(derp + "");
                        }
                        temp.add(result);
                    }
                    String result = "";
                    for (char derp : currSubPart.get(k).getSpecificData2().toCharArray()) {
                        result += mutate(derp + "");
                    }
                    newSubPart.add(new GeneticData(temp, result, currSubPart.get(k).getNumInstances()));
                }
                offspringGeneticData.add(newSubPart);
            }
            Chromosome temp;
            if (c instanceof OldChromosome) {
                temp = new OldChromosome(offspringGeneticData);
                if (!temp.getGeneticData().equals(c.getGeneticData())) {
                    newChromosomes.add(temp);
                } else {
                    ////System.out.println("It mutated to be the same asexually");
                }
            } else if (c instanceof VariableLengthChromosome) {
                VariableLengthChromosome derp = (VariableLengthChromosome) c;
                temp = new VariableLengthChromosome(offspringGeneticData, derp.getParameters(), derp.getInputs());
                //System.out.println("CHILD " + j + ": " + temp.getGeneticDataByParts());
                if (!temp.getGeneticData().equals(c.getGeneticData())) {
                    newChromosomes.add(temp);
                } else {
                    ////System.out.println("It mutated to be the same asexually");
                }
            } else {
                ////System.out.println("Something is very wrong here asexually");
            }
        }
        //System.out.println("Killing Clones from Asexual reproduction");
        newChromosomes = killClones(newChromosomes);
        //System.out.println("Asexual reproduction complete");
        return newChromosomes;
    }

    private ArrayList<Chromosome> sexuallyReproduce(Chromosome a, Chromosome b) {
        //System.out.println("SEXUAL REPRODUCTION");
        ArrayList<Chromosome> newChromosomes = new ArrayList();
        ArrayList<ArrayList<GeneticData>> aParts = a.getDeepGeneticDataByParts();
        ArrayList<ArrayList<GeneticData>> bParts = b.getDeepGeneticDataByParts();
        //System.out.println("Parent A chromosome: " + aParts);
        //System.out.println("Parent B chromosome: " + bParts);
        for (int j = 0; j < numChildren; j++) {
            int totaltimes = aParts.size();
            if (bParts.size() < totaltimes) {
                totaltimes = bParts.size();
            }
            ArrayList<ArrayList<GeneticData>> offspringGeneticData = new ArrayList<>();
            for (int i = 0; i < totaltimes; i++) {
                ArrayList<GeneticData> subAParts = aParts.get(i);
                ArrayList<GeneticData> subBParts = bParts.get(i);
                ArrayList<GeneticData> currSubPart = new ArrayList<>();
                int minSize = subAParts.size();
                if (subBParts.size() < minSize) {
                    minSize = subBParts.size();
                }
                for (int k = 0; k < minSize; k++) {
                    if (r.nextBoolean()) {
                        currSubPart.add(subAParts.get(k));
                    } else {
                        currSubPart.add(subBParts.get(k));
                    }
                }
                offspringGeneticData.add(currSubPart);//I HATE SEXUAL REPRODUCTION
            }
            Chromosome temp;
            if (a instanceof OldChromosome && b instanceof OldChromosome) {
                temp = new OldChromosome(offspringGeneticData);
                if (!(temp.getGeneticData().equals(a.getGeneticData())
                        && temp.getGeneticData().equals(b.getGeneticData()))) {
                    newChromosomes.add(temp);
                } else {
                    ////System.out.println("It mutated to be the same sexually");
                }
            } else if (a instanceof VariableLengthChromosome && b instanceof VariableLengthChromosome) {
                VariableLengthChromosome derp = (VariableLengthChromosome) a;
                temp = new VariableLengthChromosome(offspringGeneticData, derp.getParameters(), derp.getInputs());
                //System.out.println("CHILD " + j + ": " + temp.getGeneticDataByParts());
                if (!(temp.getGeneticData().equals(a.getGeneticData())
                        && temp.getGeneticData().equals(b.getGeneticData()))) {
                    newChromosomes.add(temp);
                } else {
                    ////System.out.println("It mutated to be the same sexually");
                }
            } else {
                ////System.out.println("Something is very wrong here sexually");
            }
        }
        //System.out.println("Killing Clones from Sexual reproduction");
        newChromosomes = killClones(newChromosomes);
        //System.out.println("Sexual reproduction complete");
        return newChromosomes;
    }

    private String mutate(String in) {
        if (generateProbability(mutationPercent)) {
            //////System.out.println("ELDRITCH HORRORS UNLEASHED");
            if (generateProbability(mutationTypeChance)) {
                return r.nextInt(10) + "";
            } else {
                int toAdd = r.nextInt(3) - 1;
                int newInt = Integer.parseInt(in) + toAdd;
                if (newInt == 10) {
                    newInt = 0;
                } else if (newInt == -1) {
                    newInt = 9;
                }
                return newInt + "";
            }
        } else {
            return in;
        }
    }

    private void cull() {
        Collections.sort(population);
        int numToDie = (int) ((deathRate / 100) * population.size());
        while (numToDie > 1 || population.size() > populationLimit) {
            population.remove(population.size() - 1);
            numToDie--;
        }
    }

    public ArrayList<Chromosome> getCurrIteration() {
        return population;
    }

    public void setFitness(Chromosome c, double val) {
        for (Chromosome chrom : population) {
            if (chrom.GeneticDataEqual(c)) {
                //System.out.println("WE HAVE SET A FITNESS");
                chrom.setFitness(val);
                break;
            }
        }
    }

    public ArrayList<Chromosome> killClones(ArrayList<Chromosome> pop) {
        for (int i = 0; i < pop.size(); i++) {
            for (int j = i + 1; j < pop.size(); j++) {
                Chromosome test = pop.get(i);
                Chromosome check = pop.get(j);
                if (test.GeneticDataEqual(check)) {
                    //System.out.println("KILLING: " + check + " which is the same as " + test);
                    pop.remove(j);
                    j--;
                }
            }
        }
        return pop;
    }

    public boolean generateProbability(double chance) {
        return r.nextDouble() * 100 <= chance;
    }

    public static String shortString(ArrayList<Chromosome> list) {
        String s = "";
        for (Chromosome c : list) {
            s += c.hashCode() + " ";
        }
        return s;
    }

    public boolean containsUntested() {
        for (Chromosome c : population) {
            if (!c.isFit()) {
                return true;
            }
        }
        return false;
    }

    public Chromosome getNewUntested() {
        //TEST
        //Collections.shuffle(population);
        for (Chromosome c : population) {
            if (!c.isFit()) {
                return c;
            }
        }
        return null;
    }

    public double[] getStats() {
        double[] toReturn = new double[6];
        //0: max
        //1: min
        //2: mean
        //3: median
        ArrayList<Chromosome> tested = getTested();
        toReturn[0] = getMaxFitness(tested);
        toReturn[1] = getMinFitness(tested);
        toReturn[2] = getMeanFitness(tested);
        toReturn[3] = getMedianFitness(tested);
        toReturn[4] = getSDFitness(tested);
        toReturn[5] = tested.size();
        return toReturn;
    }

    public ArrayList<Chromosome> getTested() {
        ArrayList<Chromosome> tested = new ArrayList<>();
        for (Chromosome c : population) {
            if (c.isFit()) {
                tested.add(c);
            }
        }
        Collections.sort(tested);
        return tested;
    }

    public double getMedianFitness(ArrayList<Chromosome> in) {
        if (in.size() % 2 == 1) {
            return in.get((in.size() + 1) / 2 - 1).getFitness();
        } else {
            double middle = in.size() / 2.0 - 0.5;
            //System.out.println("AND THE MIDDLE IS: " + middle);
            double fitOne = in.get((int) Math.floor(middle)).getFitness();
            double fitTwo = in.get((int) Math.ceil(middle)).getFitness();
            return (fitOne + fitTwo) / 2;
        }
    }

    public double getMeanFitness(ArrayList<Chromosome> in) {
        double total = 0;
        for (Chromosome s : in) {
            total += s.getFitness();
        }
        return total / in.size();
    }

    public double getMaxFitness(ArrayList<Chromosome> in) {
        return in.get(0).getFitness();
    }

    public double getMinFitness(ArrayList<Chromosome> in) {
        return in.get(in.size() - 1).getFitness();
    }

    public double getSDFitness(ArrayList<Chromosome> in) {
        double mean = getMeanFitness(in);
        double total = 0;
        for (Chromosome c : in) {
            total += Math.pow(c.getFitness() - mean, 2);
        }
        return Math.sqrt(total / in.size());
    }

    public ArrayList<Chromosome> getPopulation() {
        return population;
    }
    
    public ArrayList<Chromosome> getLegitCurrIteration() {
        ArrayList<Chromosome> untested = new ArrayList<>();
        for (Chromosome c : population) {
            if (!c.isFit()) {
                untested.add(c);
            }
        }
        return untested;
    }
}