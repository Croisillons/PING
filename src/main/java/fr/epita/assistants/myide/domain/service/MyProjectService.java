package fr.epita.assistants.myide.domain.service;

import fr.epita.assistants.myide.domain.aspect.GitAspect;
import fr.epita.assistants.myide.domain.entity.*;
import fr.epita.assistants.myide.domain.aspect.AnyAspect;
import fr.epita.assistants.myide.domain.aspect.MavenAspect;

import java.util.List;
import java.util.Optional;

import java.nio.file.Path;

public class MyProjectService implements ProjectService {
    NodeService nodeService = new MyNodeService();

    /**
     * Load a {@link Project} from a path.
     *
     * @param root Path of the root of the project to load.
     * @return New project.
     */
    @Override
    public Project load(final Path root) {
        MyNode rootNode = new MyNode(root);

        MyProject project = new MyProject(rootNode);

        // Get the type of the project
        List<Node> children = rootNode.getChildren();
        project.getAspects().add(new AnyAspect());
        if (children.stream().anyMatch(node -> node.getPath().getFileName().toString().equals("pom.xml")))
            project.getAspects().add(new MavenAspect());
        if (children.stream().anyMatch(node -> node.getPath().getFileName().toString().equals(".git")))
            project.getAspects().add(new GitAspect());

        return project;
    }

    /**
     * Execute the given feature on the given project.
     *
     * @param project     Project for which the features is executed.
     * @param featureType Type of the feature to execute.
     * @param params      Parameters given to the features.
     * @return Execution report of the feature.
     */
    @Override
    public Feature.ExecutionReport execute(final Project project, final Feature.Type featureType,
                                           final Object... params) {
        Optional<Feature> feature = project.getFeature(featureType);
        if (feature.isEmpty())
            return () -> false;
        return feature.get().execute(project, params);
    }

    /**
     * @return The {@link NodeService} associated with your {@link MyProjectService}
     */
    @Override
    public NodeService getNodeService() {
        return this.nodeService;
    }
}
