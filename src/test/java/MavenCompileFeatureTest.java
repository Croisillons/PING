import fr.epita.assistants.MyIde;
import fr.epita.assistants.features.git.CommitFeature;
import fr.epita.assistants.features.maven.CompileFeature;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.MyProject;
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
import java.util.Optional;
import java.util.Set;

public class MavenCompileFeatureTest {
    private Path path;
    private File file;
    private MyProjectService projectService;

    @BeforeEach
    void setUp()
    {
        projectService = new MyProjectService();
    }

    @AfterEach
    void tearDown() {
//        deleteDir(file);
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
    public void compile() {
        Project project = projectService.load(Path.of("."));
        Optional<Feature> feature = project.getFeature(Mandatory.Features.Maven.COMPILE);
        Assertions.assertTrue(feature.isPresent());

        feature.get().execute(project);
//        Assertions.assertTrue(new File("ping-ref-1.0.0-SNAPSHOT.jar").exists());
    }
}