package com.project.timeline.service;

import com.project.timeline.model.*;
import com.project.timeline.repository.CommentRepository;
import com.project.timeline.repository.LikeTableRepository;
import com.project.timeline.repository.PostRepository;
import com.project.timeline.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TimelineServiceTest {

    @Test
    public void testIfBodyIsPresentValidateMethodReturnTrue() {
        Post post = new Post();
        post.setBody("Test Body");
        PostRepository postRepository = mock(PostRepository.class);
        TimelineService timelineService = new TimelineService(postRepository, null, null, null);
        assertTrue(timelineService.validate(post));
    }

    @Test
    public void testIfBodyIsNotPresentValidateMethodReturnFalse() {
        Post post = new Post();
        post.setBody("");
        PostRepository postRepository = mock(PostRepository.class);
        TimelineService timelineService = new TimelineService(postRepository, null, null, null);
        assertFalse(timelineService.validate(post));
    }

    @Test
    public void testIfPostIsValidSavePost() {
        PostRepository postRepository = mock(PostRepository.class);
        Post post = new Post();
        post.setBody("Test Body");
        TimelineService timelineService = new TimelineService(postRepository, null, null, null);
        timelineService.create(post);
        verify(postRepository, times(1)).save(post);

    }


    @Test
    public void testIfToggleLikeIsAddingLike() {
        User user = new User();
        user.setId(1);
        Post post = new Post();
        post.setId(1);
        LikeTableRepository likeTableRepository = mock(LikeTableRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        PostRepository postRepository = mock(PostRepository.class);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(likeTableRepository.findByPostAndLikedBy(post, user)).thenReturn(Collections.emptyList());
        LikeTable likeTable = new LikeTable();
        likeTable.setPost(post);
        likeTable.setLikedBy(user);
        TimelineService timelineService = new TimelineService(postRepository, userRepository, likeTableRepository, null);
        timelineService.toggleLike(likeTable);
        verify(likeTableRepository, times(1)).save(likeTable);
    }

    @Test
    public void testIfToggleLikeIsDeletingLike() {
        User user = new User();
        user.setId(1);
        Post post = new Post();
        post.setId(1);
        LikeTable likeTable = new LikeTable();
        likeTable.setPost(post);
        likeTable.setLikedBy(user);
        likeTable.setId(1);
        LikeTableRepository likeTableRepository = mock(LikeTableRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        PostRepository postRepository = mock(PostRepository.class);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(likeTableRepository.findByPostAndLikedBy(post, user)).thenReturn(Collections.singletonList(likeTable));
        TimelineService timelineService = new TimelineService(postRepository, userRepository, likeTableRepository, null);
        timelineService.toggleLike(likeTable);
        verify(likeTableRepository, times(1)).deleteById(1);
    }

    @Test
    public void testIfPostIsPresentCalculateCount() {
        PostRepository postRepository = mock(PostRepository.class);
        LikeTableRepository likeTableRepository = mock(LikeTableRepository.class);
        Post post = new Post();
        post.setId(1);
        TimelineService timelineService = new TimelineService(postRepository, null, likeTableRepository, null);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        timelineService.countLikesInAPost(1);

        verify(likeTableRepository, times(1)).findByPost(post);
    }

    @Test
    public void testIfPostIsNotPresentDontCalculateCount() {
        PostRepository postRepository = mock(PostRepository.class);
        LikeTableRepository likeTableRepository = mock(LikeTableRepository.class);
        Post post = new Post();
        post.setId(1);
        TimelineService timelineService = new TimelineService(postRepository, null, likeTableRepository, null);
        when(postRepository.findById(1)).thenReturn(Optional.empty());
        timelineService.countLikesInAPost(1);
        verify(likeTableRepository, times(0)).findByPost(post);
    }

    @Test
    public void testIfUserHasLikedPostReturnTrue() {
        PostRepository postRepository = mock(PostRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        LikeTableRepository likeTableRepository = mock(LikeTableRepository.class);
        User user = new User();
        Post post = new Post();
        LikeTable likeTable = new LikeTable();
        TimelineService timelineService = new TimelineService(postRepository, userRepository, likeTableRepository, null);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(likeTableRepository.findByPostAndLikedBy(post, user)).thenReturn(Collections.singletonList(likeTable));
        assertTrue(timelineService.hasUserLikedPost(1, 1));
        verify(likeTableRepository, times(1)).findByPostAndLikedBy(post, user);
    }

    @Test
    public void testIfUserHasNotLikedPostReturnFalse() {
        PostRepository postRepository = mock(PostRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        LikeTableRepository likeTableRepository = mock(LikeTableRepository.class);
        User user = new User();
        Post post = new Post();
        LikeTable likeTable = new LikeTable();
        TimelineService timelineService = new TimelineService(postRepository, userRepository, likeTableRepository, null);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        assertFalse(timelineService.hasUserLikedPost(1, 1));
        verify(likeTableRepository, times(1)).findByPostAndLikedBy(post, user);
    }

    @Test
    public void testIfUserIsNotPresentHasLikedPostReturnFalse() {
        PostRepository postRepository = mock(PostRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        LikeTableRepository likeTableRepository = mock(LikeTableRepository.class);
        Post post = new Post();
        User user = new User();
        LikeTable likeTable = new LikeTable();
        TimelineService timelineService = new TimelineService(postRepository, userRepository, likeTableRepository, null);
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        assertFalse(timelineService.hasUserLikedPost(1, 1));
        verify(likeTableRepository, times(0)).findByPostAndLikedBy(post, user);
    }

    @Test
    public void testIfPostIsNotPresentHasLikedPostReturnFalse() {
        PostRepository postRepository = mock(PostRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        LikeTableRepository likeTableRepository = mock(LikeTableRepository.class);
        User user = new User();
        Post post = new Post();
        LikeTable likeTable = new LikeTable();
        TimelineService timelineService = new TimelineService(postRepository, userRepository, likeTableRepository, null);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.findById(1)).thenReturn(Optional.empty());
        assertFalse(timelineService.hasUserLikedPost(1, 1));
        verify(likeTableRepository, times(0)).findByPostAndLikedBy(post, user);
    }

    @Test
    public void testAddCommentIfUserAndPostAreValid() {
        User user = new User();
        user.setId(1);
        Post post = new Post();
        post.setId(1);
        Comment comment = new Comment();
        comment.setCommentedBy(user);
        comment.setPost(post);
        UserRepository userRepository = mock(UserRepository.class);
        PostRepository postRepository = mock(PostRepository.class);
        CommentRepository commentRepository = mock(CommentRepository.class);
        TimelineService timelineService = new TimelineService(postRepository, userRepository, null, commentRepository);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        timelineService.addComment(comment);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    public void testAddCommentIfUserIsInvalidAndPostAreValid() {
        User user = new User();
        user.setId(1);
        Post post = new Post();
        post.setId(1);
        Comment comment = new Comment();
        comment.setCommentedBy(user);
        comment.setPost(post);
        UserRepository userRepository = mock(UserRepository.class);
        PostRepository postRepository = mock(PostRepository.class);
        CommentRepository commentRepository = mock(CommentRepository.class);
        TimelineService timelineService = new TimelineService(postRepository, userRepository, null, commentRepository);
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        timelineService.addComment(comment);
        verify(commentRepository, times(0)).save(comment);
    }

    @Test
    public void testAddCommentIfUserIsValidAndPostIsInvalid() {
        User user = new User();
        user.setId(1);
        Post post = new Post();
        post.setId(1);
        Comment comment = new Comment();
        comment.setCommentedBy(user);
        comment.setPost(post);
        UserRepository userRepository = mock(UserRepository.class);
        PostRepository postRepository = mock(PostRepository.class);
        CommentRepository commentRepository = mock(CommentRepository.class);
        TimelineService timelineService = new TimelineService(postRepository, userRepository, null, commentRepository);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.findById(1)).thenReturn(Optional.empty());
        timelineService.addComment(comment);
        verify(commentRepository, times(0)).save(comment);
    }

    @Test
    public void testGetCommentsForPostForValidPostId() {
        Post post = new Post();
        CommentRepository commentRepository = mock(CommentRepository.class);
        PostRepository postRepository = mock(PostRepository.class);
        TimelineService timelineService = new TimelineService(postRepository, null, null, commentRepository);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        assertEquals(Collections.emptyList(), timelineService.getCommentsForPost(1));
        verify(commentRepository, times(1)).findByPost(post);
    }

    @Test
    public void testGetTimelineOfUserForValidUserAndValidPost() {
        User user = new User();
        user.setId(1);
        user.setFirstName("FirstName");
        Post post = new Post();
        post.setId(2);
        post.setBody("Body");
        PostRepository postRepository = mock(PostRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        LikeTableRepository likeTableRepository = mock(LikeTableRepository.class);
        TimelineService timelineService = new TimelineService(postRepository, userRepository, likeTableRepository, null);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.findByCreatedBy(user)).thenReturn(Collections.singletonList(post));
        when(likeTableRepository.countLikesInAPost(2)).thenReturn(0);
        when(likeTableRepository.findByPostAndLikedBy(post, user)).thenReturn(Collections.emptyList());
        Collections.singletonList(new PostWrapper(user.getId(), user.getFirstName(), post.getId(), 0, post.getBody()));
        assertEquals(
                Collections.singletonList(new PostWrapper(user.getId(), user.getFirstName(), post.getId(), 0, post.getBody())),
                timelineService.getTimelineOfUser(user.getId()));

        verify(userRepository).findById(user.getId());
        verify(postRepository).findByCreatedBy(user);
        verify(likeTableRepository).countLikesInAPost(post.getId());
        verify(likeTableRepository).findByPostAndLikedBy(post,user);

    }


}