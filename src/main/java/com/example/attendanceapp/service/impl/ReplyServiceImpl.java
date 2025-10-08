package com.example.attendanceapp.service.impl;

import com.example.attendanceapp.dto.ReplyDTO;
import com.example.attendanceapp.entity.Reply;
import com.example.attendanceapp.entity.User;
import com.example.attendanceapp.entity.Vote;
import com.example.attendanceapp.repository.ReplyRepository;
import com.example.attendanceapp.repository.UserRepository;
import com.example.attendanceapp.repository.VoteRepository;
import com.example.attendanceapp.service.ReplyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
@Transactional
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;
    private final VoteRepository voteRepo;
    private final UserRepository userRepo;

    public ReplyServiceImpl(ReplyRepository replyRepository, VoteRepository voteRepo, UserRepository userRepo) {
        this.replyRepository = replyRepository;
        this.voteRepo = voteRepo;
        this.userRepo = userRepo;
    }

    @Override
    public ReplyDTO editReply(Long replyId, Long userId, String text) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("Reply not found"));
        if (!reply.getUser().getId().equals(userId)) throw new RuntimeException("Unauthorized");
        reply.setText(text);
        Reply updated = replyRepository.save(reply);
        return new ReplyDTO(updated.getId(), userId, reply.getUser().getName(), updated.getText(), updated.getTimestamp(), updated.getLikes(), updated.getDislikes());
    }

    @Override
    public String deleteReply(Long replyId, Long userId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("Reply not found"));
        if (!reply.getUser().getId().equals(userId)) throw new RuntimeException("Unauthorized");
        replyRepository.delete(reply);
        return "Reply deleted";
    }

    @Override
    public ReplyDTO voteReply(Long replyId, Long userId, String type) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("Reply not found"));

        Optional<Vote> existing = voteRepo.findByUserIdAndReplyId(userId, replyId);

        if (type.equals("none")) {
            existing.ifPresent(voteRepo::delete);
        } else {
            if (existing.isPresent()) {
                existing.get().setType(type);
                voteRepo.save(existing.get());
            } else {
                Vote vote = new Vote();
                User user = userRepo.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                vote.setUser(user);
                vote.setReply(reply);
                vote.setType(type);
                voteRepo.save(vote);
            }
        }

        int likes = voteRepo.countByReplyIdAndType(replyId, "like");
        int dislikes = voteRepo.countByReplyIdAndType(replyId, "dislike");

        reply.setLikes(likes);
        reply.setDislikes(dislikes);
        replyRepository.save(reply);

        return new ReplyDTO(reply.getId(), reply.getUser().getId(), reply.getUser().getName(), reply.getText(), reply.getTimestamp(), likes, dislikes);
    }

    @Override
    public ReplyDTO replyToReply(Long parentReplyId, Long userId, String text) {
        Reply parent = replyRepository.findById(parentReplyId)
                .orElseThrow(() -> new RuntimeException("Parent reply not found"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reply r = new Reply();
        r.setParent(parent);
        r.setQuery(parent.getQuery());
        r.setUser(user);
        r.setText(text);
        r.setTimestamp(OffsetDateTime.now(ZoneOffset.ofHoursMinutes(5, 30)));

        Reply saved = replyRepository.save(r);
        int likes = voteRepo.countByReplyIdAndType(saved.getId(), "like");
        int dislikes = voteRepo.countByReplyIdAndType(saved.getId(), "dislike");

        return new ReplyDTO(saved.getId(), user.getId(), user.getName(), saved.getText(), saved.getTimestamp(), likes, dislikes);
    }
}
