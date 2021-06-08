package fr.epita.assistants.features.git;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.nio.file.Path;

public class AddFeature implements Feature {
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        Git git = null;
        try {
            git = Git.open(project.getRootNode().getPath().toFile());
        } catch (IOException e) {
            e.printStackTrace();
            return () -> false;
        }

        String rootPath = project.getRootNode().getPath().toString();
        for (Object param : params) {
            try {
                Path path = Path.of(rootPath + '/' + param);
                if (!path.toFile().exists())
                    return () -> false;
                git.add().addFilepattern((String) param).call();
            } catch (GitAPIException e) {
                e.printStackTrace();
                return () -> false;
            }
        }

        return () -> true;
    }

    @Override
    public Type type() {
        return Mandatory.Features.Git.ADD;
    }
}
