package com.uade.tpo.marketplace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entity.dto.request.SellerFilter;
import com.uade.tpo.marketplace.entity.dto.response.SellerListDTO;
import com.uade.tpo.marketplace.repository.interfaces.IUserRepositoryCustom;
import com.uade.tpo.marketplace.service.interfaces.ISellerQueryService;

@Service
public class SellerQueryService implements ISellerQueryService {

    @Autowired
    private IUserRepositoryCustom userRepositoryCustom;

    @Override
    @Transactional(readOnly = true)
    public Page<SellerListDTO> search(SellerFilter filter, Pageable pageable) {
        return userRepositoryCustomSafe(filter, pageable);
    }

    private Page<SellerListDTO> userRepositoryCustomSafe(SellerFilter filter, Pageable pageable) {
        return userRepositoryCustom.findSellersWithAggregates(filter, pageable);
    }
}