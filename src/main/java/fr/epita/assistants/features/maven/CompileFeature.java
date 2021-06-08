package fr.epita.assistants.features.maven;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;

import java.io.IOException;
import java.nio.file.Path;


public class CompileFeature implements Feature {
    /**
     * @param project {@link Project} on which the feature is executed.
     * @param params Parameters given to the features.
     *
     * @return {@link ExecutionReport}
     */
    @Override
    public ExecutionReport execute(final Project project, final Object... params)
    {
        Path path = project.getRootNode().getPath();
        try
        {
            Process proc = Runtime.getRuntime().exec("cd " + path + " && mvn compile");
            var ret = proc.waitFor();

            if (ret != 0)
                return () -> false;
        }
        catch (IOException e)
        {
            return () -> false;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            return () -> false;
        }
        return () -> true;
    }

    /**
     * @return The type of the Feature.
     */
    @Override
    public Type type()
    {
        return Mandatory.Features.Maven.COMPILE;
    }
}
