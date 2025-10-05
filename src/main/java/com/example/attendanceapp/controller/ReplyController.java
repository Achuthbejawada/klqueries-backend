package com.example.attendanceapp.controller;

import com.example.attendanceapp.dto.ReplyDTO;
import com.example.attendanceapp.service.ReplyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/replies")
@CrossOrigin(origins = "*")
public class ReplyController {

    private final ReplyService replyService;
    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PutMapping("/{replyId}")
    public ReplyDTO editReply(@PathVariable Long replyId,
                              @RequestParam Long userId,
                              @RequestParam String text) {
        return replyService.editReply(replyId, userId, text);
    }

    @DeleteMapping("/{replyId}")
    public String deleteReply(@PathVariable Long replyId,
                              @RequestParam Long userId) {
        return replyService.deleteReply(replyId, userId);
    }

    @PostMapping("/{replyId}/vote")
    public ReplyDTO voteReply(@PathVariable Long replyId,
                              @RequestParam Long userId,
                              @RequestParam String type) {
        return replyService.voteReply(replyId, userId, type); // "like", "dislike", or "none"
    }

    @PostMapping("/{replyId}/reply")
    public ReplyDTO replyToReply(@PathVariable Long replyId,
                                 @RequestParam Long userId,
                                 @RequestParam String text) {
        return replyService.replyToReply(replyId, userId, text);
    }
}
