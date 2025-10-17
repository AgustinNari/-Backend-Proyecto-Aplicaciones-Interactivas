package com.uade.tpo.marketplace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.dto.request.ProductFilter;
import com.uade.tpo.marketplace.entity.dto.response.ProductListDTO;
import com.uade.tpo.marketplace.repository.interfaces.IProductRepositoryCustom;
import com.uade.tpo.marketplace.service.interfaces.IProductQueryService;

@Service
public class ProductQueryService implements IProductQueryService {

    @Autowired
    private IProductRepositoryCustom productRepositoryCustom;

    @Override
    public Page<ProductListDTO> search(ProductFilter filter, Pageable pageable) {
        return productRepositoryCustom.findProductsWithAggregates(filter, pageable);
    }

}
