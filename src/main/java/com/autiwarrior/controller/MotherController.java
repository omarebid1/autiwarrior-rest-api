package com.autiwarrior.controller;


import com.autiwarrior.dto.MotherDTO;
import com.autiwarrior.dto.MotherProfileDTO;
import com.autiwarrior.dto.mapper.MotherMapper;
import com.autiwarrior.entities.Mother;
import com.autiwarrior.service.MotherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mothers")
public class MotherController {

    @Autowired
    private MotherService motherService;

    // ------------------ Create ------------------

    @PostMapping
    public ResponseEntity<MotherDTO> createMother(@RequestBody Mother mother) {
        Mother createdMother = motherService.createMother(mother);
        MotherDTO dto = MotherMapper.toDTO(createdMother);
        return ResponseEntity.ok(dto);
    }

    // ------------------ Read ------------------

    @GetMapping("/find")
    public ResponseEntity<MotherDTO> getMotherByEmail(@RequestParam String email) {
        Optional<Mother> motherOpt = motherService.getMotherByEmail(email);
        if (motherOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Mother mother = motherOpt.get();
        MotherDTO dto = MotherMapper.toDTO(mother);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<MotherDTO>> getAllMothers() {
        List<Mother> mothers = motherService.getAllMothers();
        List<MotherDTO> dtos = mothers.stream()
                .map(MotherMapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // ------------------ Update ------------------

    @PutMapping("/update")
    public ResponseEntity<Mother> updateMother(@RequestParam String email, @RequestBody Mother mother) {
        Optional<Mother> motherOpt = motherService.getMotherByEmail(email);
        if (motherOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        mother.setMotherId(motherOpt.get().getMotherId());
        Mother updatedMother = motherService.updateMother(mother);
        return ResponseEntity.ok(updatedMother);
    }

    // ------------------ Delete ------------------

    @DeleteMapping
    public ResponseEntity<Void> deleteMother(@RequestParam String email) {
        motherService.deleteMother(email);
        return ResponseEntity.noContent().build();
    }

    // ------------------ Profile ------------------

    @GetMapping("/profile")
    public ResponseEntity<MotherDTO> getMotherProfile(@RequestParam String email) {
        Optional<Mother> motherOpt = motherService.getMotherByEmail(email);
        if (motherOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Mother mother = motherOpt.get();
        MotherDTO dto = MotherMapper.toDTO(mother);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/complete-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> completeMotherData(
            @RequestParam String email,
            @ModelAttribute MotherProfileDTO motherProfileDTO) throws IOException {

        motherService.saveMotherDataByEmail(email, motherProfileDTO);
        return ResponseEntity.ok("Mother profile updated successfully");
    }
}
