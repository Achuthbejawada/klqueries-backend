package com.example.attendanceapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "reply_id")
    private Reply reply;

    @Column(nullable = false)
    private String type; // "like" or "dislike"

    public Vote() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Reply getReply() { return reply; }
    public void setReply(Reply reply) { this.reply = reply; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
