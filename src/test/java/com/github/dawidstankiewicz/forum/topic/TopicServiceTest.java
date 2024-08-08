package com.github.dawidstankiewicz.forum.topic;

import com.github.dawidstankiewicz.forum.model.dto.NewTopicForm;
import com.github.dawidstankiewicz.forum.model.entity.Post;
import com.github.dawidstankiewicz.forum.model.entity.Section;
import com.github.dawidstankiewicz.forum.model.entity.Topic;
import com.github.dawidstankiewicz.forum.model.entity.User;
import com.github.dawidstankiewicz.forum.post.PostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TopicServiceTest {

    @Mock private TopicRepository repository;
    @Mock private PostRepository postRepository;

    @Spy @InjectMocks private TopicService service;

    @Test
    public void shouldCreateNewTopic() {
        //given
        NewTopicForm topicForm = NewTopicForm.builder().title("title").build();
        User author = new User();
        Section section = new Section();
        Topic expectedTopic = Topic.builder().title(topicForm.getTitle()).user(author).section(section).build();
        doReturn(expectedTopic).when(repository).save(expectedTopic);
        Post expectedPost = Post.builder().topic(expectedTopic).user(author).build();
        //when
        Topic result = service.createNewTopic(topicForm, author, section);
        //then
        verify(repository).save(eq(expectedTopic));
        verify(postRepository).save(eq(expectedPost));
        assertEquals(topicForm.getTitle(), result.getTitle());
    }

    @Test
    public void shouldFindOneTopic() {
        //given
        Topic topic = Topic.builder().id(123).build();
        when(repository.findById(123)).thenReturn(Optional.of(topic));
        //when
        Topic result = service.findOne(123);
        //then
        assertEquals(topic, result);
        verify(repository).findById(123);
    }

    @Test
    public void shouldThrowExceptionIfTopicNotFound() {
        //given
        //when
        try {
            Topic result = service.findOne(111);
            fail("Exception expected");
        } catch (Exception e) {
            //then
            verify(repository).findById(111);
        }
    }

    @Test
    public void shouldFindRecentTopics() {
        //given
        Set<Topic> topics = Set.of(Topic.builder().id(123).build(), Topic.builder().id(456).build());
        when(repository.findTop5ByOrderByCreationDateDesc()).thenReturn(topics);
        //when
        Set<Topic> posts = service.findRecent();
        //then
        assertEquals(topics, posts);
    }

    @Test
    public void shouldFindBySection() {
        //given
        Set<Topic> topics = Set.of(Topic.builder().id(123).build(), Topic.builder().id(456).build());
        Section section = Section.builder().id(123).build();
        when(repository.findBySection(section)).thenReturn(topics);
        //when
        Set<Topic> result = service.findBySection(section);
        //then
        assertEquals(topics, result);
    }

    @Test
    public void shouldDeleteTopic() {
        //given
        Topic topic = Topic.builder().id(123).build();
        //when
        service.delete(topic);
        //then
        verify(repository).delete(topic);
    }
}
