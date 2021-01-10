package ru.akirakozov.sd.refactoring.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class CommonDatabaseTest {
    protected static final String DB_FILE = "DatabaseTest.db";

    @BeforeAll
    static void cleanup() {
        File dbFile = new File(DB_FILE);
        if (dbFile.exists() ) {
            assertTrue(dbFile.delete());
        }
    }

    @AfterEach
    void cleanupAfterTest() {
        cleanup();
    }
}
