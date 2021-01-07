package ru.akirakozov.sd.refactoring.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ResourceReaderTest {
    @Test
    void shouldRead() throws IOException  {
        String result = ResourceReader.read("test.txt");
        assertEquals("Test content.", result);
    }

    @Test
    void shouldReturnNull() throws IOException {
        String result = ResourceReader.read("404.txt");
        assertNull(result);
    }
}
