package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;


import com.scm.entities.contact;
import com.scm.entities.User;


public interface ContactService {
    // save contacts
    contact save(contact contact);

    // update contact
    contact update(contact contact);

    // get contacts
    List<contact> getAll();

    // get contact by id

    contact getById(String id);

    // delete contact

    void delete(String id);

    int countTotalContacts(String userId);
    int countFavoriteContacts(String userId);

    // search contact
    Page<contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order,User user);

    Page<contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order,User user);

    Page<contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String order,User user);
            

    // get contacts by userId
    List<contact> getByUserId(String userId);
    
   

Page<contact> getByUser(User user, int page, int size, String sortBy, String direction);



}
