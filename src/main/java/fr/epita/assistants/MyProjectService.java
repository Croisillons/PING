package fr.epita.assistants;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.service.NodeService;
import fr.epita.assistants.myide.domain.service.ProjectService;

import java.nio.file.Path;

public class MyProjectService implements ProjectService {
    /**
     * Load a {@link Project} from a path.
     *
     * @param root Path of the root of the project to load.
     *
     * @return New project.
     */
    @Override
    public Project load(final Path root)
    {
        MyNode rootNode = new MyNode(root);
        rootNode.loadChildren();

        MyProject project = new MyProject(rootNode);

        // Get the type of the project
        project.getAspects().add(new AnyAspect());
        if (rootNode.getChildren().stream().anyMatch(node -> node.getPath().getFileName().toString().equals("pom.xml")))
            project.getAspects().add(new MavenAspect());
        if (rootNode.getChildren().stream().anyMatch(node -> node.getPath().getFileName().toString().equals(".git")))
            project.getAspects().add(new GitAspect());

        return project;
    }


    /**
     * Execute the given feature on the given project.
     *
     * @param project Project for which the features is executed.
     * @param featureType Type of the feature to execute.
     * @param params Parameters given to the features.
     *
     * @return Execution report of the feature.
     */
    @Override
    public Feature.ExecutionReport execute(final Project project, final Feature.Type featureType, final Object... params)
    {
        return null;
    }

    /**
     * @return The {@link NodeService} associated with your {@link MyProjectService}
     */
    @Override
    public NodeService getNodeService()
    {
        return null;
    }
}
