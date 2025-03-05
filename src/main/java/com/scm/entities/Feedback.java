package com.scm.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback") // Maps to the MySQL table
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String message;
    private int rating;

    // Constructors
    public Feedback() {}

    public Feedback(String name, String email, String message, int rating) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.rating = rating;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
}
