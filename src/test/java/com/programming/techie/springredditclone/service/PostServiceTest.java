package com.programming.techie.springredditclone.service;

import static java.util.Collections.emptyList;

import com.programming.techie.springredditclone.dto.PostRequest;
import com.programming.techie.springredditclone.dto.PostResponse;
import com.programming.techie.springredditclone.mapper.PostMapper;
import com.programming.techie.springredditclone.model.Post;
import com.programming.techie.springredditclone.model.Subreddit;
import com.programming.techie.springredditclone.model.User;
import com.programming.techie.springredditclone.repository.PostRepository;
import com.programming.techie.springredditclone.repository.SubredditRepository;
import com.programming.techie.springredditclone.repository.UserRepository;
import java.time.Instant;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

  @Mock private PostRepository postRepository;
  @Mock private SubredditRepository subredditRepository;
  @Mock private UserRepository userRepository;
  @Mock private AuthService authService;
  @Mock private PostMapper postMapper;

  PostService postService;

  @Captor
  private ArgumentCaptor<Post> postArgumentCaptor;

  @BeforeEach
  public void setup() {
    postService =
        new PostService(
            postRepository, subredditRepository, userRepository, authService, postMapper);
  }

  @Test
  @DisplayName("Should Find Post By Id")
  void shouldFindPostById() {

    Post post =
        new Post(123L, "First Post", "http://url.site", "Test", 0, null, Instant.now(), null);
    Mockito.when(postRepository.findById(123L)).thenReturn(Optional.of(post));

    PostResponse expectedPostResponse =
        new PostResponse(
            123L,
            "First Post",
            "https://url.site",
            "Test",
            "Test User",
            "Test Subreddit",
            0,
            0,
            "1 Hour Ago",
            false,
            false);
    Mockito.when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(expectedPostResponse);

    PostResponse actualPostResponse = postService.getPost(123L);
    Assertions.assertThat(actualPostResponse.getId()).isEqualTo(expectedPostResponse.getId());
    Assertions.assertThat(actualPostResponse.getPostName())
        .isEqualTo(expectedPostResponse.getPostName());
  }

  @Test
  @DisplayName("Should Save Posts")
  public void shouldSavePosts() {
    User currentUser =
        new User(123L, "Test User", "secret password", "user@email.com", Instant.now(), true);
    Subreddit subreddit =
        new Subreddit(
            123L,
            "First Subreddit",
            "Subreddit Description",
            emptyList(),
            Instant.now(),
            currentUser);

    PostRequest postRequest =
        new PostRequest(null, "First Subreddit", "First Post", "http://url.site", "Test");

    Post post =
            new Post(123L, "First Post", "http://url.site", "Test", 0, null, Instant.now(), null);

    Mockito.when(subredditRepository.findByName("First Subreddit")).thenReturn(Optional.of(subreddit));
    Mockito.when(authService.getCurrentUser()).thenReturn(currentUser);
    Mockito.when(postMapper.map(postRequest, subreddit, currentUser)).thenReturn(post);

    postService.save(postRequest);
    Mockito.verify(postRepository, Mockito.times(1)).save(postArgumentCaptor.capture());

    Assertions.assertThat(postArgumentCaptor.getValue().getPostId()).isEqualTo(123L);
    Assertions.assertThat(postArgumentCaptor.getValue().getPostName()).isEqualTo("First Post");
  }
}
