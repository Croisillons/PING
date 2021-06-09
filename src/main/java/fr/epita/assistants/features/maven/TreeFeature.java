package fr.epita.assistants.features.maven;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        if (params.length < 1)
            return () -> false;
        ProcessBuilder builder = new ProcessBuilder("mvn", "dependency:tree", params[0].toString())
                .directory(project.getRootNode().getPath().toFile());
        try {
            Process process = builder.start();
            process.waitFor();
            var output = process.getInputStream();
            var bytes = output.readAllBytes();

            String outputStr = new String(bytes);
            String[] lines = outputStr.split("\n");
            List<String> res = Arrays.stream(lines).filter(line -> !line.contains("Download")).toList();

            try (FileWriter writer = new FileWriter(project.getRootNode().getPath().toString() + "/tree_output.txt")) {
                for (String line : res)
                    writer.write(line);
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
