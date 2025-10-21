package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.dto.request.ProductFilter;
import com.uade.tpo.marketplace.entity.dto.response.ProductListDTO;
import com.uade.tpo.marketplace.service.ProductQueryService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductQueryController {

    @Autowired
    private ProductQueryService queryService;

    @GetMapping ("/filtered/active")
    public Page<ProductListDTO> listOnlyActive(
            ProductFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        Sort sortOrder = parseSort(sort);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return queryService.search(filter, pageable, true);
    }

    @GetMapping ("/filtered/all")
    public Page<ProductListDTO> listAll(
            ProductFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        Sort sortOrder = parseSort(sort);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return queryService.search(filter, pageable, false);
    }


    private Sort parseSort(String sortParam) {
    if (sortParam == null || sortParam.isBlank()) {
        return Sort.by("createdAt").descending();
    }

    String[] parts = sortParam.split(",");

    if (parts.length != 2) {
        return Sort.by("createdAt").descending();
    }

    String field = parts[0].trim();
    String direction = parts[1].trim().toLowerCase();

    if (direction.equals("asc")) {
        return Sort.by(Sort.Direction.ASC, field);
    } else {
        return Sort.by(Sort.Direction.DESC, field);
    }
}

}