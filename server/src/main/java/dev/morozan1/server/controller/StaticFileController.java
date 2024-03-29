package dev.morozan1.server.controller;

import dev.morozan1.server.exception.ForbiddenException;
import dev.morozan1.server.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/static")
public class StaticFileController {

    @Value("${static.folder.path}")
    private String staticFolderPath;

    @GetMapping("/{file}")
    public ResponseEntity<byte[]> serveStaticFile(@PathVariable String file) {
        try{
            Path filePath = Paths.get(staticFolderPath, file);
            if (Files.exists(filePath)) {
                byte[] fileContent = Files.readAllBytes(filePath);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, getMediaType(file));
                return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            } else {
                throw new NotFoundException();
            }
        } catch (IOException e) {
            throw new ForbiddenException();
        }
    }

    private String getMediaType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return switch (extension) {
            case "js" -> "text/javascript";
            case "txt" -> "text/plain";
            case "css" -> "text/css";
            default -> throw new ForbiddenException();
        };
    }
}
