package com.fds.ftpweb;

/**
 * @author fandeshan
 * @description //TODO 写点注释吧
 * @date 2024/6/11
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class FileController {

    @Value("${rootPath:/data/uf/default/}")
    private String rootPath;

    private Path getRootLocation(){
        return Paths.get(rootPath);
    }
    @GetMapping("/list")
    public List<FileInfo> listFiles(@RequestParam(defaultValue = "") String path) throws Exception {
        Path dirPath = getRootLocation().resolve(URLDecoder.decode(path, StandardCharsets.UTF_8.name()));
        try (Stream<Path> stream = Files.list(dirPath)) {
            return stream.map(p -> new FileInfo(p.getFileName().toString(), Files.isDirectory(p)))
                    .collect(Collectors.toList());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam String path, @RequestParam("files") MultipartFile[] files) throws Exception {
        Path uploadPath = getRootLocation().resolve(URLDecoder.decode(path, StandardCharsets.UTF_8.name()));
        for (MultipartFile file : files) {
            try {
                Files.copy(file.getInputStream(), uploadPath.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.ok("Files uploaded successfully");
    }

    @PostMapping("/create-directory")
    public ResponseEntity<String> createDirectory(@RequestParam String path, @RequestParam String name) throws Exception {
        Path dirPath = getRootLocation().resolve(URLDecoder.decode(path, StandardCharsets.UTF_8.name())).resolve(URLDecoder.decode(name, StandardCharsets.UTF_8.name()));
        try {
            Files.createDirectories(dirPath);
            return ResponseEntity.ok("Directory created successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/rename")
    public ResponseEntity<String> renameFile(@RequestParam String path, @RequestParam String oldName, @RequestParam String newName) throws Exception {
        Path oldFilePath = getRootLocation().resolve(URLDecoder.decode(path, StandardCharsets.UTF_8.name())).resolve(URLDecoder.decode(oldName, StandardCharsets.UTF_8.name()));
        Path newFilePath = getRootLocation().resolve(URLDecoder.decode(path, StandardCharsets.UTF_8.name())).resolve(URLDecoder.decode(newName, StandardCharsets.UTF_8.name()));
        try {
            Files.move(oldFilePath, newFilePath);
            return ResponseEntity.ok("File renamed successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam String path, @RequestParam String name) throws Exception {
        Path filePath = getRootLocation().resolve(URLDecoder.decode(path, StandardCharsets.UTF_8.name())).resolve(URLDecoder.decode(name, StandardCharsets.UTF_8.name()));
        try {
            if (Files.isDirectory(filePath)) {
                Files.walkFileTree(filePath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } else {
                Files.delete(filePath);
            }
            return ResponseEntity.ok("File or directory deleted successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    static class FileInfo {
        private String name;
        private boolean directory;

        // Constructor, getters, and setters

        public FileInfo(String name, boolean directory) {
            this.name = name;
            this.directory = directory;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isDirectory() {
            return directory;
        }

        public void setDirectory(boolean directory) {
            this.directory = directory;
        }
    }
}


