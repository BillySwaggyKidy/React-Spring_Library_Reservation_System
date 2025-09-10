package com.billykid.template.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billykid.template.service.AuthorService;
import com.billykid.template.utils.DTO.AuthorDTO;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping(path="/api")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService){
        this.authorService = authorService;
    }

    @GetMapping("/authors/name/{name}")
    public ResponseEntity<List<AuthorDTO>> getAuthors(@PathVariable String name, Pageable pageable) {
        List<AuthorDTO> authors = authorService.findAuthorsByName(name, pageable);
        return ResponseEntity.ok(authors);
    }

    @PostMapping("/authors/add")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public ResponseEntity<AuthorDTO> addAuthor(@RequestBody AuthorDTO author) {
        AuthorDTO newAuthor = authorService.addNewAuthor(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAuthor);
    }

    @PutMapping("/authors/update/{id}")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Integer id, @RequestBody AuthorDTO author) {
        AuthorDTO updatedAuthor = authorService.updateAuthor(id, author);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/authors/remove/{id}")
    @RolesAllowed({"EMPLOYEE", "ADMIN"})
    public ResponseEntity<AuthorDTO> removeAuthor(@PathVariable Integer id) {
        AuthorDTO deletedAuthor = authorService.removeAuthor(id);
        return ResponseEntity.ok(deletedAuthor);
    }


}
