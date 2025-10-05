package com.example.attendanceapp.service.impl;

import com.example.attendanceapp.dto.QueryDTO;
import com.example.attendanceapp.dto.ReplyDTO;
import com.example.attendanceapp.entity.Query;
import com.example.attendanceapp.entity.Reply;
import com.example.attendanceapp.entity.User;
import com.example.attendanceapp.repository.QueryRepository;
import com.example.attendanceapp.repository.ReplyRepository;
import com.example.attendanceapp.repository.UserRepository;
import com.example.attendanceapp.repository.VoteRepository;
import com.example.attendanceapp.service.QueryService;
import com.example.attendanceapp.util.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QueryServiceImpl implements QueryService {

    private final QueryRepository queryRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepo;
    private final EmailSender emailSender;

    @Autowired
    public QueryServiceImpl(QueryRepository queryRepository,
                            ReplyRepository replyRepository,
                            UserRepository userRepository,
                            VoteRepository voteRepo,
                            EmailSender emailSender) {
        this.queryRepository = queryRepository;
        this.replyRepository = replyRepository;
        this.userRepository = userRepository;
        this.voteRepo = voteRepo;
        this.emailSender = emailSender;
    }

    private QueryDTO toDto(Query q) {
        QueryDTO dto = new QueryDTO(
                q.getId(),
                q.getUser() != null ? q.getUser().getId() : null,
                q.getUser() != null ? q.getUser().getName() : null,
                q.getText(),
                q.getTimestamp()
        );

        List<ReplyDTO> replies = q.getReplies().stream()
                .map(r -> {
                    int likes = voteRepo.countByReplyIdAndType(r.getId(), "like");
                    int dislikes = voteRepo.countByReplyIdAndType(r.getId(), "dislike");

                    return new ReplyDTO(
                            r.getId(),
                            r.getUser() != null ? r.getUser().getId() : null,
                            r.getUser() != null ? r.getUser().getName() : null,
                            r.getText(),
                            r.getTimestamp(),
                            likes,
                            dislikes
                    );
                })
                .collect(Collectors.toList());

        dto.setReplies(replies);
        dto.setReportCount(q.getReportCount()); // ✅ Include reportCount for frontend
        return dto;
    }

    @Override
    public QueryDTO postQuery(Long userId, String text) {
        List<String> bannedWords = List.of(
                "madda", "sulli", "lavada", "lanja", "sulliga", "gudha", "gudhamuyy", "maddaguduv",
                "fuck", "fuckoff", "fuckyou", "fuck off", "bastardd", "loveyou", "stupid",
                "killyou", "kill", "lanjodaka"
        );
        String lowerText = text.toLowerCase();
        boolean containsAbuse = bannedWords.stream().anyMatch(lowerText::contains);
        if (containsAbuse) throw new RuntimeException("❌ Query contains inappropriate language");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Query q = new Query();
        q.setUser(user);
        q.setText(text);
        q.setTimestamp(LocalDateTime.now());

        Query saved = queryRepository.save(q);
        return toDto(saved);
    }

    @Override
    public List<QueryDTO> getAllQueries(String search, String sort) {
        List<Query> queries = queryRepository.findAll();

        if (search != null && !search.isBlank()) {
            queries = queries.stream()
                    .filter(q -> q.getText().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if ("newest".equalsIgnoreCase(sort)) {
            queries.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        } else if ("oldest".equalsIgnoreCase(sort)) {
            queries.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
        }

        return queries.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<QueryDTO> getQueriesByUser(Long userId) {
        return queryRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReplyDTO replyToQuery(Long queryId, Long userId, String text) {
        Query q = queryRepository.findById(queryId)
                .orElseThrow(() -> new RuntimeException("Query not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Reply r = new Reply();
        r.setQuery(q);
        r.setUser(user);
        r.setText(text);
        r.setTimestamp(LocalDateTime.now());

        Reply saved = replyRepository.save(r);
        q.getReplies().add(saved);
        queryRepository.save(q);

        emailSender.sendQueryReplyNotification(q.getUser().getEmail(), q.getText());

        int likes = voteRepo.countByReplyIdAndType(saved.getId(), "like");
        int dislikes = voteRepo.countByReplyIdAndType(saved.getId(), "dislike");

        return new ReplyDTO(saved.getId(), user.getId(), user.getName(), saved.getText(), saved.getTimestamp(), likes, dislikes);
    }

    @Override
    public QueryDTO editQuery(Long queryId, Long userId, String text) {
        Query query = queryRepository.findById(queryId).orElseThrow(() -> new RuntimeException("Query not found"));
        if (!query.getUser().getId().equals(userId)) throw new RuntimeException("Unauthorized");
        query.setText(text);
        return toDto(queryRepository.save(query));
    }

    @Override
    public String deleteQuery(Long queryId, Long userId) {
        Query query = queryRepository.findById(queryId).orElseThrow(() -> new RuntimeException("Query not found"));
        if (!query.getUser().getId().equals(userId)) throw new RuntimeException("Unauthorized");
        queryRepository.delete(query);
        return "Query deleted";
    }

    @Override
    public String reportQuery(Long queryId, Long userId) {
        Query query = queryRepository.findById(queryId)
                .orElseThrow(() -> new RuntimeException("Query not found"));

        query.setReportCount(query.getReportCount() + 1);
        queryRepository.save(query);

        return "Reported";
    }
}
