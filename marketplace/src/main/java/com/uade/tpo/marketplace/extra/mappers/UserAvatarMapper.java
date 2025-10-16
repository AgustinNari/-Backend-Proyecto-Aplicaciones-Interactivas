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

import com.uade.tpo.marketplace.entity.basic.User;
import com.uade.tpo.marketplace.entity.dto.response.UserAvatarResponseDto;
import com.uade.tpo.marketplace.exceptions.ImageProcessingException;
import com.uade.tpo.marketplace.exceptions.InvalidFileException;

@Component
public class UserAvatarMapper {

    public User toEntityAvatar(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        try {
            byte[] bytes = file.getBytes();
            String mime = detectMimeFromBytes(bytes);
            String clientMime = file.getContentType();
            if ((mime == null || "application/octet-stream".equals(mime)) && clientMime != null && clientMime.startsWith("image/")) {
                mime = clientMime;
            }
            User u = new User();
            u.setId(userId);
            u.setAvatar(new SerialBlob(bytes));
            u.setAvatarContentType(mime);
            return u;
        } catch (IOException | SQLException e) {
            throw new ImageProcessingException("Error al convertir MultipartFile a Blob", e);
        }
    }

    public UserAvatarResponseDto toResponse(User user) {
        if (user == null) return null;
        Blob avatar = user.getAvatar();
        if (avatar == null) return new UserAvatarResponseDto(user.getId(), null, null, null);

        String mime = user.getAvatarContentType();
        String base64 = blobToBase64(avatar);
        if ((mime == null || mime.isBlank()) && base64 != null) {
            mime = detectMimeFromBlob(avatar);
        }
        String dataUrl = (base64 != null && mime != null) ? ("data:" + mime + ";base64," + base64) : null;
        return new UserAvatarResponseDto(user.getId(), mime, base64, dataUrl);
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

    private String detectMimeFromBytes(byte[] bytes) {
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            String mime = URLConnection.guessContentTypeFromStream(is);
            if (mime != null) return mime;
        } catch (IOException ignored) {}

        if (bytes.length >= 3 && (bytes[0] & 0xFF) == 0xFF && (bytes[1] & 0xFF) == 0xD8) return "image/jpeg";
        if (bytes.length >= 4 && (bytes[0] & 0xFF) == 0x89 && bytes[1] == 'P' && bytes[2] == 'N' && bytes[3] == 'G') return "image/png";
        if (bytes.length >= 3 && bytes[0] == 'G' && bytes[1] == 'I' && bytes[2] == 'F') return "image/gif";
        if (bytes.length >= 4 && bytes[0] == '<' && (bytes[1] == '?' || bytes[1] == 's')) return "image/svg+xml";
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
