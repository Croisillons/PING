import fr.epita.assistants.features.git.PushFeature;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.service.MyProjectService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class PushFeatureTest {
    private Path path;
    private File file;
    private Git git;
    private PushFeature feature;

    @BeforeEach
    void setUp() throws GitAPIException {
        file = new File("src/test/testFiles/tmp");
        path = file.toPath();
        file.mkdirs();
        git = Git.init().setDirectory(file).call();
        feature = new PushFeature();
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

    public void pushOne() throws GitAPIException {
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

        git.add().addFilepattern("file1").call();
        git.commit().setMessage("message").call();
        Assertions.assertTrue(feature.execute(project).isSuccess());
    }

    @Test
    public void pushNoCommit() throws GitAPIException {
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

        git.add().addFilepattern("file1").call();
        Assertions.assertFalse(feature.execute(project).isSuccess());
    }
}
