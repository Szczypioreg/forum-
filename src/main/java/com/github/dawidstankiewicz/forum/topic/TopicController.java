package com.github.dawidstankiewicz.forum.topic;

import com.github.dawidstankiewicz.forum.model.dto.NewPostForm;
import com.github.dawidstankiewicz.forum.model.dto.NewTopicForm;
import com.github.dawidstankiewicz.forum.model.entity.Post;
import com.github.dawidstankiewicz.forum.model.entity.Section;
import com.github.dawidstankiewicz.forum.model.entity.Topic;
import com.github.dawidstankiewicz.forum.model.entity.User;
import com.github.dawidstankiewicz.forum.post.PostService;
import com.github.dawidstankiewicz.forum.section.SectionService;
import com.github.dawidstankiewicz.forum.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@Slf4j
public class TopicController {

    private final PostService postService;
    private final TopicService topicService;
    private final SectionService sectionService;
    private final UserService userService;

    public TopicController(PostService postService, TopicService topicService, SectionService sectionService, UserService userService) {
        this.postService = postService;
        this.topicService = topicService;
        this.sectionService = sectionService;
        this.userService = userService;
    }

    @RequestMapping(value = "/topics/{idTopic}", method = RequestMethod.GET)
    public String getTopicById(@PathTopic Topic topic, Model model) {
        model.addAttribute("topic", topic);
        List<Post> posts = postService.findByTopic(topic);
        model.addAttribute("topicPost", posts.get(0));
        model.addAttribute("posts", posts.stream().skip(1).collect(Collectors.toList()));
        model.addAttribute("newPost", new NewPostForm());
        return "topics/topic";
    }

    @RequestMapping(value = "/topics/{idTopic}", method = RequestMethod.POST)
    public String addPost(
            @Valid
            @ModelAttribute("newPost") NewPostForm newPost,
            BindingResult result,
            Authentication authentication,
            @PathTopic Topic topic,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("topic", topic);
            model.addAttribute("posts", postService.findByTopic(topic));
            return "topics/topic";
        }

        Post post = new Post();
        post.setContent(newPost.getContent());
        post.setTopic(topic);
        post.setUser(userService.findByEmail(authentication.getName()));
        postService.save(post);

        model.asMap().clear();
        return "redirect:/topics/" + topic.getId();
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = {"/topics/new"})
    public String getNewTopicForm(@Param("sectionId") Integer sectionId, Model model) {
        if (sectionId != null) {
            model.addAttribute("selectedSection", sectionService.findOneOrExit(sectionId));
        }
        model.addAttribute("newTopic", NewTopicForm.builder().sectionId(sectionId).build());
        model.addAttribute("sections", sectionService.findAll());
        return "topics/new_topic_form";
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(value = "/topics/new")
    public String processAndAddNewTopic(@Valid @ModelAttribute("newTopic") NewTopicForm newTopic,
                                        BindingResult result,
                                        Authentication authentication,
                                        Model model) {
        log.info("Create new topic requested by user: " + authentication.getName());
        if (result.hasErrors()) {
            return getNewTopicForm(newTopic.getSectionId(), model);
        }
        User user = userService.findByEmailOrExit(authentication.getName());
        Section section = sectionService.findOneOrExit(newTopic.getSectionId());
        Topic topic = topicService.createNewTopic(newTopic, user, section);
        return "redirect:/topics/" + topic.getId();
    }

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "delete/{idTopic}", method = RequestMethod.GET)
    public String delete(@PathTopic Topic topic,
                         Authentication authentication,
                         RedirectAttributes model) {
        if (!isTopicOwner(topic, authentication)) {
            return "redirect:/topics/" + topic.getId();
        }
        topicService.delete(topic);

        model.addFlashAttribute("message", "topic.successfully.deleted");
        return "redirect:/section/" + topic.getSection().getId();
    }

    private static boolean isTopicOwner(Topic topic, Authentication authentication) {
        return authentication.getName().equals(topic.getUser().getEmail());
    }

}
