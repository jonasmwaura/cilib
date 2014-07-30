/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.functions.continuous.unconstrained.Ackley;
import net.sourceforge.cilib.functions.continuous.unconstrained.Branin;
import net.sourceforge.cilib.functions.continuous.unconstrained.Bukin6;
import net.sourceforge.cilib.functions.continuous.unconstrained.EggHolder;
import net.sourceforge.cilib.functions.continuous.unconstrained.EqualMaxima;
import net.sourceforge.cilib.functions.continuous.unconstrained.EvenDecreasingMaxima;
import net.sourceforge.cilib.functions.continuous.unconstrained.Griewank;
import net.sourceforge.cilib.functions.continuous.unconstrained.Michalewicz;
import net.sourceforge.cilib.functions.continuous.unconstrained.ModifiedHimmelblau;
import net.sourceforge.cilib.functions.continuous.unconstrained.Pathological;
import net.sourceforge.cilib.functions.continuous.unconstrained.PenHolder;
import net.sourceforge.cilib.functions.continuous.unconstrained.Rana;
import net.sourceforge.cilib.functions.continuous.unconstrained.Rastrigin;
import net.sourceforge.cilib.functions.continuous.unconstrained.Rosenbrock;
import net.sourceforge.cilib.functions.continuous.unconstrained.SchwefelProblem2_26;
import net.sourceforge.cilib.functions.continuous.unconstrained.Shubert;
import net.sourceforge.cilib.functions.continuous.unconstrained.SixHumpCamelBack;
import net.sourceforge.cilib.functions.continuous.unconstrained.UnevenDecreasingMaxima;
import net.sourceforge.cilib.functions.continuous.unconstrained.UnevenEqualMaxima;
import net.sourceforge.cilib.functions.continuous.unconstrained.UrsemF1;
import net.sourceforge.cilib.functions.continuous.unconstrained.UrsemF3;
import net.sourceforge.cilib.functions.continuous.unconstrained.Vincent;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * An implementation of the standard PSO algorithm.
 * </p>
 * <p>
 * References:
 * </p>
 * <p>
 * <ul>
 * <li> J. Kennedy and R.C. Eberhart, "Particle swarm optimization," in Proceedings of IEEE
 * International Conference on Neural Networks, vol. IV, (Perth Australia), pp. 1942-1948, 1995.
 * </li>
 * <li> R.C. Eberhart and J. Kennedy, "A new optimizer using particle swarm theory," in Proceedings
 * of the Sixth International Symposium on Micro Machine and Human Science, (Nagoya, Japan), pp.
 * 39-43, 1995. </li>
 * <li> Y. Shi amd R.C. Eberhart, "A modified particle swarm optimizer," in Proceedings of the IEEE
 * Congress on Evolutionary Computation, (Anchorage, Alaska), pp. 69-73, May 1998. </li>
 * </ul>
 * </p>
 */
public class PSO extends SinglePopulationBasedAlgorithm<Particle> {

    private static final long serialVersionUID = -8234345682394295357L;

    private IterationStrategy<PSO> iterationStrategy;

    /**
     * Creates a new instance of {@link PSO}.
     * <p>
     * All fields are initialised to reasonable defaults. Note that the
     * {@link net.sourceforge.cilib.problem.Problem} is initially {@code null}
     * and must be set before {@link #algorithmInitialisation()} is called.
     */
    public PSO() {
        iterationStrategy = new SynchronousIterationStrategy();
        initialisationStrategy = new ClonedPopulationInitialisationStrategy();
        initialisationStrategy.setEntityType(new StandardParticle());
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public PSO(PSO copy) {
        super(copy);
        this.iterationStrategy = copy.iterationStrategy.getClone();

        for (Particle p : topology) {
            Particle nBest = Topologies.getNeighbourhoodBest(topology, p, this.neighbourhood, new SocialBestFitnessComparator());
            p.setNeighbourhoodBest(nBest);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PSO getClone() {
        return new PSO(this);
    }

    /**
     * Perform the required initialisation for the algorithm. Create the particles and add then to
     * the specified topology.
     */
    @Override
    public void algorithmInitialisation() {
        this.topology = fj.data.List.iterableList(initialisationStrategy.<Particle>initialise(optimisationProblem));

        for (Particle p : topology) {
            p.updateFitness(p.getBehaviour().getFitnessCalculator().getFitness(p));
        }
        
       //Pathological func= new Pathological();
       //Vector x = Vector.of(0.0879,10.0,0.356);
       //System.out.println(func.f(x));
       //System.out.println(func.getGradientVector(x));
    }

    /**
     * Perform the iteration of the PSO algorithm, use the appropriate <code>IterationStrategy</code>
     * to perform the iteration.
     */
    @Override
    protected void algorithmIteration() {
        iterationStrategy.performIteration(this);
    }

    /**
     * Get the best current solution. This best solution is determined from the personal bests of the
     * particles.
     * @return The <code>OptimisationSolution</code> representing the best solution.
     */
    @Override
    public OptimisationSolution getBestSolution() {
        Particle bestEntity = Topologies.getBestEntity(topology, new SocialBestFitnessComparator<Particle>());
        return new OptimisationSolution(bestEntity.getBestPosition(), bestEntity.getBestFitness());
    }

    /**
     * Get the collection of best solutions. This result does not actually make sense in the normal
     * PSO algorithm, but rather in a MultiObjective optimisation.
     * @return The <code>Collection&lt;OptimisationSolution&gt;</code> containing the solutions.
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> solutions = Lists.newLinkedList();
        for (Particle e : Topologies.getNeighbourhoodBestEntities(topology, neighbourhood, new SocialBestFitnessComparator<Particle>())) {
            solutions.add(new OptimisationSolution(e.getBestPosition(), e.getBestFitness()));
        }
        return solutions;
    }

    /**
     * Get the <code>IterationStrategy</code> of the PSO algorithm.
     * @return Returns the iterationStrategy..
     */
    public IterationStrategy<PSO> getIterationStrategy() {
        return iterationStrategy;
    }

    /**
     * Set the <code>IterationStrategy</code> to be used.
     * @param iterationStrategy The iterationStrategy to set.
     */
    public void setIterationStrategy(IterationStrategy<PSO> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

}
