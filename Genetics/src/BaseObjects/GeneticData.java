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
public class GeneticData implements Serializable {

    ArrayList<String> genericDataList2;
    String specificData2 = "";
    int numofInstance;

    public GeneticData(int generalDataLength2, int specificDataLength2, int numOfType) {
        genericDataList2 = new ArrayList<>();
        String temp;
        for (int j = 0; j < numOfType; j++) {
            temp = "";
            for (int i = 0; i < generalDataLength2; i++) {
                temp += "" + (int) (Math.random() * 10);
            }
            genericDataList2.add(temp);
        }
        temp = "";
        for (int i = 0; i < specificDataLength2; i++) {
            temp += "" + (int) (Math.random() * 10);
        }
        specificData2 = temp;
        this.numofInstance = numOfType;
    }

    public GeneticData(ArrayList<String> generalData2, String specificData2, int numOfType) {
        this.genericDataList2 = generalData2;
        this.specificData2 = specificData2;
        this.numofInstance = numOfType;
    }

    public int getNumInstances() {
        return this.numofInstance;
    }

    public void setNumInstances(int newNum) {
        this.numofInstance = newNum;
        int toAdd = this.numofInstance - this.genericDataList2.size();
        if (toAdd > 0) {//this allows for junk data that can be used later
            for (int j = 0; j < toAdd; j++) {
                String temp = "";
                for (int i = 0; i < genericDataList2.get(0).length(); i++) {
                    temp += "" + (int) (Math.random() * 10);
                }
                genericDataList2.add(temp);
            }
        }/* else if (toAdd < 0) {//If this fixes it i will be very pissed
         for (int i = 0; i > toAdd; i--) {
         genericDataList2.remove(genericDataList2.size() - 1);
         }
         }*/
    }

    public ArrayList<String> getGenericDataList2() {
        return genericDataList2;
    }

    public ArrayList<String> getDeepGeneralDataList2() {
        ArrayList<String> toReturn = new ArrayList<>();
        for (String s : genericDataList2) {
            toReturn.add(s);
        }
        return toReturn;
    }

    public String getSpecificData2() {
        return specificData2;
    }

    @Override
    public String toString() {
        return genericDataList2 + "," + specificData2 + " X" + this.getNumInstances();
    }
}
