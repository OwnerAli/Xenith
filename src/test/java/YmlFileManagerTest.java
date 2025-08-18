import me.ogali.xenithlibrary.files.YmlFileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

public class YmlFileManagerTest {
    private YmlFileManager ymlFileManager;
    private final String conditionsFile = "conditions";

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        // Use the temporary directory for testing
        File tempDirFile = tempDir.toFile();
        ymlFileManager = new YmlFileManager(tempDirFile);
    }
}