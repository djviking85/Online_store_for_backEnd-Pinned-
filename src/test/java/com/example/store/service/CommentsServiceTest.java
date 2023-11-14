package com.example.store.service;

import com.example.store.dto.Comment;
import com.example.store.dto.Comments;
import com.example.store.entity.AdEntity;
import com.example.store.entity.CommentEntity;
import com.example.store.entity.UserEntity;
import com.example.store.mapper.CommentMapper;
import com.example.store.repository.AdEntityRepository;
import com.example.store.repository.CommentEntityRepository;
import com.example.store.repository.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CommentsServiceTest {
    @Mock
    private CommentEntityRepository commentEntityRepository;

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private AdEntityRepository adEntityRepository;

    @Mock
    private Principal principal;

    @InjectMocks
    private CommentsService commentsService;

    Comment comment = new Comment();
    UserEntity userEntity = new UserEntity();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddComment() {
        when(principal.getName()).thenReturn(userEntity.getEmail());
        when(userEntityRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

        AdEntity adEntity = new AdEntity();
        adEntity.setPk(1);
        when(adEntityRepository.findById(adEntity.getPk())).thenReturn(Optional.of(adEntity));

        CommentEntity commentEntity = new CommentEntity();
        when(commentMapper.toEntity(comment)).thenReturn(commentEntity);

        Comment savedComment = commentsService.addComment(comment, principal, adEntity.getPk());

        assertNotNull(savedComment);
        verify(userEntityRepository, times(1)).findByEmail(userEntity.getEmail());
        verify(adEntityRepository, times(1)).findById(adEntity.getPk());
        verify(commentMapper, times(1)).toEntity(comment);
        verify(commentEntityRepository, times(1)).save(commentEntity);
    }

    @Test
    void testDeleteComment() {
        Integer commentId = 1;

        doNothing().when(commentEntityRepository).deleteById(commentId);

        commentsService.removeComment(commentId);

        verify(commentEntityRepository, times(1)).deleteById(commentId);
    }

    @Test
    void testUpdateComment() throws Exception {
        Integer id = 1;
        Comment updateComment = new Comment();
        updateComment.setText("Updated text");

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setText("Original text");

        when(commentEntityRepository.findById(id)).thenReturn(Optional.of(commentEntity));
        when(commentMapper.toEntity(updateComment)).thenReturn(commentEntity);
        when(commentEntityRepository.save(commentEntity)).thenReturn(commentEntity);
        when(commentMapper.toDTO(commentEntity)).thenReturn(updateComment);

        Comment updatedComment = commentsService.updateComment(id, updateComment);

        assertNotNull(updatedComment);
        assertEquals(updateComment, updatedComment);
        verify(commentEntityRepository, times(1)).findById(id);
        verify(commentMapper, times(1)).toEntity(updateComment);
        verify(commentEntityRepository, times(1)).save(commentEntity);
        verify(commentMapper, times(1)).toDTO(commentEntity);
    }

    @Test
    void testGetAllComments() {
        int adId = 1;

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setPk(1);
        commentEntity.setText("Example text");

        List<CommentEntity> commentEntityList = new ArrayList<>();
        commentEntityList.add(commentEntity);

        when(commentEntityRepository.findAllByAdEntity_Pk(adId)).thenReturn(commentEntityList);

        Comment comment = new Comment();
        comment.setPk(commentEntity.getPk());
        comment.setText(commentEntity.getText());

        when(commentMapper.commentsListToDTO(commentEntityList)).thenReturn(Collections.singletonList(comment));

        Comments comments = commentsService.getAllComments(adId);

        assertNotNull(comments);
        assertEquals(1, comments.getCount());
        assertEquals(Collections.singletonList(comment), comments.getResults());
        verify(commentEntityRepository, times(1)).findAllByAdEntity_Pk(adId);
        verify(commentMapper, times(1)).commentsListToDTO(commentEntityList);
    }
}
