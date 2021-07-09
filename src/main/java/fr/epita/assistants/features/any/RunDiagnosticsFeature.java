package fr.epita.assistants.features.any;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.Supplement;

import javax.tools.*;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RunDiagnosticsFeature implements Feature {
    @Override
    public ExecutionReport execute(final Project project, final Object... params)
    {
        Callback callback = (Callback) params[0];

        List<Diagnostic<? extends JavaFileObject>> diagnostics;
        if (params.length == 2)
            diagnostics = runDiagnostics(project, (Node) params[1]);
        else
            diagnostics = runDiagnostics(project);

        callback.callback.accept(diagnostics);
        return () -> true;
    }

    @Override
    public Type type()
    {
        return Supplement.Features.Any.RUN_DIAGNOSTICS;
    }

    private List<Diagnostic<? extends JavaFileObject>> runDiagnostics(final Project project)
    {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, Locale.getDefault(), null);

        var javaFileObjects = scanRecursivelyForJavaObjects(project.getRootNode().getPath().toFile(), fileManager);

        boolean res = compiler.getTask(null, fileManager, diagnostics, List.of("-d", "/Users/gabriel/Desktop/tmp"), null, javaFileObjects)
                              .call();

        return diagnostics.getDiagnostics();
    }

    private List<Diagnostic<? extends JavaFileObject>> runDiagnostics(final Project project, final Node node)
    {
        return runDiagnostics(project).stream()
                                      .filter(diagnostic -> {
                                          JavaFileObject fileObject = diagnostic.getSource();
                                          return fileObject.toUri().equals(node.getPath().toUri());
                                      })
                                      .collect(Collectors.toList());
    }

    private List<JavaFileObject> scanRecursivelyForJavaObjects(File dir, StandardJavaFileManager fileManager)
    {
        List<JavaFileObject> javaObjects = new LinkedList<>();
        File[] files = dir.listFiles();
        for (File file : files)
        {
            if (file.isDirectory())
            {
                javaObjects.addAll(scanRecursivelyForJavaObjects(file, fileManager));
            }
            else if (file.isFile() && file.getName().toLowerCase().endsWith(".java"))
            {
                javaObjects.add(readJavaObject(file, fileManager));
            }
        }
        return javaObjects;
    }


    private JavaFileObject readJavaObject(File file, StandardJavaFileManager fileManager)
    {
        Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjects(file);
        Iterator<? extends JavaFileObject> it = javaFileObjects.iterator();
        if (it.hasNext())
        {
            return it.next();
        }
        throw new RuntimeException("Could not load " + file.getAbsolutePath() + " java file object");
    }

    public static record Callback(Consumer<List<Diagnostic<? extends JavaFileObject>>> callback)
    {}
}
