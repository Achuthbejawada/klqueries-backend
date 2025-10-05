package com.example.attendanceapp.service;

import com.example.attendanceapp.dto.QueryDTO;
import com.example.attendanceapp.dto.ReplyDTO;
import java.util.List;

public interface QueryService {
    QueryDTO postQuery(Long userId, String text);
    List<QueryDTO> getAllQueries(String search, String sort);
    List<QueryDTO> getQueriesByUser(Long userId);
    ReplyDTO replyToQuery(Long queryId, Long userId, String text);
    QueryDTO editQuery(Long queryId, Long userId, String text);
    String deleteQuery(Long queryId, Long userId);
    String reportQuery(Long queryId, Long userId); // ✅ NEW
}
