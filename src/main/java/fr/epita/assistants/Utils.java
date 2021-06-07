package fr.epita.assistants;

import fr.epita.assistants.myide.domain.entity.Node;

import java.io.File;

public class Utils {
    public static Boolean removeNode(Node parent, Node node) {
        parent.getChildren().remove(node);
        return deleteDir(node.getPath().toFile());
    }

    public static Boolean deleteDir(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (final File file : files)
                deleteDir(file);
        }
        return dir.delete();
    }
}
