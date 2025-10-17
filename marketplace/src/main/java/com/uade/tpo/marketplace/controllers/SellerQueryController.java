package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.dto.request.SellerFilter;
import com.uade.tpo.marketplace.entity.dto.response.SellerListDTO;
import com.uade.tpo.marketplace.service.interfaces.ISellerQueryService;

@RestController
@RequestMapping("/api/v1/sellers")
public class SellerQueryController {

    @Autowired
    private ISellerQueryService sellerQueryService;

    @GetMapping("/filtered")
    public ResponseEntity<Page<SellerListDTO>> list(
            SellerFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        Sort s;
        try {
            String[] parts = sort.split(",");
            if (parts.length == 2) {
                s = Sort.by(Sort.Direction.fromString(parts[1].trim()), parts[0].trim());
            } else {
                s = Sort.by(Sort.Direction.DESC, "createdAt");
            }
        } catch (Exception ex) {
            s = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        Pageable pageable = PageRequest.of(page, size, s);
        Page<SellerListDTO> result = sellerQueryService.search(filter, pageable);
        return ResponseEntity.ok(result);
    }
}
