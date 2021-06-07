import fr.epita.assistants.MyNode;
import fr.epita.assistants.MyNodeService;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.service.NodeService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
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
        int childrenCount = testFolder.getChildren().size();
        nodeService.create(testFolder, "test.txt", Node.Types.FILE);

        assertTrue(new File("src/test/testFiles/test.txt").exists());
        assertEquals(childrenCount + 1, testFolder.getChildren().size());
        new File("src/test/testFiles/test.txt").delete();
    }
}
