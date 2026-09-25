package az.librarycrudapi.service.impl;

import az.librarycrudapi.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final Path fileStorageLocation;
    private final List<String> allowedMimeTypes = List.of("image/jpeg", "image/png", "application/pdf");
    private final long maxFileSize = 5 * 1024 * 1024;

    public FileServiceImpl() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            log.error("Fayl saxlama qovlugu yaradıla bilmedi!", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {
        log.info("Yeni fayl yukleme sorgusu daxil oldu. Fayl adi: {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            log.error("Yukleme ugursuz oldu: Fayl bosdur");
            throw new RuntimeException("Fayl bosdur");
        }

        if (file.getSize() > maxFileSize) {
            log.error("Yukleme ugursuz oldu: Maksimum olsu 5MB olmalidir. Fayl olcusu: {}", file.getSize());
            throw new RuntimeException("Fayl olcusu maksimum 5MB ola biler");
        }

        try (InputStream inputStream = file.getInputStream()) {
            String detectedType = URLConnection.guessContentTypeFromStream(inputStream);
            if (detectedType == null) {
                detectedType = file.getContentType();
            }

            if (detectedType == null || !allowedMimeTypes.contains(detectedType)) {
                log.error("Yukleme ugursuz oldu: Desteklenmeyen fayl tipi: {}", detectedType);
                throw new RuntimeException("Yalniz JPEG, PNG ve PDF fayllarina icaze verilir");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("Fayl ugurla yaddasa yazildi. Yeni unikal adi: {}", uniqueFilename);
            return uniqueFilename;
        } catch (IOException e) {
            log.error("Fayl kopyalanarken xeta bas verdi", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource loadFileAsResource(String filename) {
        log.info("Fayl endirme sorgusu daxil oldu. Fayl adi: {}", filename);
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                log.info("Fayl ugurla tapildi ve endirilir");
                return resource;
            } else {
                log.error("Fayl tapilmadi! Qovluq yolu: {}", filename);
                throw new RuntimeException("Fayl tapilmadi");
            }
        } catch (MalformedURLException e) {
            log.error("Fayl yolu xetali formalasib", e);
            throw new RuntimeException(e);
        }
    }
}
