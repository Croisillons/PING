package fr.epita.assistants.myide.domain.service;

import fr.epita.assistants.myide.domain.entity.MyNode;
import fr.epita.assistants.myide.domain.entity.Node;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class MyNodeService implements NodeService {
    /**
     * Update the content in the range [from, to[.
     *
     * @param node            Node to update (must be a file).
     * @param from            Beginning index of the text to update.
     * @param to              Last index of the text to update (Not included).
     * @param insertedContent Content to insert.
     * @return The node that has been updated.
     * @throws Exception upon update failure.
     */
    @SneakyThrows
    @Override
    public Node update(final Node node, final int from, final int to,
                       final byte[] insertedContent)
    {
        if (node.getType() == Node.Types.FOLDER)
            throw new IllegalArgumentException("Cannot update content of a folder");
        if (from > to)
            throw new IllegalArgumentException("to must be greater than from");
        File file = node.getPath().toFile();
        final byte[] content;
        try (FileInputStream inputStream = new FileInputStream(file))
        {
            content = inputStream.readAllBytes();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file))
        {
            outputStream.write(content);
            outputStream.write(insertedContent);
        }
        return node;
    }

    /**
     * Delete the node given as parameter.
     *
     * @param node Node to remove.
     * @return True if the node has been deleted, false otherwise.
     */
    @Override
    public boolean delete(final Node node) {
        // Delete file/folder. Need to remove from list of nodes outside.
        try {
            return deleteDir(node.getPath().toFile());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDir(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (final File file : files)
                deleteDir(file);
        }
        return dir.delete();
    }

    /**
     * Create a new node.
     *
     * @param folder Parent node of the new node.
     * @param name   Name of the new node.
     * @param type   Type of the new node.
     * @return Node that has been created.
     * @throws Exception upon creation failure.
     */
    @Override
    public Node create(final Node folder, final String name, final Node.Type type) {
        Path subPath = Path.of(folder.getPath().toString() + "/" + name);
        try {
            File newFile = new File(subPath.toString());
            if (type == Node.Types.FILE) {
                if (!newFile.createNewFile())
                    throw new RuntimeException("File already exists");
            } else {
                if (!newFile.mkdir())
                    throw new RuntimeException("Could not create folder");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not create file/folder");
        }
        return new MyNode(subPath);
    }

    /**
     * Move node from source to destination.
     *
     * @param nodeToMove        Node to move.
     * @param destinationFolder Destination of the node.
     * @return The node that has been moved.
     * @throws Exception upon move failure.
     */
    @Override
    public Node move(final Node nodeToMove, final Node destinationFolder) {
        Path srcPath = nodeToMove.getPath();
        Path dstPath = Path.of(destinationFolder.getPath().toString() + '/' + srcPath.getFileName()
                .toString());

        try {
            if (nodeToMove.isFolder())
                FileUtils.moveDirectory(srcPath.toFile(), dstPath.toFile());
            else
                Files.move(srcPath, dstPath);
        } catch (FileAlreadyExistsException e) {
            throw new RuntimeException("A file already exists with that name");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't move directory");
        }

        return new MyNode(dstPath);
    }
}
