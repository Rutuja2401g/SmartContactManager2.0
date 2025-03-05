package com.scm.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.entities.User;
import com.scm.entities.contact;
@Repository
public interface ContactRepo extends JpaRepository<contact,String> {

   Page<contact> findByUser(User user,Pageable pageable);
    //Custom Query method
    @Query("SELECT c FROM contact c WHERE c.user.id=:userId")
    List<contact> findByUserId(@Param("userId")String userId);

    
    Page<contact> findByUserAndNameContaining(User user, String namekeyword,Pageable pageable);

    Page<contact> findByUserAndEmailContaining( User user,String emailkeyword, Pageable pageable);

    Page<contact> findByUserAndPhoneNumberContaining( User user,String phonekeyword, Pageable pageable);
    int countByUserUserId(String userId);

    // This method counts only the favorite contacts for a given user
    int countByUserUserIdAndFavorite(String userId, boolean favorite);


    

}
