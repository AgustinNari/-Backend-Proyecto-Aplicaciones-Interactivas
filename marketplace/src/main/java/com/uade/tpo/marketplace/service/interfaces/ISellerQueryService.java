package com.uade.tpo.marketplace.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.marketplace.entity.dto.request.SellerFilter;
import com.uade.tpo.marketplace.entity.dto.response.SellerListDTO;

public interface ISellerQueryService {
    Page<SellerListDTO> search(SellerFilter filter, Pageable pageable);
}