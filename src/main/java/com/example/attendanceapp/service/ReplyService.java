package com.example.attendanceapp.service;

import com.example.attendanceapp.dto.ReplyDTO;

public interface ReplyService {
    ReplyDTO editReply(Long replyId, Long userId, String text);
    String deleteReply(Long replyId, Long userId);
    ReplyDTO voteReply(Long replyId, Long userId, String type); // "like", "dislike", or "none"

    ReplyDTO replyToReply(Long replyId, Long userId, String text);
}
