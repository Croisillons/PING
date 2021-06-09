package fr.epita.assistants.features.maven;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;


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
            process.waitFor();
            int res = process.exitValue();
            var output = process.getInputStream();
            var bytes = output.readAllBytes();
            // System.out.println("Exit value: " + res);
            // System.out.println(new String(bytes));

        } catch (IOException | InterruptedException e) {
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
