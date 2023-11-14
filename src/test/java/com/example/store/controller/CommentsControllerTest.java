package com.example.store.controller;

import com.example.store.dto.Comment;
import com.example.store.dto.Comments;
import com.example.store.service.CommentsService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.security.Principal;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CommentsController.class)
public class CommentsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentsService commentsService;

    @Test
    @WithMockUser
    void testGetComments() throws Exception {
        when(commentsService.getAllComments(1)).thenReturn(new Comments());

        mockMvc.perform(get("/ads/{id}/comments", 1))
                .andExpect(status().isOk());

        verify(commentsService, times(1)).getAllComments(any(Integer.class));
    }

    @Test
    @WithMockUser
    void testPostComments() throws Exception {
        Comment comment = new Comment();

        comment.setPk(1);
        comment.setText("Example text");
        comment.setAuthor(1);

        JSONObject commentObject = new JSONObject();
        commentObject.put("text", "Example text");
        commentObject.put("author", "1");

        when(commentsService.addComment(any(Comment.class), any(Principal.class), any(Integer.class))).thenReturn(new Comment());

        mockMvc.perform(post("/ads/{id}/comments", 1)
                        .content(commentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(commentsService, times(1)).addComment(any(Comment.class), any(Principal.class), any(Integer.class));
    }

    @Test
    @WithMockUser
    void testDeleteComment() throws Exception {
        mockMvc.perform(delete("/ads/{id}/comments/{commentId}", 1, 1)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(commentsService, times(1)).removeComment(any(Integer.class));
    }

    @Test
    @WithMockUser
    void testPatchComment() throws Exception {
        Comment comment = new Comment();

        comment.setPk(1);
        comment.setText("Example text");
        comment.setAuthor(1);

        JSONObject commentObject = new JSONObject();
        commentObject.put("text", "Example text");
        commentObject.put("author", "1");

        when(commentsService.updateComment(1, comment)).thenReturn(new Comment());

        mockMvc.perform(patch("/ads/{id}/comments/{commentId}", 1, 1)
                        .content(commentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(commentsService, times(1)).updateComment(any(Integer.class), any(Comment.class));
    }

}