package com.github.dawidstankiewicz.forum.section;


import com.github.dawidstankiewicz.forum.config.Templates;
import com.github.dawidstankiewicz.forum.model.ForumModelMapper;
import com.github.dawidstankiewicz.forum.model.dto.SectionDto;
import com.github.dawidstankiewicz.forum.model.dto.SectionForm;
import com.github.dawidstankiewicz.forum.model.entity.Section;
import com.github.dawidstankiewicz.forum.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/a/sections")
@Slf4j
public class SectionAdminController {

    private final SectionService sectionService;
    private final UserService userService;
    private final ForumModelMapper modelMapper;

    public SectionAdminController(SectionService sectionService, UserService userService, ForumModelMapper modelMapper) {
        this.sectionService = sectionService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String getSectionsPage(Model model, Pageable pageable) {
        Page<Section> sections = sectionService.findSections(pageable);
        model.addAttribute("sections", modelMapper.mapPage(sections, SectionDto.class));
        return Templates.ADMIN_SECTIONS_PANEL;
    }

    @GetMapping(value = "/new")
    public String getNewSectionForm(Model model) {
        model.addAttribute("newSection", new SectionForm());
        model.addAttribute("isNew", true);
        return "sections/section_form";
    }

    @PostMapping
    public String processAndAddNewSection(
            @Valid @ModelAttribute("newSection") SectionForm newSection,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.mergeAttributes(result.getModel());
            return "sections/section_form";
        }

        Section section = new Section();
        section.setName(newSection.getName());
        section.setDescription(newSection.getDescription());
        section = sectionService.save(section);
        return "redirect:/sections/" + section.getId();
    }

    @GetMapping(value = "/{id}/details")
    public String getSectionForm(Model model, @PathVariable int id) {
        Section section = sectionService.findOneOrExit(id);
        model.addAttribute("newSection",
                SectionForm.builder()
                        .id(section.getId())
                        .name(section.getName())
                        .description(section.getDescription())
                        .build());
        model.addAttribute("isNew", false);
        return "sections/section_form";
    }

    @PostMapping("/{id}")
    public String updateSection(@Valid @ModelAttribute("newSection") SectionForm newSection,
                                BindingResult result,
                                Model model) {
        log.info("Updating section: {}", newSection.getId());
        if (result.hasErrors()) {
            log.info("Section form has errors (id: {})", newSection.getId());
            model.mergeAttributes(result.getModel());
            return "sections/section_form";
        }
        Section section = sectionService.findOneOrExit(newSection.getId());
        section.setName(newSection.getName());
        section.setDescription(newSection.getDescription());
        section = sectionService.save(section);
        log.info("Updated section: {}", newSection.getId());
        return "redirect:/sections/" + section.getId();
    }

    @GetMapping(value = "/{id}/delete")
    public String getDeleteSectionConfirmationForm(Model model, @PathVariable int id) {
        Section section = sectionService.findOneOrExit(id);
        model.addAttribute("type", "section");
        model.addAttribute("name", section.getName());
        model.addAttribute("deleteUrl", "/a/sections/" + section.getId() + "/delete");
        return "confirm_delete_form";
    }

    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable int id, RedirectAttributes model) {
        sectionService.delete(id);
        model.addFlashAttribute("message", "section.successfully.deleted");
        return "redirect:/home";
    }
}
