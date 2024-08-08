package com.github.dawidstankiewicz.forum.topic;

import com.github.dawidstankiewicz.forum.exception.ResourceNotFoundException;
import com.github.dawidstankiewicz.forum.model.dto.NewTopicForm;
import com.github.dawidstankiewicz.forum.model.entity.Post;
import com.github.dawidstankiewicz.forum.model.entity.Section;
import com.github.dawidstankiewicz.forum.model.entity.Topic;
import com.github.dawidstankiewicz.forum.model.entity.User;
import com.github.dawidstankiewicz.forum.post.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class TopicService {
    
    private final TopicRepository topicRepository;

    private final PostRepository postRepository;
    
    public TopicService(TopicRepository topicRepository, PostRepository postRepository) {
        this.topicRepository = topicRepository;
        this.postRepository = postRepository;
    }

    public List<Topic> findAll() {
        return topicRepository.findAll();
    }
    
    public Topic findOne(int id) {
        Optional<Topic> topicOptional = topicRepository.findById(id);
        if (!topicOptional.isPresent()) {
            throw new ResourceNotFoundException();
        }
        return topicOptional.get();
    }
    
    public Set<Topic> findRecent() {
        return topicRepository.findTop5ByOrderByCreationDateDesc();
    }

    public Set<Topic> findBySection(Section section) {
        return topicRepository.findBySection(section);
    }

    public Topic createNewTopic(NewTopicForm topicForm, User author, Section section) {
        Topic topic = Topic.builder()
                .section(section)
                .user(author)
                .title(topicForm.getTitle()).build();
        topic = topicRepository.save(topic);
        Post post = Post.builder()
                .topic(topic)
                .content(topicForm.getContent())
                .user(author)
                .build();
        postRepository.save(post);
        return topic;
    }

    public void delete(Topic topic) {
        topicRepository.delete(topic);
    }
    
}
