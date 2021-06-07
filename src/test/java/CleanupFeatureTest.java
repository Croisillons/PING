import fr.epita.assistants.MyProjectService;
import fr.epita.assistants.features.any.CleanupFeature;
import fr.epita.assistants.myide.domain.entity.Project;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

class CleanupFeatureTest {
    private Path path;
    private File file;
    private CleanupFeature feature;

    @BeforeEach
    void setUp() {
        feature = new CleanupFeature();
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

    public void writeToFile(File file, String message) {
        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(message);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void noMyIdeIgnore() {

        File subfile1 = new File("src/test/testFiles/tmp/file1");
        File subfile2 = new File("src/test/testFiles/tmp/file2");
        try {
            subfile1.createNewFile();
            subfile2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
            return;
        }

        MyProjectService projectService = new MyProjectService();
        Project project = projectService.load(file.toPath());

        Assertions.assertFalse(feature.execute(project).isSuccess());
    }

    @Test
    public void noneMatched() {
        File myIdeIgnore = new File("src/test/testFiles/tmp/.myideignore");

        File subfile1 = new File("src/test/testFiles/tmp/file1");
        File subfile2 = new File("src/test/testFiles/tmp/file2");
        try {
            myIdeIgnore.createNewFile();
            writeToFile(myIdeIgnore, "toto");

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
    }


    @Test
    public void oneMatched() {
        File myIdeIgnore = new File("src/test/testFiles/tmp/.myideignore");

        File subfile1 = new File("src/test/testFiles/tmp/file1");
        File subfile2 = new File("src/test/testFiles/tmp/file2");
        try {
            myIdeIgnore.createNewFile();
            writeToFile(myIdeIgnore, "file3\nfile1");

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
        Assertions.assertEquals(2, project.getRootNode().getChildren().size());
    }

    @Test
    public void allMatched() {
        File myIdeIgnore = new File("src/test/testFiles/tmp/.myideignore");

        File subfile1 = new File("src/test/testFiles/tmp/file1");
        File subfile2 = new File("src/test/testFiles/tmp/file2");
        try {
            myIdeIgnore.createNewFile();
            writeToFile(myIdeIgnore, "file1\nfile2");

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
        Assertions.assertEquals(1, project.getRootNode().getChildren().size());
    }

    @Test
    public void oneRegexMatched() {
        File myIdeIgnore = new File("src/test/testFiles/tmp/.myideignore");

        File subfile1 = new File("src/test/testFiles/tmp/file1");
        File subfile2 = new File("src/test/testFiles/tmp/file2");
        try {
            myIdeIgnore.createNewFile();
            writeToFile(myIdeIgnore, ".*e1");

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
        Assertions.assertEquals(2, project.getRootNode().getChildren().size());
    }

    @Test
    public void cleanInFolder() {
        File myIdeIgnore = new File("src/test/testFiles/tmp/.myideignore");

        File subfolder1 = new File("src/test/testFiles/tmp/folder1");
        File subfolder2 = new File("src/test/testFiles/tmp/folder2");
        File subfile1 = new File("src/test/testFiles/tmp/folder1/file1");
        File subfile2 = new File("src/test/testFiles/tmp/folder2/file2");
        try {
            myIdeIgnore.createNewFile();
            writeToFile(myIdeIgnore, "file*");

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
        Assertions.assertEquals(3, project.getRootNode().getChildren().size());
        Assertions.assertEquals(0, project.getRootNode().getChildren().get(0).getChildren().size());
    }
}