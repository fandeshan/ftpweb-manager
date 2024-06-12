package com.fds.ftpweb;

/**
 * @author fandeshan
 * @description //TODO 写点注释吧
 * @date 2024/6/11
 */

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService {

    private static final Path BASE_DIR = Paths.get("/data/file");

    public List<FileInfo> listFiles(Path path) {
        Path absPath = BASE_DIR.resolve(path).normalize();
        try (Stream<Path> stream = Files.list(absPath)) {
            return stream.map(p -> new FileInfo(p.getFileName().toString(), Files.isDirectory(p)))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return java.util.Collections.emptyList();
        }
    }

    public void uploadFiles(String path, MultipartFile[] files) throws IOException {
        Path absPath = BASE_DIR.resolve(path).normalize();
        for (MultipartFile file : files) {
            Path destination = absPath.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void createDirectory(Path path, String name) throws IOException {
        Path dirPath = BASE_DIR.resolve(path).resolve(name).normalize();
        Files.createDirectories(dirPath);
    }

    public void rename(Path path, String oldName, String newName) throws IOException {
        Path oldPath = BASE_DIR.resolve(path).resolve(oldName).normalize();
        Path newPath = BASE_DIR.resolve(path).resolve(newName).normalize();
        Files.move(oldPath, newPath);
    }
}



