package com.techservback.service;

import org.springframework.stereotype.Service;

import com.techservback.repository.IContactRepository;
import com.techservback.repository.model.Contact;

@Service
public class ContactServiceImpl implements IContactService {

    private final IContactRepository contactRepository;

    public ContactServiceImpl(IContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Contact createContact(Contact contact) {
        contact.setId(null);
        if (contact.getName() != null) {
            contact.setName(contact.getName().trim());
        }
        if (contact.getEmail() != null) {
            contact.setEmail(contact.getEmail().trim());
        }
        if (contact.getSubject() != null) {
            contact.setSubject(contact.getSubject().trim());
        }
        if (contact.getMessage() != null) {
            contact.setMessage(contact.getMessage().trim());
        }

        return contactRepository.save(contact);
    }
}
