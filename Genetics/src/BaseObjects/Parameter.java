/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BaseObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Seva
 */
public class Parameter implements Serializable {

    private int[] params;
    private int legitUpperLimit;
    private int legitLowerLimit;

    public Parameter(int[] params) {
        this.params = params;
    }

    public Parameter(int[] params, int lower, int upper) {
        this.params = params;
        this.legitLowerLimit = lower;
        this.legitUpperLimit = upper;
    }

    public int[] getDeepParams() {
        int[] toReturn = new int[params.length];
        System.arraycopy(params, 0, toReturn, 0, params.length);
        return toReturn;
    }

    public void fixTheLimit(ArrayList<Parameter> fixer) { //this should only be called once after construction
        if (dependant()) {
            setLegitUpperLimit(getUpperLimit() + fixer.get(fixer.size() - 1).getUpperLimit());
            setLegitLowerLimit(getLowerLimit() + fixer.get(fixer.size() - 1).getLowerLimit());
        } else {
            setLegitUpperLimit(getUpperLimit());
            setLegitLowerLimit(getLowerLimit());
        }
    }

    public int getGeneralDataLength2() {
        return params[0];
    }

    public int getSpecificDataLength2() {
        return params[1];
    }

    public int getLowerLimit() {
        return params[2];
    }

    public int getUpperLimit() {
        return params[3];
    }

    public boolean dependant() {//its dependant if the param is 1;
        return params[4] == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Parameter) {
            Parameter other = (Parameter) o;
            int[] derp = other.getDeepParams();
            if (params.length == derp.length) {
                for (int i = 0; i < params.length; i++) {
                    if (params[i] != derp[i]) {
                        return false;
                    }
                }
                if (this.getLegitLowerLimit() != other.getLegitLowerLimit()) {
                    return false;
                }
                if (this.getLegitUpperLimit() != other.getLegitUpperLimit()) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Arrays.hashCode(this.params);
        return hash;
    }

    @Override
    public String toString() {
        String toReturn = "Parameter: ";
        for (int temp : params) {
            toReturn += temp + ",";
        }
        return toReturn.substring(0, toReturn.length() - 1);
    }

    public int getLegitUpperLimit() {
        return legitUpperLimit;
    }

    public void setLegitUpperLimit(int legitUpperLimit) {
        this.legitUpperLimit = legitUpperLimit;
    }

    public int getLegitLowerLimit() {
        return legitLowerLimit;
    }

    public void setLegitLowerLimit(int legitLowerLimit) {
        this.legitLowerLimit = legitLowerLimit;
    }
}