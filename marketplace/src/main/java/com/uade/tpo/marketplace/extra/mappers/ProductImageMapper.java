package com.uade.tpo.marketplace.extra.mappers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
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
            ImageTuple tuple = multipartFileToBlob(file);
            img.setImage(tuple.serialBlob());
            img.setContentType(tuple.mime());
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
            ImageTuple tuple = multipartFileToBlob(file);
            entity.setImage(tuple.serialBlob());
            entity.setContentType(tuple.mime());
        }

        entity.setPrimary(dto.isPrimary());
    }


    public ProductImageResponseDto toResponse(ProductImage img) {
        if (img == null) return null;

        Long productId = img.getProduct() != null ? safeGetId(img.getProduct()) : null;
        String base64File = null;
        String mime = img.getContentType();

        Blob blob = img.getImage();
        if (blob != null) {
            base64File = blobToBase64(blob);
            if (mime == null || mime.isBlank()) {
                mime = detectMimeFromBlob(blob);
            }
        }

        String dataUrl = (base64File != null && mime != null && !mime.isBlank())
                ? "data:" + mime + ";base64," + base64File
                : null;

        return new ProductImageResponseDto(
            img.getId(),
            productId,
            img.getName(),
            img.isPrimary(),
            base64File,
            mime,
            dataUrl
        );
    }

    public Product productFromId(Long id){
        if (id == null) return null;
        Product p = new Product();
        p.setId(id);
        return p;
    }



    private ImageTuple multipartFileToBlob(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String mime = detectMimeFromBytes(bytes);
            String client = file.getContentType();
            if ((mime == null || "application/octet-stream".equals(mime)) && client != null && client.startsWith("image/")) {
                mime = client;
            }
            return new ImageTuple(mime, new SerialBlob(bytes));
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

    private String detectMimeFromBytes(byte[] bytes) {
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            String mime = URLConnection.guessContentTypeFromStream(is);
            if (mime != null) return mime;
        } catch (IOException ignored) {}

        if (bytes.length >= 3 && (bytes[0] & 0xFF) == 0xFF && (bytes[1] & 0xFF) == 0xD8) {
            return "image/jpeg";
        }
        if (bytes.length >= 4 && (bytes[0] & 0xFF) == 0x89 && bytes[1] == 'P' && bytes[2] == 'N' && bytes[3] == 'G') {
            return "image/png";
        }
        if (bytes.length >= 3 && bytes[0] == 'G' && bytes[1] == 'I' && bytes[2] == 'F') {
            return "image/gif";
        }
        if (bytes.length >= 4 && bytes[0] == '<' && (bytes[1] == '?' || bytes[1] == 's')) {
            return "image/svg+xml";
        }
        return "application/octet-stream";
    }

    private String detectMimeFromBlob(Blob blob) {
        try (InputStream is = blob.getBinaryStream()) {
            String mime = URLConnection.guessContentTypeFromStream(is);
            if (mime != null) return mime;
        } catch (SQLException | IOException ignored) {}

        try {
            int len = (int) Math.min(blob.length(), 16);
            byte[] prefix = blob.getBytes(1, len);
            return detectMimeFromBytes(prefix);
        } catch (SQLException e) {
            return "application/octet-stream";
        }
    }

    private static record ImageTuple(String mime, SerialBlob serialBlob) {}

}