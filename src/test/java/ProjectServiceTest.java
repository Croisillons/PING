import fr.epita.assistants.myide.domain.service.MyProjectService;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectServiceTest {
    private Path path;
    private File file;

    @BeforeEach
    void setUp()
    {
        file = new File("src/test/testFiles/tmp");
        path = file.toPath();
        file.mkdirs();
    }

    @AfterEach
    void tearDown()
    {
        deleteDir(file);
    }

    public void deleteDir(File dir)
    {
        File[] files = dir.listFiles();
        if (files != null)
        {
            for (final File file : files)
                deleteDir(file);
        }
        dir.delete();
    }

    @SneakyThrows
    @Test
    public void any()
    {
        File subfile = new File("src/test/testFiles/tmp/blbl.txt");
        subfile.createNewFile();

        MyProjectService projectService = new MyProjectService();
        var project = projectService.load(file.toPath());
        assertEquals(1, project.getAspects().size());
        assertTrue(project.getAspects().stream().allMatch(aspect -> aspect.getType() == Mandatory.Aspects.ANY));
    }

    @SneakyThrows
    @Test
    public void maven()
    {
        File subfile = new File("src/test/testFiles/tmp/pom.xml");
        subfile.createNewFile();

        MyProjectService projectService = new MyProjectService();
        var project = projectService.load(file.toPath());
        assertEquals(2, project.getAspects().size());
        assertTrue(project.getAspects().stream().anyMatch(aspect -> aspect.getType() == Mandatory.Aspects.MAVEN));
    }

    @SneakyThrows
    @Test
    public void NotMavenWithPomInSubFolder()
    {
        new File("src/test/testFiles/tmp/truc").mkdirs();
        File subfile = new File("src/test/testFiles/tmp/truc/pom.xml");
        subfile.createNewFile();

        MyProjectService projectService = new MyProjectService();
        var project = projectService.load(file.toPath());
        assertEquals(1, project.getAspects().size());
        assertTrue(project.getAspects().stream().anyMatch(aspect -> aspect.getType() == Mandatory.Aspects.ANY));
    }

    @SneakyThrows
    @Test
    public void git()
    {
        File subfolder = new File("src/test/testFiles/tmp/.git");
        subfolder.mkdirs();

        MyProjectService projectService = new MyProjectService();
        var project = projectService.load(file.toPath());
        assertEquals(2, project.getAspects().size());
        assertTrue(project.getAspects().stream().anyMatch(aspect -> aspect.getType() == Mandatory.Aspects.GIT));
    }

    @SneakyThrows
    @Test
    public void all()
    {
        File subfolder = new File("src/test/testFiles/tmp/.git");
        subfolder.mkdirs();
        File subfile = new File("src/test/testFiles/tmp/pom.xml");
        subfile.createNewFile();

        MyProjectService projectService = new MyProjectService();
        var project = projectService.load(file.toPath());
        assertEquals(3, project.getAspects().size());
        assertTrue(project.getAspects().stream().anyMatch(aspect -> aspect.getType() == Mandatory.Aspects.GIT));
        assertTrue(project.getAspects().stream().anyMatch(aspect -> aspect.getType() == Mandatory.Aspects.MAVEN));
        assertTrue(project.getAspects().stream().anyMatch(aspect -> aspect.getType() == Mandatory.Aspects.ANY));
    }
}
