package com.github.dawidstankiewicz.forum.section;

import com.github.dawidstankiewicz.forum.config.Templates;
import com.github.dawidstankiewicz.forum.file.FileService;
import com.github.dawidstankiewicz.forum.model.ForumModelMapper;
import com.github.dawidstankiewicz.forum.model.dto.SectionForm;
import com.github.dawidstankiewicz.forum.model.dto.SectionDto;
import com.github.dawidstankiewicz.forum.model.entity.Section;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SectionAdminControllerTest {

    @Mock
    private SectionService sectionService;
    @Mock
    private FileService fileService;
    @Mock
    private ForumModelMapper mapper;
    @Mock
    private Model model;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private MultipartFile multipartFile;
    @Spy
    @InjectMocks
    private SectionAdminController controller;

    @Before
    public void setUp() {
        doReturn(Mockito.mock(File.class)).when(fileService).saveFile(any(), any());
    }

    @Test
    public void shouldReturnSectionsPage() {
        //given
        Pageable pageable = Pageable.unpaged();
        Page<Section> page = new PageImpl<>(Arrays.asList(
                Section.builder().id(1).build(),
                Section.builder().id(2).build()
        ));
        Page<SectionDto> dtos = new PageImpl<>(Arrays.asList(
                SectionDto.builder().id(1).build(),
                SectionDto.builder().id(2).build()
        ));
        doReturn(page).when(sectionService).findSections(pageable);
        doReturn(dtos).when(mapper).mapPage(page, SectionDto.class);
        //when
        String result = controller.getSectionsPage(model, pageable);
        //then
        assertEquals(Templates.ADMIN_SECTIONS_PANEL, result);
        verify(model).addAttribute("sections", dtos);
        verify(sectionService).findSections(pageable);
        verify(mapper).mapPage(page, SectionDto.class);
    }

    @Test
    public void shouldAddNewSection() {
        //given
        SectionForm form = SectionForm.builder()
                .name("name")
                .description("desc")
                .build();
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(Section.builder().id(1).build()).when(sectionService).save(any());
        //when
        String resultView = controller.processAndAddNewSection(form, bindingResult, multipartFile, model);
        //then
        verify(sectionService).save(any(Section.class));
        verify(bindingResult).hasErrors();
        verify(fileService).saveFile(any(), any());
        assertEquals("redirect:/sections/1", resultView);
    }

    @Test
    public void shouldNotAddNewSection_WhenFormIsInvalid() {
        //given
        SectionForm form = SectionForm.builder()
                .name("name")
                .description("desc")
                .build();
        doReturn(true).when(bindingResult).hasErrors();
        //when
        String resultView = controller.processAndAddNewSection(form, bindingResult, multipartFile, model);
        //then
        verifyNoInteractions(sectionService);
        verify(bindingResult).hasErrors();
        assertEquals("sections/section_form", resultView);
    }

    @Test
    public void shouldUpdateSection() {
        //given
        Section section = Section.builder().id(123).build();
        SectionForm form = SectionForm.builder()
                .id(123)
                .name("name")
                .description("desc")
                .build();
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(Section.builder().id(1).build()).when(sectionService).save(any());
        doReturn(section).when(sectionService).findOneOrExit(123);
        //when
        String resultView = controller.updateSection(form, bindingResult, multipartFile, model);
        //then
        verify(sectionService).findOneOrExit(123);
        verify(sectionService).save(any(Section.class));
        verify(bindingResult).hasErrors();
        verify(fileService).saveFile(any(), any());
        assertEquals("redirect:/sections/1", resultView);
    }

    @Test
    public void shouldNotUpdateImageWhenFileIsMissing() {
        //given
        Section section = Section.builder().id(123).build();
        SectionForm form = SectionForm.builder()
                .id(123)
                .name("name")
                .description("desc")
                .build();
        doReturn(false).when(bindingResult).hasErrors();
        doReturn(true).when(multipartFile).isEmpty();
        doReturn(Section.builder().id(1).build()).when(sectionService).save(any());
        doReturn(section).when(sectionService).findOneOrExit(123);
        //when
        String resultView = controller.updateSection(form, bindingResult, multipartFile, model);
        //then
        verify(sectionService).findOneOrExit(123);
        verify(sectionService).save(any(Section.class));
        verify(bindingResult).hasErrors();
        verifyNoInteractions(fileService);
        assertEquals("redirect:/sections/1", resultView);
    }
}
