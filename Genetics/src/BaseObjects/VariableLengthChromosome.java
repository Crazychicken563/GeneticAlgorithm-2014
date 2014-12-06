/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BaseObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Seva
 */
public class VariableLengthChromosome extends Chromosome {

    private Random r = new Random();
    private ArrayList<Parameter> parameters = new ArrayList<>();

    public VariableLengthChromosome(int... input) throws Exception {
        inputs = Arrays.copyOfRange(input, 0, input.length);
        //System.out.println("NEW CHROMOSOME CREATION");
        int dataPerParam = 5;
        //params are
        //specific Data length doesnt chnge
        //general data length
        //lower bound/modifiers
        //upper bound/modifiers
        //dependancy boolean
        if (input.length % dataPerParam != 0) {
            throw new Exception() {
                @Override
                public String getMessage() {
                    return "Incorrect num of params";
                }
            };
        }
        parts = new ArrayList<>();
        int[] param;
        for (int i = 0; i < input.length; i += dataPerParam) {
            param = new int[dataPerParam];
            param[0] = input[i];
            param[1] = input[i + 1];
            param[2] = input[i + 2];
            param[3] = input[i + 3];
            param[4] = input[i + 4];
            Parameter temp = new Parameter(param);
            temp.fixTheLimit(parameters);//ABSOLUTELY MUST BE CALLED ==|BEFORE|== ADDING TO PARAMETERS
            parameters.add(temp);
        }
        int gI = 0;
        for (Parameter currParam : parameters) {
            //System.out.println("THE CURR PARAM: " + currParam);
            int totalAmount = 0;
            int upperLimit = r.nextInt((currParam.getUpperLimit() + 1) - currParam.getLowerLimit()) + currParam.getLowerLimit();
            //System.out.println("We will limit the number of this type to " + upperLimit + " + prev calculated limit");
            ArrayList<GeneticData> newData = new ArrayList<>();
            int lowerLimit = currParam.getLowerLimit();
            //Damage Control
            if (currParam.dependant()) {
                int numPrevType = numObjects(parts.get(gI - 1));//will crash here if the first parameter is dependant
                upperLimit = numPrevType + currParam.getUpperLimit();
                lowerLimit = numPrevType + currParam.getLowerLimit();
                //System.out.println("new Upper Limit: " + upperLimit);
                //System.out.println("new Lower Limit: " + lowerLimit);
            }
            //

            //System.out.println("Initial Test: " + totalAmount + " < " + lowerLimit + " || " + totalAmount + " < " + upperLimit);
            while (totalAmount < lowerLimit || totalAmount < upperLimit) {
                int numOfType = r.nextInt(upperLimit - totalAmount) + 1;
                //System.out.println("Lets add " + numOfType + " of this kind of type");
                totalAmount += numOfType;
                //System.out.println("This gives us " + totalAmount + " objects for this subset");
                if (totalAmount > upperLimit) {
                    //System.out.println("We have exceeded the limit of objects, reseting to 1");
                    numOfType = 1;
                }
                GeneticData temp = new GeneticData(currParam.getGeneralDataLength2(), currParam.getSpecificDataLength2(), numOfType);
                newData.add(temp);
                //System.out.println("Loop Test: " + totalAmount + " < " + lowerLimit + " || " + totalAmount + " < " + upperLimit);
            }
            parts.add(newData);
            gI++;
        }
        //System.out.println("PARTS: " + parts);
    }

    public VariableLengthChromosome(ArrayList<ArrayList<GeneticData>> gData, ArrayList<Parameter> params, int[] input) {
        ////System.out.println("I HAVE BEEN INSTANTIATED WITH PARAMETERS");
        inputs = Arrays.copyOfRange(input, 0, input.length);
        parts = gData;
        parameters = params;
        //does the input correspond to the parameters? who knows? probably not.
    }

    @Override
    public String toString() {
        return "Variable Length: " + this.getGeneticDataByParts();
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public ArrayList<Parameter> getDeepParameters() {
        ArrayList<Parameter> toReturn = new ArrayList<>();
        for (Parameter p : parameters) {
            Parameter deep = new Parameter(p.getDeepParams(), p.getLegitLowerLimit(), p.getLegitUpperLimit());
            toReturn.add(deep);
        }
        return toReturn;
    }

    @Override
    public boolean GeneticDataEqual(Chromosome other) {
        //System.out.println("ARE WE EVEN HERE");
        if (other instanceof VariableLengthChromosome) {
            VariableLengthChromosome c = (VariableLengthChromosome) other;
            //System.out.println("WE ARE IN FACT A VARIABLE LENGTH");
            ArrayList<Parameter> deep = c.getDeepParameters();
            for (int i = 0; i < this.getParameters().size(); i++) {
                if (!parameters.get(i).equals(deep.get(i))) {
                    return false;
                }
            }
            //System.out.println("WE DO IN FACT HAVE THE SAME PARAMETERS");
            //System.out.println(this.getGeneticData());
            //System.out.println(other.getGeneticData());
            if (this.getGeneticData().equals(other.getGeneticData())) {
                //System.out.println("WE ARE IN FACT THE SAME");
                return true;
            }

        }
        return false;
    }

    private int numObjects(ArrayList<GeneticData> input) {
        int total = 0;
        for (GeneticData g : input) {
            total += g.getNumInstances();
        }
        return total;
    }
}