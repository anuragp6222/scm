package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {

    // Save Contacts
    Contact save(Contact contact);

    // Update Contacts
    Contact update(Contact contact);

    // get Contact
    List<Contact> getAll();

    // get Contact Ny id
    Contact getById(String id);

    // delete Contact
    void delete(String id);

    // search Contact
    Page<Contact> searchByName(String name, int size, int page, String sortBy, String order, User user);

    Page<Contact> searchByEmail(String email, int size, int page, String sortBy, String order, User user);

    Page<Contact> searchByPhone(String phone, int size, int page, String sortBy, String order, User user);

    // get Contact By User id
    List<Contact> getByUserId(String userid);

    Page<Contact> getByUser(User user, int page, int size, String sortField, String sortDirection);
}
