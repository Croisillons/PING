package fr.epita.assistants.features.maven;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.service.MyProjectService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TreeFeatureTest {
    private Path path;
    private File file;
    private MyProjectService projectService;

    @BeforeEach
    void setUp() {
        projectService = new MyProjectService();
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
    public void tree() throws IOException
    {
        File pom = new File("src/test/testFiles/tmp/pom.xml");
        pom.createNewFile();
        Project project = projectService.load(path);
        Optional<Feature> feature = project.getFeature(Mandatory.Features.Maven.TREE);
        Assertions.assertTrue(feature.isPresent());

        feature.get().execute(project);
    }
}