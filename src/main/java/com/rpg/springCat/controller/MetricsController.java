package com.rpg.springCat.controller;

import com.rpg.springCat.model.Metrics;
import com.rpg.springCat.service.MetricsService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/metrics")
@AllArgsConstructor
public class MetricsController {

    private final MetricsService service;

    @GetMapping
    public Metrics findMetrics(Authentication auth){
        return service.findMetrics(auth);
    }

    @PostMapping("/new")
    public Metrics saveMetrics(@RequestBody Metrics metrics, Authentication auth){
        return service.saveMetrics(metrics, auth);
    }

    @PutMapping("/upd")
    public Metrics updateMetrics(@RequestBody Metrics metrics, Authentication auth){
        return service.updateMetrics(metrics, auth);
    }
}