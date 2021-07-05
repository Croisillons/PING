package fr.epita.assistants.myide.domain.aspect;
import fr.epita.assistants.features.any.CleanupFeature;
import fr.epita.assistants.features.any.DistFeature;
import fr.epita.assistants.features.any.RunDiagnosticsFeature;
import fr.epita.assistants.features.any.SearchFeature;
import fr.epita.assistants.myide.domain.entity.Aspect;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;

import java.util.ArrayList;
import java.util.List;

public class AnyAspect implements Aspect {
    /**
     * @return The type of the Aspect.
     */
    @Override
    public Type getType()
    {
        return Mandatory.Aspects.ANY;
    }

    /**
     * @return The list of features associated with the Aspect.
     */
    @Override
    public List<Feature> getFeatureList()
    {
        return List.of(new CleanupFeature(), new DistFeature(), new SearchFeature(), new RunDiagnosticsFeature());
    }
}
