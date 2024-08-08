package com.github.dawidstankiewicz.forum.section;

import com.github.dawidstankiewicz.forum.model.entity.Section;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SectionServiceTest {

    @Mock private SectionRepository sectionRepository;
    @Spy @InjectMocks private SectionService service;

    @Test
    public void shouldFindSections() {
        //given
        Pageable pageable = Pageable.unpaged();
        Page<Section> page = new PageImpl<>(Arrays.asList(
                Section.builder().id(1).build(),
                Section.builder().id(2).build()
        ));
        doReturn(page).when(sectionRepository).findAll(pageable);
        //when
        Page<Section> result = service.findSections(pageable);
        //then
        assertEquals(page, result);
        verify(sectionRepository).findAll(pageable);
    }

    @Test
    public void shouldFindAllSections() {
        //given
        List<Section> sections = Arrays.asList(Section.builder().id(1).build());
        doReturn(sections).when(sectionRepository).findAll();
        //when
        List<Section> result = service.findAll();
        //then
        assertEquals(sections, result);
    }

    @Test
    public void shouldFindSection() {
        //given
        Section section = Section.builder().id(1).build();
        doReturn(Optional.of(section)).when(sectionRepository).findById(1);
        //when
        Section result = service.findOneOrExit(1);
        //then
        assertEquals(section, result);
    }

    @Test
    public void shouldThrowExceptionIfSectionNotFound() {
        //given
        try {
            //when
            Section result = service.findOneOrExit(2);
            fail("Exception expected");
        } catch (Exception e) {
            //then
            verify(sectionRepository).findById(2);
        }
    }

    @Test
    public void shouldSaveSection() {
        //given
        Section section = Section.builder().id(1).build();
        doReturn(section).when(sectionRepository).save(section);
        //when
        Section result = service.save(section);
        //then
        assertEquals(section, result);
    }

    @Test
    public void shouldDeleteSection() {
        //given
        Section section = Section.builder().id(1).build();
        //when
        service.delete(section);
        //then
        verify(sectionRepository).delete(section);
    }

    @Test
    public void shouldDeleteSectionById() {
        //given
        Section section = Section.builder().id(1).build();
        doReturn(Optional.of(section)).when(sectionRepository).findById(1);
        //when
        service.delete(section.getId());
        //then
        verify(sectionRepository).delete(section);
    }
}
