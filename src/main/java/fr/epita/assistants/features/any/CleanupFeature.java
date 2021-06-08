package fr.epita.assistants.features.any;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.Project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static fr.epita.assistants.Utils.removeNode;

public class CleanupFeature implements Feature {
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        Optional<Node> myIdeIgnore = project.getRootNode().getChildren().stream()
                .filter(node -> node.getPath().getFileName().toString().equals(".myideignore"))
                .findAny();
        if (myIdeIgnore.isEmpty())
            return () -> false;

        try {
            List<String> toIgnore = Files.readAllLines(myIdeIgnore.get().getPath());
            cleanup(project.getRootNode(), toIgnore);
        } catch (IOException e) {
            e.printStackTrace();
            return () -> false;
        }

        return () -> true;
    }

    private void cleanup(Node node, List<String> toIgnore) {
        if (node.getType() == Node.Types.FILE)
            return;
        // Use fori because we will remove children during cleanup
        for (int i = node.getChildren().size() - 1; i >= 0; i--) {
            Node child = node.getChildren().get(i);
            if (toIgnore.stream().anyMatch(reg -> child.getPath().getFileName().toString().matches(reg))) {
                // child.destroy();
                removeNode(node, child);
            } else {
                cleanup(child, toIgnore);
            }
        }
    }


    @Override
    public Type type() {
        return Mandatory.Features.Any.CLEANUP;
    }
}
