import fr.epita.assistants.myide.domain.entity.MyNode;
import fr.epita.assistants.myide.domain.service.MyNodeService;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.service.NodeService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class NodeServiceTest {
    static final private String rootPath = "src/test/testFiles";
    private Node testFolder;
    private Node testFolder2;

    private NodeService nodeService;

    @BeforeEach
    void setUp() {
        nodeService = new MyNodeService();
        new File(rootPath).mkdir();
        testFolder = new MyNode(Path.of(rootPath));
        new File(rootPath + "/tmp").mkdir();
        testFolder2 = new MyNode(Path.of(rootPath + "/tmp"));
    }

    @AfterEach
    void tearDown() {
        testFolder2.getPath().toFile().delete();
        deleteDir(new File(rootPath));
    }

    public void deleteDir(File dir)
    {
        File[] files = dir.listFiles();
        if (files != null)
        {
            for (final File file : files)
                deleteDir(file);
        }
        dir.delete();
    }

    @Test
    public void createFile() {
        assertEquals(1, testFolder.getChildren().size());
        nodeService.create(testFolder, "test.txt", Node.Types.FILE);

        assertTrue(new File(rootPath + "/test.txt").exists());
        assertEquals(2, testFolder.getChildren().size());
        Node fileNode = testFolder.getChildren().get(0);
        assertEquals(Node.Types.FILE, fileNode.getType());
        new File(rootPath + "/test.txt").delete();
    }

    @Test
    public void createFolder() {
        assertEquals(1, testFolder.getChildren().size());
        nodeService.create(testFolder, "folder", Node.Types.FOLDER);

        assertTrue(new File(rootPath + "/folder").exists());
        assertTrue(new File(rootPath + "/folder").isDirectory());
        assertEquals(2, testFolder.getChildren().size());
        Node fileNode = testFolder.getChildren().get(0);
        assertEquals(Node.Types.FOLDER, fileNode.getType());
        new File(rootPath + "/folder").delete();
    }

    @Test
    public void createFolderInvalidPath() {
        assertThrows(Exception.class, () -> nodeService.create(testFolder, "invalid/path/folder", Node.Types.FOLDER));

        assertEquals(1, testFolder.getChildren().size());
    }

    @SneakyThrows
    @Test
    public void moveFile() {
        new File(rootPath + "/test.txt").delete();
        new File(rootPath + "/tmp/test.txt").delete();
        new File(rootPath + "/test.txt").createNewFile();
        Node srcNode = new MyNode(Path.of( rootPath + "/test.txt"));
        // il faut créer le fichier non ?

        Node res = nodeService.move(srcNode, testFolder2);
        assertTrue(new File(rootPath + "/tmp/test.txt").exists());
        assertFalse(new File(rootPath + "/test.txt").exists());
        assertEquals(1, testFolder2.getChildren().size());
        assertEquals(Node.Types.FILE, res.getType());
        new File(rootPath + "/test.txt").delete();
        new File(rootPath + "/tmp/test.txt").delete();
    }

    @SneakyThrows
    @Test
    public void moveFolder() {
        new File(rootPath + "/wsh").mkdir();
        Node srcNode = new MyNode(Path.of(rootPath + "/wsh"));

        nodeService.move(srcNode, testFolder2);
        assertTrue(new File(rootPath + "/tmp/wsh").exists());
        assertFalse(new File(rootPath + "/wsh").exists());
        assertEquals(1, testFolder2.getChildren().size());
        Node dstNode = testFolder2.getChildren().get(0);
        assertEquals(Node.Types.FOLDER, dstNode.getType());

        srcNode.getPath().toFile().delete();
        dstNode.getPath().toFile().delete();
    }

    @SneakyThrows
    @Test
    public void moveFolderWithContent() {
        new File(rootPath + "/wsh").mkdirs();
        new File(rootPath + "/wsh/alors").createNewFile();
        Node srcNode = new MyNode(Path.of(rootPath + "/wsh"));
        Node srcNodeChild = new MyNode(Path.of(rootPath + "/wsh/alors"));

        nodeService.move(srcNode, testFolder2);
        assertTrue(new File(rootPath + "/tmp/wsh").exists());
        assertTrue(new File(rootPath + "/tmp/wsh/alors").exists());
        assertFalse(new File(rootPath + "/wsh").exists());
        assertFalse(new File(rootPath + "/wsh/alors").exists());
        assertEquals(1, testFolder2.getChildren().size());
        Node dstNode = testFolder2.getChildren().get(0);

        assertEquals(Node.Types.FOLDER, dstNode.getType());
    }
}