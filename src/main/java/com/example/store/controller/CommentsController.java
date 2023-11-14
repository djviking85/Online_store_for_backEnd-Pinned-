package com.example.store.controller;
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")

public class CommentsController {
    private final CommentsService service;


    public CommentsController(CommentsService service) {
        this.service = service;
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "OK",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Comments.class)
                    )}
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getAllComments(id));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "OK",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class)
                    )}
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Integer id, @RequestBody Comment comment, Principal principal) {
        return ResponseEntity.ok(service.addComment(comment, principal, id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> deleteComment(@PathVariable Integer adId, @PathVariable Integer commentId) {
        service.removeComment(commentId);
        return ResponseEntity.ok().build();
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "OK",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class)
                    )}
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer adId, @PathVariable Integer commentId,
                                                 @RequestBody Comment updateComment) throws Exception {
        return ResponseEntity.ok(service.updateComment(commentId, updateComment));
    }
}
