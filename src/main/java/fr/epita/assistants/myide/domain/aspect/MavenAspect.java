package fr.epita.assistants.myide.domain.aspect;

import fr.epita.assistants.features.maven.*;
import fr.epita.assistants.myide.domain.entity.Aspect;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;

import java.util.List;

public class MavenAspect implements Aspect {
    /**
     * @return The type of the Aspect.
     */
    @Override
    public Type getType()
    {
        return Mandatory.Aspects.MAVEN;
    }

    /**
     * @return The list of features associated with the Aspect.
     */
    @Override
    public List<Feature> getFeatureList()
    {
        return List.of(new TreeFeature(), new TestFeature(), new PackageFeature(), new CompileFeature(), new CleanFeature(), new ExecFeature(),
                       new InstallFeature());
    }
}
