package fr.epita.assistants.myide.domain.aspect;

import fr.epita.assistants.myide.domain.entity.Aspect;
import fr.epita.assistants.myide.domain.entity.Mandatory;

public class GitAspect implements Aspect {
    /**
     * @return The type of the Aspect.
     */
    @Override
    public Type getType()
    {
        return Mandatory.Aspects.GIT;
    }
}
