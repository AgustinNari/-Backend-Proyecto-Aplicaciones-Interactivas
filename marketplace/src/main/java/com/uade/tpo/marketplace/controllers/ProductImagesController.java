package com.uade.tpo.marketplace.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.dto.create.ProductImageCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.ProductImageResponseDto;
import com.uade.tpo.marketplace.exceptions.BadRequestException;
import com.uade.tpo.marketplace.exceptions.ResourceNotFoundException;
import com.uade.tpo.marketplace.exceptions.UnauthorizedException;
import com.uade.tpo.marketplace.service.interfaces.IProductImageService;

@RestController
@RequestMapping("product_images")
public class ProductImagesController {

    @Autowired
    private IProductImageService productImageService;

    @CrossOrigin
    @GetMapping()
    public ResponseEntity<ProductImageResponseDto> displayImage(@RequestParam("id") Long id) throws ResourceNotFoundException, Exception {
        ProductImageResponseDto image = productImageService.getImageById(id);
        return ResponseEntity.ok().body(image);
    }

    @PostMapping()
    public ProductImageResponseDto addImage(ProductImageCreateDto request, Long requestingUserId) throws IOException, SerialException, SQLException, ResourceNotFoundException, BadRequestException, UnauthorizedException {
        ProductImageResponseDto image = productImageService.addImage(request, requestingUserId);
        return image;
    }

}
