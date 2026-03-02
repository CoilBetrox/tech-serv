package com.techservback.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techservback.repository.model.Contact;

public interface IContactRepository extends JpaRepository<Contact, Long> {
}
