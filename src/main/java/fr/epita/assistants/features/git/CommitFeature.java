package fr.epita.assistants.features.git;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

public class CommitFeature implements Feature {
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        Git git = null;
        try {
            git = Git.open(project.getRootNode().getPath().toFile());
        } catch (IOException e) {
            e.printStackTrace();
            return () -> false;
        }

        if (params.length < 1)
            return () -> false;

        try {
            git.commit().setMessage((String) params[0]).setAllowEmpty(false).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            return () -> false;
        }

        return () -> true;
    }

    @Override
    public Type type() {
        return Mandatory.Features.Git.COMMIT;
    }
}
