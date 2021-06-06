import fr.epita.assistants.MyNode;
import fr.epita.assistants.myide.domain.entity.Node;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class MyNodeTest {
    private Path path;
    private File file;

    @BeforeEach
    void setUp()
    {
        file = new File("src/test/testFiles/tmp");
        path = file.toPath();
        file.mkdirs();
    }

    @AfterEach
    void tearDown()
    {
        deleteDir(file);
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
    public void loadNoChildren()
    {
        MyNode node = new MyNode(path, Node.Types.FOLDER);

        node.loadChildren();
        Assertions.assertEquals(0, node.getChildren().size());
    }

    @Test
    public void loadChildren()
    {
        MyNode node = new MyNode(path, Node.Types.FOLDER);
        File subfolder1 = new File("src/test/testFiles/tmp/folder1");
        File subfile1 = new File("src/test/testFiles/tmp/folder1/file1");
        File subfile2 = new File("src/test/testFiles/tmp/file2");

        try
        {
            subfolder1.mkdir();
            subfile1.createNewFile();
            subfile2.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Assertions.fail();
            return;
        }

        node.loadChildren();
        Assertions.assertEquals(2, node.getChildren().size());
    }

    @Test
    public void load2Files()
    {
        MyNode node = new MyNode(path, Node.Types.FOLDER);
        File subfile1 = new File("src/test/testFiles/tmp/file1");
        File subfile2 = new File("src/test/testFiles/tmp/file2");
        try
        {
            subfile1.createNewFile();
            subfile2.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Assertions.fail();
            return;
        }

        node.loadChildren();
        Assertions.assertEquals(2, node.getChildren().size());
    }

    @Test
    public void load2Files1FileEach()
    {
        /**
         tmp/
            folder1/
                file1
            folder2/
                file2
         **/
        MyNode node = new MyNode(path, Node.Types.FOLDER);
        File subfolder1 = new File("src/test/testFiles/tmp/folder1");
        File subfolder2 = new File("src/test/testFiles/tmp/folder2");
        File subfile1 = new File("src/test/testFiles/tmp/folder1/file1");
        File subfile2 = new File("src/test/testFiles/tmp/folder2/file2");
        try
        {
            subfolder1.mkdirs();
            subfolder2.mkdirs();
            subfile1.createNewFile();
            subfile2.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Assertions.fail();
            return;
        }

        node.loadChildren();

        Assertions.assertEquals(2, node.getChildren().size());
        Assertions.assertEquals(Node.Types.FOLDER, node.getType());
        Assertions.assertEquals(1, node.getChildren().get(0).getChildren().size());
        Assertions.assertEquals(Node.Types.FILE, node.getChildren().get(0).getChildren().get(0).getType());
        Assertions.assertEquals(1, node.getChildren().get(1).getChildren().size());
        Assertions.assertEquals(Node.Types.FILE, node.getChildren().get(1).getChildren().get(0).getType());
    }

}
