import fr.epita.assistants.MyNode;
import fr.epita.assistants.MyNodeService;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.service.NodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class NodeServiceTest
{
    private final Node testFolder = new MyNode(Path.of("src/test/testFiles"));
    private NodeService nodeService;

    @BeforeEach
    void setUp()
    {
        nodeService = new MyNodeService();
    }

    @Test
    public void createFile()
    {
        nodeService.create(testFolder, "test.txt", Node.Types.FILE);

        assertTrue(new File("src/test/testFiles/test.txt").exists());
        assertEquals(1, testFolder.getChildren().size());
        Node fileNode = testFolder.getChildren().get(0);
        assertEquals(Node.Types.FILE, fileNode.getType());
        new File("src/test/testFiles/test.txt").delete();
    }

    @Test
    public void createFolder()
    {
        nodeService.create(testFolder, "folder", Node.Types.FOLDER);

        assertTrue(new File("src/test/testFiles/folder").exists());
        assertTrue(new File("src/test/testFiles/folder").isDirectory());
        assertEquals(1, testFolder.getChildren().size());
        Node fileNode = testFolder.getChildren().get(0);
        assertEquals(Node.Types.FOLDER, fileNode.getType());
        new File("src/test/testFiles/folder").delete();
    }

    @Test
    public void createFolderInvalidPath()
    {
        assertThrows(Exception.class, () -> nodeService.create(testFolder, "invalid/path/folder", Node.Types.FOLDER));

        assertEquals(0, testFolder.getChildren().size());
    }
}
