package fr.epita.assistants.features.maven;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.service.MyProjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TreeFeatureTest {
    private Path path;
    private File file;
    private MyProjectService projectService;

    @BeforeEach
    void setUp()
    {
        projectService = new MyProjectService();
    }

    @Test
    public void tree() {
        Project project = projectService.load(Path.of("."));
        Optional<Feature> feature = project.getFeature(Mandatory.Features.Maven.TREE);
        Assertions.assertTrue(feature.isPresent());

        feature.get().execute(project);
    }
}