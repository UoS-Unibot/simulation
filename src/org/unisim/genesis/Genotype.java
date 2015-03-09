/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.genesis;

import com.google.common.base.Joiner;
import com.google.common.primitives.Floats;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Stores a list of gene values that can be mutated or crossed over from another
 * genotype. To instantiate, use the static factory methods
 * Genotype.withRandomGenome() or Genotype.withGenes(). Instances are immutable;
 * that is, they are always guaranteed to be in a consistent state, making this
 * class threadsafe.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public final class Genotype {

    /**
     * Creates a new Genotype with random number of genes.
     *
     * @param length number of genes to create; must be 1 or more.
     * @return the randomised Genotype.
     */
    public static Genotype withRandomGenome(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Length must be one or more.");
        }
        return new Genotype(getRandomisedGenome(length));
    }

    /**
     * Creates a new Genotype with the specified genes.
     *
     * @param genes array of genes to specify. These are copied into the new
     * genotype.
     * @return A new Genotype with the specified genes.
     */
    public static Genotype withGenes(float[] genes) {
        return new Genotype(Arrays.copyOf(genes, genes.length));
    }

    private static float[] getRandomisedGenome(int length) {
        float[] genes = new float[length];
        for (int i = 0; i < length; i++) {
            genes[i] = ThreadLocalRandom.current().nextFloat() * 2 - 1;
        }
        return genes;
    }

    private final int length;
    private final float[] genes;

    private Genotype(float[] genes) {
        length = genes.length;
        this.genes = genes;
    }

    private float randCreep(float mutVariance) {
        return (float) ThreadLocalRandom.current().nextGaussian() * mutVariance;
    }

    /**
     * Returns a version of this Genotype with mutated genes, adding creep,
     * which is a Gaussian random variable with mean 0 and the specified
     * variance. Creep that would take a gene out of the [-1,1] range is
     * reflected, e.g. if the gene was 0.8 and the creep was 0.5, the new gene
     * would be 0.7.
     *
     * @param mutVariance variance of the creep. May be any value, but if it is
     * over 1 the creep is likely to often take genes out of range causing a lot
     * of reflections, and the genotype will be extremely unstable.
     * @return the new mutated Genotype
     */
    public Genotype mutate(float mutVariance) {
        float[] newGenes = new float[length];
        for (int i = 0; i < length; i++) {
            float newGene;
            newGene = genes[i] + randCreep(mutVariance);
            if (newGene < -1.0) {
                newGenes[i] = -2 - newGene;
            } else if (newGene > 1.0) {
                newGenes[i] = 2 - newGene;
            } else {
                newGenes[i] = newGene;
            }
        }
        return Genotype.withGenes(newGenes);
    }

    /**
     * Returns a version of this Genotype with genes copied over from another
     * genotype to this one with the specified per-gene probability.
     *
     * @param pCross probability that a given gene will be copied. Must be
     * between 0 and 1, with 0 indicating that the original gene will always be
     * retained, and 1 indicating that the gene will always be copied from the
     * second genotype.
     * @param genes2 array of genes to copy from. Should be the same length as
     * the number of genes in this genotype.
     * @return the new Genotype with copied genes.
     */
    public Genotype crossover(float pCross, float[] genes2) {
        if (genes.length != genes2.length) {
            throw new IllegalArgumentException("Genes2 (length " + genes2.length
                    + ") must have the same number of genes as this genotype (length "
                    + genes.length + ")");
        }

        float[] newGenes = new float[length];
        for (int i = 0; i < genes2.length;
                i++) {
            if (ThreadLocalRandom.current().nextFloat() < pCross) {
                newGenes[i] = genes2[i];
            } else {
                newGenes[i] = genes[i];
            }
        }
        return Genotype.withGenes(newGenes);
    }

    /**
     * Gets the genes of this Genotype.
     *
     * @return an array containing a copy of the genes.
     */
    public float[] getGenes() {
        return Arrays.copyOf(genes, length);
    }

    /**
     * Gets the number of genes in this genotype.
     *
     * @return number of genes contained in this genotype.
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns a string representation of this genotype, with the length of the
     * genotype and the genes. If the genotype is longer than 10 genes, the gene
     * list is abbreviated so that only the first 5 and last 5 genes are
     * displayed.
     *
     * @return String representation of this genotype.
     */
    @Override
    public String toString() {
        Joiner j = Joiner.on(", ");
        StringBuilder s = new StringBuilder("Genotype (").append(String.valueOf(
                length));
        s.append(" genes) {");
        if (length < 10) {
            s.append(j.join(Floats.asList(genes)));
        } else {
            s.append(j.join(Floats.asList(Arrays.copyOfRange(genes, 0,
                    5))));
            s.append("...");
            s.append(j.join(Floats.asList(Arrays.copyOfRange(genes, length - 5,
                    length))));
        }
        s.append("}");
        return s.toString();
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.length;
        hash = 89 * hash + Arrays.hashCode(this.genes);
        return hash;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Genotype other = (Genotype) obj;
        if (this.length != other.length) {
            return false;
        }
        return Arrays.equals(this.genes, other.genes);
    }

}
