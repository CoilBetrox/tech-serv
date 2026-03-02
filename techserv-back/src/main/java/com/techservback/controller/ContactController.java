package com.techservback.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techservback.repository.model.Contact;
import com.techservback.service.IContactService;

@RestController
@RequestMapping("/api/v1/contacts")
public class ContactController {

    private final IContactService contactService;

    public ContactController(IContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<?> createContact(@RequestBody Contact contact) {
        if (contact == null) {
            return ResponseEntity.badRequest().body("Request body is required");
        }

        String name = contact.getName() == null ? "" : contact.getName().trim();
        String email = contact.getEmail() == null ? "" : contact.getEmail().trim();
        String subject = contact.getSubject() == null ? "" : contact.getSubject().trim();
        String message = contact.getMessage() == null ? "" : contact.getMessage().trim();

        if (name.isEmpty() || email.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            return ResponseEntity.badRequest().body("name, email, subject and message are required");
        }

        Contact toCreate = new Contact();
        toCreate.setName(name);
        toCreate.setEmail(email);
        toCreate.setSubject(subject);
        toCreate.setMessage(message);

        Contact created = contactService.createContact(toCreate);
        return ResponseEntity.ok(created);
    }
}
