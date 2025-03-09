package com.autiwarrior.controller;


import com.autiwarrior.entities.Mother;
import com.autiwarrior.service.MotherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mothers")
public class MotherController {

    @Autowired
    private MotherService motherService;

    // Create a new mother
    @PostMapping
    public ResponseEntity<Mother> createMother(@RequestBody Mother mother) {
        Mother createdMother = motherService.createMother(mother);
        return ResponseEntity.ok(createdMother);
    }

    // Get a mother by ID
    @GetMapping("/{motherId}")
    public ResponseEntity<Mother> getMotherById(@PathVariable Long motherId) {
        Optional<Mother> mother = motherService.getMotherById(motherId);
        return mother.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get all mothers
    @GetMapping
    public ResponseEntity<List<Mother>> getAllMothers() {
        List<Mother> mothers = motherService.getAllMothers();
        return ResponseEntity.ok(mothers);
    }

    // Update a mother
    @PutMapping("/{motherId}")
    public ResponseEntity<Mother> updateMother(@PathVariable Long motherId, @RequestBody Mother mother) {
        mother.setMotherId(motherId);
        Mother updatedMother = motherService.updateMother(mother);
        return ResponseEntity.ok(updatedMother);
    }

    // Delete a mother by ID
    @DeleteMapping("/{motherId}")
    public ResponseEntity<Void> deleteMother(@PathVariable Long motherId) {
        motherService.deleteMother(motherId);
        return ResponseEntity.noContent().build();
    }
}
