package com.programming.techie.springredditclone.service;

import static org.junit.jupiter.api.Assertions.*;

import com.programming.techie.springredditclone.exceptions.SpringRedditException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentServiceTest {

  @Test
  @DisplayName("Test should Pass when Comment do not Contains Swear Words")
  void shouldNotContainSwearWordsInsideComment() {
    CommentService commentService = new CommentService(null, null, null, null, null, null, null);
    Assertions.assertFalse(commentService.containsSwearWords("This is a Clean Comment"));

//    assertThat(commentService.containsSwearWords("This is a Clean Comment"));
  }

  @Test
  @DisplayName("Test should Throw Exception when Comment Contains Swear Words")
  void shouldFailWhenCommentContainsSwearWords() {
    CommentService commentService = new CommentService(null, null, null, null, null, null, null);
    SpringRedditException exception = Assertions.assertThrows(SpringRedditException.class, () -> {
      commentService.containsSwearWords("This is shitty comment");
    });
    Assertions.assertTrue(exception.getMessage().contains("Comments contains unacceptable language"));
  }
}
