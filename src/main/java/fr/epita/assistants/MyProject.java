package fr.epita.assistants;

import fr.epita.assistants.myide.domain.entity.Aspect;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.Project;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MyProject implements Project {
    private final Node rootNode;
    private final Set<Aspect> aspects = new HashSet<>();

    public MyProject(final Node rootNode)
    {
        this.rootNode = rootNode;
    }

    /**
     * @return The root node of the project.
     */
    @Override
    public Node getRootNode()
    {
        return this.rootNode;
    }

    /**
     * @return The aspects of the project.
     */
    @Override
    public Set<Aspect> getAspects()
    {
        return this.aspects;
    }

    /**
     * Get an optional feature of the project depending
     * of its type. Returns an empty optional if the
     * project does not have the features queried.
     *
     * @param featureType Type of the feature to retrieve.
     *
     * @return An optional feature of the project.
     */
    @Override
    public Optional<Feature> getFeature(final Feature.Type featureType)
    {
        // FIXME
        return Optional.empty();
    }
}
