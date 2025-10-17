package com.uade.tpo.marketplace.repository.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.marketplace.entity.dto.request.SellerFilter;
import com.uade.tpo.marketplace.entity.dto.response.SellerListDTO;

public interface IUserRepositoryCustom {
    Page<SellerListDTO> findSellersWithAggregates(SellerFilter filter, Pageable pageable);
}