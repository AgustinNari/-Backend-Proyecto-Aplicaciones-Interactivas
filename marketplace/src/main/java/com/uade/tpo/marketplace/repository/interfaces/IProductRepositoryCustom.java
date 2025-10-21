package com.uade.tpo.marketplace.repository.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.marketplace.entity.dto.request.ProductFilter;
import com.uade.tpo.marketplace.entity.dto.response.ProductListDTO;

public interface IProductRepositoryCustom {
    Page<ProductListDTO> findProductsWithAggregates(ProductFilter filter, Pageable pageable, boolean activeOnly);
}