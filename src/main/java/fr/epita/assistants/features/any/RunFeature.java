package fr.epita.assistants.features.any;

import fr.epita.assistants.features.maven.PackageFeature;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.Supplement;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class RunFeature implements Feature {
    @Override
    public ExecutionReport execute(final Project project, final Object... params)
    {
        var targetFolder = project.getRootNode().getChildren().stream()
                                  .filter(node -> node.isFolder() && node.getPath().getFileName().toString().equals("target"))
                                  .findFirst();
        if (targetFolder.isPresent())
        {
            var jarFile = targetFolder.get().getChildren().stream()
                                      .filter(node -> node.getPath().getFileName().toString().endsWith(".jar"))
                                      .findFirst();
            if (jarFile.isPresent())
            {
                ProcessBuilder builder = new ProcessBuilder("java", "-jar", jarFile.get().getPath().toAbsolutePath().toString())
                        .directory(project.getRootNode().getPath().toFile());
                try {
                    Process process = builder.start();
                    if (params.length == 1)
                    {
                        var callback = ((RunFeature.Callback) params[0]).callback;
                        callback.accept(new RunStreams(process.getInputStream(), process.getErrorStream()));
                    }
                    process.waitFor();
                    return () -> true;
                } catch (IOException | InterruptedException e) {
                    return () -> false;
                }
            }
        }

        return () -> false;
    }

    @Override
    public Type type()
    {
        return Supplement.Features.Any.RUN;
    }

    public static record Callback(Consumer<RunStreams> callback)
    {}

    public static record RunStreams(InputStream output, InputStream error)
    {
        public String readOutput() throws IOException
        {
            return readStream(output);
        }

        public String readError() throws IOException
        {
            return readStream(error);
        }

        private String readStream(final InputStream stream) throws IOException
        {
            int byteToRead = stream.available();
            var bytes = stream.readNBytes(byteToRead);
            if (bytes.length != 0)
                return new String(bytes);
            return "";
        }
    }
}
