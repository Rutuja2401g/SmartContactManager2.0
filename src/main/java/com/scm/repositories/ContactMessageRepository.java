package com.scm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.scm.entities.*;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
}
