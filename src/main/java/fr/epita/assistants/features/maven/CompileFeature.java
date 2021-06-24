package fr.epita.assistants.features.maven;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;


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
        ProcessBuilder builder = new ProcessBuilder("mvn", "compile")
                .directory(project.getRootNode().getPath().toFile());
        try {
            Process process = builder.start();
            if (params.length == 1)
            {
                Consumer<InputStream> callback = (Consumer<InputStream>) params[0];
                callback.accept(process.getInputStream());
            }
        } catch (IOException e) {
            // e.printStackTrace();
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
