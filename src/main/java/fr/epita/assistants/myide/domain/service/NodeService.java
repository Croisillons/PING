package fr.epita.assistants.myide.domain.service;

import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.utils.Given;

@Given(overridden = true)
public interface NodeService {

    /**
     * Update the content in the range [from, to[.
     *
     * @param node Node to update (must be a file).
     * @param from Beginning index of the text to update.
     * @param to Last index of the text to update (Not included).
     * @param insertedContent Content to insert.
     * @throws Exception upon update failure.
     * @return The node that has been updated.
     */
    Node update(final Node node,
                final int from,
                final int to,
                final byte[] insertedContent);

    /**
     * Delete the node given as parameter.
     *
     * @param node Node to remove.
     * @return True if the node has been deleted, false otherwise.
     */
    boolean delete(final Node node);

    /**
     * Create a new node.
     *
     * @param folder Parent node of the new node.
     * @param name Name of the new node.
     * @param type Type of the new node.
     * @throws Exception upon creation failure.
     * @return Node that has been created.
     */
    Node create(final Node folder,
                final String name,
                final Node.Type type);

    /**
     * Move node from source to destination.
     *
     * @param nodeToMove Node to move.
     * @param destinationFolder Destination of the node.
     * @throws Exception upon move failure.
     * @return The node that has been moved.
     */
    Node move(final Node nodeToMove,
              final Node destinationFolder);
}
