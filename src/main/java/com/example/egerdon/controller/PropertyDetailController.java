package com.example.egerdon.controller;

import com.example.egerdon.dto.PropertyDetailDto;
import com.example.egerdon.service.PropertyDetailService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/property-details")
@CrossOrigin
public class PropertyDetailController {
    private final PropertyDetailService service;

    public PropertyDetailController(PropertyDetailService service) {
        this.service = service;
    }

    @GetMapping
    public List<PropertyDetailDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public PropertyDetailDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public PropertyDetailDto save(@RequestBody PropertyDetailDto dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public PropertyDetailDto update(@PathVariable Long id, @RequestBody PropertyDetailDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
