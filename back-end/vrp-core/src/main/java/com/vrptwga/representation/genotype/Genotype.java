package com.vrptwga.representation.genotype;

import com.vrptwga.concepts.OptimizationScenario;
import com.vrptwga.representation.Individual;

import java.util.ArrayList;
import java.util.List;

public class Genotype {

    private HeaderChromosome headerChromosome;
    private CoreChromosome coreChromosome;
    private TailerChromosome tailerChromosome;
    private List<String> schemeChromosome = new ArrayList<>();

    public List<String> getSchemeChromosome() {
        return schemeChromosome;
    }

    public void setSchemeChromosome(List<String> schemeChromosome) {
        this.schemeChromosome = schemeChromosome;
    }

    public TailerChromosome getTailerChromosome() {
        return tailerChromosome;
    }

    public void setTailerChromosome(TailerChromosome tailerChromosome) {
        this.tailerChromosome = tailerChromosome;
    }

    public CoreChromosome getCoreChromosome() {
        return coreChromosome;
    }

    public void setCoreChromosome(CoreChromosome coreChromosome) {
        this.coreChromosome = coreChromosome;
    }

    public HeaderChromosome getHeaderChromosome() {
        return headerChromosome;
    }

    public void setHeaderChromosome(HeaderChromosome headerChromosome) {
        this.headerChromosome = headerChromosome;
    }

    @Override
    public String toString() {
        return "Genotype{" +
                "headerChromosome=" + headerChromosome +
                ", coreChromosome=" + coreChromosome +
                ", tailerChromosome=" + tailerChromosome +
                ", schemeChromosome=" + schemeChromosome +
                '}';
    }

    public static Genotype enCodingScheme(Individual individual, OptimizationScenario optimizationScenario){
        Genotype genotype = new Genotype();
        genotype.headerChromosome = new HeaderChromosome(individual, optimizationScenario.getVehicles());
        genotype.coreChromosome = new CoreChromosome(individual);
        genotype.tailerChromosome = new TailerChromosome(individual);
        return genotype;
    }
}
