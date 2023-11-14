package com.example.store.service;

import org.springframework.stereotype.Service;

@Service
public class CommentsService {
    private final CommentEntityRepository repository;
    private final CommentMapper commentMapper;
    private final UserEntityRepository userEntityRepository;
    private final AdEntityRepository adEntityRepository;

    /**
     * Constructs a CommentsService instance with the required dependencies.
     *
     * @param repository           The repository for accessing CommentEntity data.
     * @param commentMapper        Mapper for converting between Comment and CommentEntity.
     * @param userEntityRepository Repository for accessing UserEntity data.
     * @param adEntityRepository   Repository for accessing AdEntity data.
     */
    public CommentsService(CommentEntityRepository repository, CommentMapper commentMapper, UserEntityRepository userEntityRepository, AdEntityRepository adEntityRepository) {
        this.repository = repository;
        this.commentMapper = commentMapper;
        this.userEntityRepository = userEntityRepository;
        this.adEntityRepository = adEntityRepository;
    }

    /**
     * Adds a new comment to an advertisement.
     *
     * @param comment   The comment to be added.
     * @param principal The principal representing the authenticated user.
     * @param id        The ID of the advertisement.
     * @return The added Comment.
     */
    public Comment addComment(Comment comment, Principal principal, Integer id) {
        String email  = principal.getName();
        Optional<UserEntity> optionalUserEntity = userEntityRepository.findByEmail(email);
        UserEntity userEntity = optionalUserEntity.get();
        Optional<AdEntity> optionalAdEntity = adEntityRepository.findById(id);
        AdEntity ad = optionalAdEntity.get();
        Comment comment1 = new Comment();
        comment1.setAuthor(userEntity.getId());
        CommentEntity commentEntity = commentMapper.toEntity(comment);
        commentEntity.setAdEntity(ad);
        commentEntity.setUser(userEntity);
        commentEntity.setCreatedAt(LocalDateTime.now());
        repository.save(commentEntity);
        return comment;
    }

    /**
     * Removes a comment by its ID.
     *
     * @param idComment The ID of the comment to be removed.
     */
    public void removeComment(Integer idComment) {
        repository.deleteById(idComment);
    }

    /**
     * Updates a comment's text by its ID.
     *
     * @param id            The ID of the comment to be updated.
     * @param updateComment The updated comment content.
     * @return The updated Comment.
     * @throws Exception if the comment is not found.
     */
    public Comment updateComment(Integer id, Comment updateComment) throws Exception{
        Optional<CommentEntity> entity = repository.findById(id);
        if (entity.isPresent()) {
            CommentEntity newCommentEntity = entity.get();
            newCommentEntity.setText(commentMapper.toEntity(updateComment).getText());
            repository.save(newCommentEntity);
            return commentMapper.toDTO(newCommentEntity);
        }
        throw new Exception("Отзыв не найден");
    }

    /**
     * Retrieves all comments for a specific advertisement.
     *
     * @param id The ID of the advertisement.
     * @return Comments object containing the list of comments and their count.
     */
    @Transactional
    public Comments getAllComments(int id) {
        List<CommentEntity> list = repository.findAllByAdEntity_Pk(id);
        Comments comments = new Comments();
        List<Comment> result = commentMapper.commentsListToDTO(list);
        comments.setCount(result.size());
        comments.setResults(result);
        return comments;
    }
}
