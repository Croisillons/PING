import fr.epita.assistants.features.any.DistFeature;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.service.MyProjectService;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class DistFeatureTest {
    private Path path;
    private File file;
    private DistFeature feature;

    @BeforeEach
    void setUp() {
        feature = new DistFeature();
        file = new File("src/test/testFiles/tmp");
        path = file.toPath();
        file.mkdirs();
    }

    @AfterEach
    void tearDown() {
        deleteDir(file);
    }

    public void deleteDir(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (final File file : files)
                deleteDir(file);
        }
        dir.delete();
    }

    @Test
    public void zipFolder() {
        File myIdeIgnore = new File("src/test/testFiles/tmp/.myideignore");

        File subfolder1 = new File("src/test/testFiles/tmp/folder1");
        File subfolder2 = new File("src/test/testFiles/tmp/folder2");
        File subfile1 = new File("src/test/testFiles/tmp/folder1/file1");
        File subfile2 = new File("src/test/testFiles/tmp/folder2/file2");
        try {
            myIdeIgnore.createNewFile();

            subfolder1.mkdir();
            subfolder2.mkdir();
            subfile1.createNewFile();
            subfile2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
            return;
        }

        MyProjectService projectService = new MyProjectService();
        Project project = projectService.load(file.toPath());

        Assertions.assertTrue(feature.execute(project).isSuccess());
        File tempFile = new File("src/test/testFiles/tmp.zip");
        Assertions.assertTrue(tempFile.exists());
        tempFile.delete();
    }
}
