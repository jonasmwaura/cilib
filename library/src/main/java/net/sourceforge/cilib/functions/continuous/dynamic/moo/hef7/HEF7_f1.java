/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.hef7;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f1 function of the adapted F7 problem defined in the following paper:
 * H. Li and Q. Zhang. Multiobjective optimization problems with complicated Pareto sets,
 * MOEA/D and NSGA-II, IEEE Transactions on Evolutionary Computation, 13(2):284-302, 2009.
 *
 * The problem has been adapted by Helbig and Engelbrecht to make it a DMOOP.
 *
 */
public class HEF7_f1 implements ContinuousFunction {

    private static final long serialVersionUID = 1914230427150406406L;

    /**
     * Evaluates the function
     */
    @Override
    public Double apply(Vector input) {
        double value = Math.abs(input.doubleValueOf(0));
        double sum = 0.0;

        int index = 0;
        for (int k=2; k < input.size(); k+=2) {
            double val = input.doubleValueOf(k) - Math.pow(value, 0.5*(1.0+(3.0*(k-2)/(input.size()-2))));
            sum += Math.pow(4.0*val,2)-Math.cos(8.0*val*Math.PI) + 1.0;
            index++;
        }
        sum *= 2.0/index;
        sum += value;
        return sum;
    }
}
