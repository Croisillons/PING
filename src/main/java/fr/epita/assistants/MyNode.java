package fr.epita.assistants;

import fr.epita.assistants.myide.domain.entity.Node;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MyNode implements Node {
    private final Path path;
    private final Type type;
    private final List<Node> children;

    public MyNode(final Path path, final Type type)
    {
        this.path = path;
        this.type = type;
        this.children = new ArrayList<>();
    }

    /**
     * @return The Node path.
     */
    @Override
    public Path getPath()
    {
        return this.path;
    }

    /**
     * @return The Node type.
     */
    @Override
    public Type getType()
    {
        return this.type;
    }

    /**
     * If the Node is a Folder, returns a list of its children,
     * else returns an empty list.
     *
     * @return List of node
     */
    @Override
    public List<Node> getChildren()
    {
        return this.children;
    }

    public void loadChildren()
    {
        if (!isFolder())
            throw new IllegalStateException("Cannot load children of a file.");

        // Load all nodes
        File[] files = path.toFile().listFiles();
        if (files != null)
        {
            for (File file : files)
            {
                MyNode node = new MyNode(file.toPath(), file.isDirectory() ? Node.Types.FOLDER : Node.Types.FILE);
                this.children.add(node);
                if (node.isFolder())
                    node.loadChildren();
            }
        }
    }
}
