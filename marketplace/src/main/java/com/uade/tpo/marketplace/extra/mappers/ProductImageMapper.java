package com.uade.tpo.marketplace.extra.mappers;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.marketplace.entity.basic.Product;
import com.uade.tpo.marketplace.entity.basic.ProductImage;
import com.uade.tpo.marketplace.entity.dto.create.ProductImageCreateDto;
import com.uade.tpo.marketplace.entity.dto.response.ProductImageResponseDto;
import com.uade.tpo.marketplace.entity.dto.update.ProductImageUpdateDto;
import com.uade.tpo.marketplace.exceptions.ImageProcessingException;
import com.uade.tpo.marketplace.exceptions.InvalidFileException;

@Component
public class ProductImageMapper {

    public ProductImage toEntity(ProductImageCreateDto dto) {
        if (dto == null) return null;

        ProductImage img = new ProductImage();
        img.setName(dto.name());

        if (dto.productId() != null) {
            Product p = new Product();
            p.setId(dto.productId());
            img.setProduct(p);
        }

        MultipartFile file = dto.file();
        if (file != null && !file.isEmpty()) {
            img.setImage(multipartFileToBlob(file));
        }

        img.setPrimary(dto.isPrimary());

        return img;
    }



    public void updateFromDto(ProductImageUpdateDto dto, ProductImage entity) {
        if (dto == null || entity == null) return;

        if (dto.name() != null) {
            entity.setName(dto.name());
        }

        MultipartFile file = dto.file();
        if (file != null && !file.isEmpty()) {
            entity.setImage(multipartFileToBlob(file));
        }
        entity.setPrimary(dto.isPrimary());
    }


    public ProductImageResponseDto toResponse(ProductImage img) {
        if (img == null) return null;

        Long productId = img.getProduct() != null ? safeGetId(img.getProduct()) : null;
        String base64File = null;

        Blob blob = img.getImage();
        if (blob != null) {
            base64File = blobToBase64(blob);
        }

        return new ProductImageResponseDto(
            img.getId(),
            productId,
            img.getName(),
            img.isPrimary(),
            base64File

        );
    }

    public Product productFromId(Long id){
        if (id == null) return null;
        Product p = new Product();
        p.setId(id);
        return p;
    }


    private Blob multipartFileToBlob(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            return new SerialBlob(bytes);
        } catch (IOException | SQLException e) {
            throw new ImageProcessingException("Error al convertir MultipartFile a Blob", e);
        }
    }


    private String blobToBase64(Blob blob) {
        try {
            long length = blob.length();
            if (length == 0) return null;
            if (length > Integer.MAX_VALUE) {
                throw new InvalidFileException("Blob demasiado grande para convertir a byte[]");
            }
            byte[] bytes = blob.getBytes(1, (int) length);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (SQLException e) {
            throw new ImageProcessingException("Error al leer los bytes del Blob", e);
        }
    }

    private Long safeGetId(Product p) {
        try { return p.getId(); } catch (Exception e) { return null; }
    }

}