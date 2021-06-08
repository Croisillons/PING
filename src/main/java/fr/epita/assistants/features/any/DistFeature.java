package fr.epita.assistants.features.any;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DistFeature implements Feature {
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        // Remove all trash files
        CleanupFeature cleanupFeature = new CleanupFeature();
        cleanupFeature.execute(project, params);

        // Create a zip file
        Path rootPath = project.getRootNode().getPath();
        String zipName = rootPath.getFileName().toString() + ".zip";

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipName))) {
            Files.walkFileTree(rootPath.getParent(), new SimpleFileVisitor<Path>() {
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    zos.putNextEntry(new ZipEntry(rootPath.relativize(file).toString()));
                    Files.copy(file, zos);
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            return () -> false;
        }

        return () -> true;
    }

    @Override
    public Type type() {
        return Mandatory.Features.Any.DIST;
    }
}
