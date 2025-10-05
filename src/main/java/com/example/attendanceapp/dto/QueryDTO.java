package com.example.attendanceapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;
@JsonInclude(JsonInclude.Include.ALWAYS)
public class QueryDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String text;
    private LocalDateTime timestamp;
    private List<ReplyDTO> replies;
    private int reportCount; // ✅ NEW

    public QueryDTO() {}

    public QueryDTO(Long id, Long userId, String userName, String text, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.text = text;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public List<ReplyDTO> getReplies() { return replies; }
    public void setReplies(List<ReplyDTO> replies) { this.replies = replies; }

    public int getReportCount() { return reportCount; } // ✅ NEW
    public void setReportCount(int reportCount) { this.reportCount = reportCount; } // ✅ NEW
}
