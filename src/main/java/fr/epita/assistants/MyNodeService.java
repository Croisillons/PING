package fr.epita.assistants;

import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.service.NodeService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class MyNodeService implements NodeService {
    /**
     * Update the content in the range [from, to[.
     *
     * @param node Node to update (must be a file).
     * @param from Beginning index of the text to update.
     * @param to Last index of the text to update (Not included).
     * @param insertedContent Content to insert.
     *
     * @return The node that has been updated.
     *
     * @throws Exception upon update failure.
     */
    @Override
    public Node update(final Node node, final int from, final int to, final byte[] insertedContent)
    {
        return null;
    }

    /**
     * Delete the node given as parameter.
     *
     * @param node Node to remove.
     *
     * @return True if the node has been deleted, false otherwise.
     */
    @Override
    public boolean delete(final Node node)
    {
        return false;
    }

    /**
     * Create a new node.
     *
     * @param folder Parent node of the new node.
     * @param name Name of the new node.
     * @param type Type of the new node.
     *
     * @return Node that has been created.
     *
     * @throws Exception upon creation failure.
     */
    @Override
    public Node create(final Node folder, final String name, final Node.Type type)
    {
        Path subPath = Path.of(folder.getPath().toString() + "/" + name);
        try
        {
            File newFile = new File(subPath.toString());
            if (!newFile.createNewFile())
                throw new RuntimeException("File already exists");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Could not create file");
        }
        Node node = new MyNode(subPath);
        folder.getChildren().add(node);
        return node;
    }

    /**
     * Move node from source to destination.
     *
     * @param nodeToMove Node to move.
     * @param destinationFolder Destination of the node.
     *
     * @return The node that has been moved.
     *
     * @throws Exception upon move failure.
     */
    @Override
    public Node move(final Node nodeToMove, final Node destinationFolder)
    {
        return null;
    }
}
