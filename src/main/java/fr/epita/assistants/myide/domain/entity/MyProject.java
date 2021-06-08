package fr.epita.assistants.myide.domain.entity;

import fr.epita.assistants.features.any.CleanupFeature;
import fr.epita.assistants.features.any.DistFeature;
import fr.epita.assistants.features.git.AddFeature;
import fr.epita.assistants.features.git.CommitFeature;
import fr.epita.assistants.features.git.PullFeature;
import fr.epita.assistants.features.git.PushFeature;
import fr.epita.assistants.myide.domain.entity.Aspect;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.Project;
import org.apache.commons.lang3.NotImplementedException;

import java.io.FileOutputStream;
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
        if (Mandatory.Features.Any.CLEANUP.equals(featureType))
            return Optional.of(new CleanupFeature());
        else if (Mandatory.Features.Any.DIST.equals(featureType))
            return Optional.of(new DistFeature());
        else if (Mandatory.Features.Any.SEARCH.equals(featureType))
            throw new NotImplementedException("blb");

        if (aspects.stream().anyMatch(aspect -> aspect.getType().equals(Mandatory.Aspects.GIT)))
        {
            if (Mandatory.Features.Git.COMMIT.equals(featureType))
                return Optional.of(new CommitFeature());
            else if (Mandatory.Features.Git.ADD.equals(featureType))
                return Optional.of(new AddFeature());
            else if (Mandatory.Features.Git.PULL.equals(featureType))
                return Optional.of(new PullFeature());
            else if (Mandatory.Features.Git.PUSH.equals(featureType))
                return Optional.of(new PushFeature());
        }

        if (aspects.stream().anyMatch(aspect -> aspect.getType().equals(Mandatory.Aspects.MAVEN)))
        {
            if (Mandatory.Features.Maven.COMPILE.equals(featureType))
                throw new NotImplementedException("blb");
            else if (Mandatory.Features.Maven.CLEAN.equals(featureType))
                throw new NotImplementedException("blb");
            else if (Mandatory.Features.Maven.TEST.equals(featureType))
                throw new NotImplementedException("blb");
            else if (Mandatory.Features.Maven.PACKAGE.equals(featureType))
                throw new NotImplementedException("blb");
            else if (Mandatory.Features.Maven.INSTALL.equals(featureType))
                throw new NotImplementedException("blb");
            else if (Mandatory.Features.Maven.EXEC.equals(featureType))
                throw new NotImplementedException("blb");
            else if (Mandatory.Features.Maven.TREE.equals(featureType))
                throw new NotImplementedException("blb");
        }

        return Optional.empty();
    }
}
