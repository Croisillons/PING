import fr.epita.assistants.features.git.AddFeature;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.service.MyProjectService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public class AddFeatureTest {
    private Path path;
    private File file;
    private Git git;
    private AddFeature feature;

    @BeforeEach
    void setUp() throws GitAPIException {
        file = new File("src/test/testFiles/tmp");
        path = file.toPath();
        file.mkdirs();
        git = Git.init().setDirectory(file).call();
        feature = new AddFeature();
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
    public void addOne() throws GitAPIException {
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

        Assertions.assertTrue(feature.execute(project, "file1").isSuccess());
        Status status = git.status().call();
        Set<String> added = status.getAdded();
        Assertions.assertEquals(1, added.size());
    }

    @Test
    public void addTwo() throws GitAPIException {
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

        Assertions.assertTrue(feature.execute(project, "file1", "file2").isSuccess());
        Status status = git.status().call();
        Set<String> added = status.getAdded();
        Assertions.assertEquals(2, added.size());
    }

    @Test
    public void addFailed() throws GitAPIException {
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

        Assertions.assertFalse(feature.execute(project, "file3").isSuccess());
        Status status = git.status().call();
        Set<String> added = status.getAdded();
        Assertions.assertEquals(0, added.size());
    }

//    @Test
//    public void addRegex() throws GitAPIException {
//        File subfile1 = new File("src/test/testFiles/tmp/file1");
//        File subfile2 = new File("src/test/testFiles/tmp/file2");
//        try {
//            subfile1.createNewFile();
//            subfile2.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Assertions.fail();
//            return;
//        }
//
//        MyProjectService projectService = new MyProjectService();
//        Project project = projectService.load(file.toPath());
//
//        Assertions.assertTrue(feature.execute(project, ".").isSuccess());
//        Status status = git.status().call();
//        Set<String> added = status.getAdded();
//        Assertions.assertEquals(2, added.size());
//    }
}
