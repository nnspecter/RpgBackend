package com.rpg.springCat.repository;


import com.rpg.springCat.model.Metrics;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryMetricsDAO {
    private Metrics METRICS;

    public Metrics saveMetrics(Metrics metrics) {
        if(this.METRICS != null){
            throw new RuntimeException("Metrics already exists");
        }
        this.METRICS = metrics;
        return this.METRICS;
    }

    public Metrics findMetrics(){
        return METRICS;
    }

    public Metrics updateMetrics(Metrics metrics){
        if(this.METRICS == null){
            throw new RuntimeException("Metrics not found");
        }
        this.METRICS = metrics;
        return this.METRICS;
    }



}
