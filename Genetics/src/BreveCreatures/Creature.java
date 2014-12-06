/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BreveCreatures;

import java.util.ArrayList;

/**
 *
 * @author Seva
 */
public class Creature {
    
    private ArrayList<BodyPart> body;
    private double moveIncrement = 1;
    
    public Creature(String geneticData) {
        body = new ArrayList<>();
        ArrayList<Integer> jointInfo = geneDecoder(geneticData);
        for (int i = 0; i <= jointInfo.size(); i++) {
            BodyPart temp = new BodyPart(jointInfo.get(i));
            temp.setJointValue(jointInfo.get(i));
            if (i - 1 < 0) {
                temp.setFront(null);
            } else {
                temp.setFront(body.get(i - 1));
            }
            body.get(i).setRear(temp);
            body.add(temp);
        }
        body.get(body.size() - 1).setRear(null);
    }
    
    private ArrayList<Integer> geneDecoder(String geneticData) {
        ArrayList<Integer> jointValues = new ArrayList<>();
        int looper = 3;
        String currNum = "";
        for (char curr : geneticData.toCharArray()) {
            if (looper % 3 == 0) {
                if (!currNum.equals("")) {
                    jointValues.add(new Integer(currNum));
                    currNum = "";
                }
                if (curr < '5') {
                    currNum += "-";
                }
            } else {
                currNum += curr;
            }
        }
        return jointValues;
    }
    
    public void move(double moveIncrement) {
        for (BodyPart curr : body) {
            curr.move(moveIncrement);
        }
    }
    
    public void setMoveIncrement(double increment) {
        moveIncrement = increment;
    }
    
    public double getMoveIncrement() {
        return moveIncrement;
    }
}