package dev.morozan1.server.controller;

import dev.morozan1.server.exception.ForbiddenException;
import dev.morozan1.server.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping()
public class PageController {

    @Value("${static.folder.path}")
    private String staticFolderPath;

    @GetMapping({"", "/", "/index"})
    public RedirectView redirect() {
        return new RedirectView("/map");
    }

    @GetMapping({"/map", "/map/", "/about", "/about/", "/products", "/products/"})
    public ResponseEntity<byte[]> page() {
        try{
            Path filePath = Paths.get(staticFolderPath, "index.html");
            if (Files.exists(filePath)) {
                byte[] fileContent = Files.readAllBytes(filePath);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "text/html");
                return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            } else {
                throw new NotFoundException();
            }
        } catch (IOException e) {
            throw new ForbiddenException();
        }
    }
}
