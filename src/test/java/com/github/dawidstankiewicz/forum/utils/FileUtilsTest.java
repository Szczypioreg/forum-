package com.github.dawidstankiewicz.forum.utils;

import org.junit.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileUtilsTest {

    @Test
    public void shouldGetExtensionFromFilename() {
        //given
        String filename = "abc.pdf";
        //when
        Optional<String> result = FileUtils.getFileExtension(filename);
        //then
        assertEquals("pdf", result.get());
    }

    @Test
    public void shouldGetNoExtension() {
        //given
        String filename = "abc";
        //when
        Optional<String> result = FileUtils.getFileExtension(filename);
        //then
        assertEquals("", result.get());
    }

    @Test
    public void shouldGetOnlyExtension() {
        //given
        String filename = ".gitignore";
        //when
        Optional<String> result = FileUtils.getFileExtension(filename);
        //then
        assertEquals("gitignore", result.get());
    }
}
