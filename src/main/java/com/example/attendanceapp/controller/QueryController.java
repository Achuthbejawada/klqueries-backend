package com.example.attendanceapp.controller;

import com.example.attendanceapp.dto.QueryDTO;
import com.example.attendanceapp.dto.ReplyDTO;
import com.example.attendanceapp.service.QueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queries")
@CrossOrigin(origins = "*")
public class QueryController {

    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @PostMapping("/post")
    public QueryDTO postQuery(@RequestParam Long userId, @RequestParam String text) {
        return queryService.postQuery(userId, text);
    }

    @GetMapping
    public List<QueryDTO> getAllQueries(@RequestParam(required = false) String search,
                                        @RequestParam(required = false) String sort) {
        return queryService.getAllQueries(search, sort);
    }

    @GetMapping("/user/{userId}")
    public List<QueryDTO> getQueriesByUser(@PathVariable Long userId) {
        return queryService.getQueriesByUser(userId);
    }

    @PostMapping("/{queryId}/reply")
    public ReplyDTO replyToQuery(@PathVariable Long queryId,
                                 @RequestParam Long userId,
                                 @RequestParam String text) {
        return queryService.replyToQuery(queryId, userId, text);
    }

    @PutMapping("/{queryId}")
    public QueryDTO editQuery(@PathVariable Long queryId,
                              @RequestParam Long userId,
                              @RequestParam String text) {
        return queryService.editQuery(queryId, userId, text);
    }

    @DeleteMapping("/{queryId}")
    public String deleteQuery(@PathVariable Long queryId,
                              @RequestParam Long userId) {
        return queryService.deleteQuery(queryId, userId);
    }

    @PostMapping("/{queryId}/report")
    public String reportQuery(@PathVariable Long queryId,
                              @RequestParam Long userId) {
        return queryService.reportQuery(queryId, userId);
    }
}
