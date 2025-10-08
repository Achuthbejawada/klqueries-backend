package com.example.attendanceapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.OffsetDateTime;

public class ReplyDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String text;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime timestamp;

    private int likes;
    private int dislikes;
    private String userVote; // optional: "like", "dislike", or null

    public ReplyDTO() {}

    public ReplyDTO(Long id, Long userId, String userName, String text, OffsetDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.text = text;
        this.timestamp = timestamp;
    }

    public ReplyDTO(Long id, Long userId, String userName, String text, OffsetDateTime timestamp, int likes, int dislikes) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.text = text;
        this.timestamp = timestamp;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public OffsetDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public int getDislikes() { return dislikes; }
    public void setDislikes(int dislikes) { this.dislikes = dislikes; }

    public String getUserVote() { return userVote; }
    public void setUserVote(String userVote) { this.userVote = userVote; }
}
