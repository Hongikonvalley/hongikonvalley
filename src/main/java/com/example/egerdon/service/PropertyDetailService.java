package com.example.egerdon.service;

import com.example.egerdon.dto.PropertyDetailDto;
import com.example.egerdon.entity.Listing;
import com.example.egerdon.entity.PropertyDetail;
import com.example.egerdon.mapper.PropertyDetailMapper;
import com.example.egerdon.repository.ListingRepository;
import com.example.egerdon.repository.PropertyDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyDetailService {
    private final PropertyDetailRepository repository;
    private final ListingRepository listingRepository;

    public PropertyDetailService(PropertyDetailRepository repository, ListingRepository listingRepository) {
        this.repository = repository;
        this.listingRepository = listingRepository;
    }

    public List<PropertyDetailDto> getAll() {
        return repository.findAll().stream()
                .map(PropertyDetailMapper::toDto)
                .collect(Collectors.toList());
    }

    public PropertyDetailDto getById(Long id) {
        PropertyDetail entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PropertyDetail not found"));
        return PropertyDetailMapper.toDto(entity);
    }

    public PropertyDetailDto save(PropertyDetailDto dto) {
        Listing listing = listingRepository.findById(dto.getListingId())
                .orElseThrow(() -> new RuntimeException("Listing not found"));
        PropertyDetail entity = PropertyDetailMapper.toEntity(dto, listing);
        return PropertyDetailMapper.toDto(repository.save(entity));
    }

    public PropertyDetailDto update(Long id, PropertyDetailDto dto) {
        PropertyDetail entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PropertyDetail not found"));

        Listing listing = listingRepository.findById(dto.getListingId())
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        // 기존 엔티티의 필드만 수정
        entity.setListing(listing);
        entity.setPropertyType(dto.getPropertyType());
        entity.setLayoutType(dto.getLayoutType());
        entity.setExclusiveArea(dto.getExclusiveArea());
        entity.setBathroomCount(dto.getBathroomCount());
        entity.setFloorNumber(dto.getFloorNumber());
        entity.setTotalFloors(dto.getTotalFloors());
        entity.setBuiltAt(dto.getBuiltAt());
        entity.setHouseholdCount(dto.getHouseholdCount());

        return PropertyDetailMapper.toDto(repository.save(entity));
    }


    public void delete(Long id) {
        repository.deleteById(id);
    }
}
