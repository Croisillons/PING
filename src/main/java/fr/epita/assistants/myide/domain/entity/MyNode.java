package fr.epita.assistants.myide.domain.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MyNode implements Node {
    private final Path path;
    private final Type type;

    public MyNode(final Path path)
    {
        if (!path.toFile().exists())
            throw new IllegalArgumentException("This file does not exist");
        this.path = path;
        if (path.toFile().isFile())
            this.type = Types.FILE;
        else if (path.toFile().isDirectory())
            this.type = Types.FOLDER;
        else
            throw new IllegalArgumentException("Unrecognized file type");
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
        List<Node> children = new ArrayList<>();
        if (!isFolder())
            return children;

        // Load all nodes
        File[] files = path.toFile().listFiles();
        if (files != null)
        {
            for (File file : files)
            {
                MyNode node = new MyNode(file.toPath());
                children.add(node);
                if (node.isFolder())
                    node.getChildren();
            }
        }
        return children;
    }

    @Override
    public String getContent() {
        final byte[] content;
        try (FileInputStream inputStream = new FileInputStream(this.path.toFile()))
        {
            content = inputStream.readAllBytes();
            return new String(content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
