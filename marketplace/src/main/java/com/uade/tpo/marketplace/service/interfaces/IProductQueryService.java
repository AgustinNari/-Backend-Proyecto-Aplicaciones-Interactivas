package com.uade.tpo.marketplace.service.interfaces;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.marketplace.entity.dto.request.ProductFilter;
import com.uade.tpo.marketplace.entity.dto.response.ProductListDTO;

public interface IProductQueryService {

    Page<ProductListDTO> search(ProductFilter filter, Pageable pageable);

}
