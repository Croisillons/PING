package fr.epita.assistants.features.maven;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TreeFeature implements Feature {
    /**
     * @param project {@link Project} on which the feature is executed.
     * @param params Parameters given to the features.
     *
     * @return {@link ExecutionReport}
     */
    @Override
    public ExecutionReport execute(final Project project, final Object... params)
    {
        ProcessBuilder builder = new ProcessBuilder("mvn", "dependency:tree")
                .directory(project.getRootNode().getPath().toFile());
        try {
            Process process = builder.start();
            process.waitFor();
            var output = process.getInputStream();
            var bytes = output.readAllBytes();
            try (FileOutputStream fileOutputStream = new FileOutputStream(project.getRootNode().getPath().toString() + "/tree_output.txt"))
            {
                fileOutputStream.write(bytes);
            }

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
        return Mandatory.Features.Maven.TREE;
    }
}
