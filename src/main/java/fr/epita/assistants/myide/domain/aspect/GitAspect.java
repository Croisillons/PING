package fr.epita.assistants.myide.domain.aspect;

import fr.epita.assistants.features.git.AddFeature;
import fr.epita.assistants.features.git.CommitFeature;
import fr.epita.assistants.features.git.PullFeature;
import fr.epita.assistants.features.git.PushFeature;
import fr.epita.assistants.myide.domain.entity.Aspect;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;

import java.util.List;

public class GitAspect implements Aspect {
    /**
     * @return The type of the Aspect.
     */
    @Override
    public Type getType()
    {
        return Mandatory.Aspects.GIT;
    }

    /**
     * @return The list of features associated with the Aspect.
     */
    @Override
    public List<Feature> getFeatureList()
    {
        return List.of(new AddFeature(), new CommitFeature(), new PullFeature(), new PushFeature());
    }
}
