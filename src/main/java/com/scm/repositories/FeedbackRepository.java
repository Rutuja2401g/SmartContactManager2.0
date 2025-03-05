package com.scm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.scm.entities.*;
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
