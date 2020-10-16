package de.bensoft.bukkit.buku.descgen.maven;

import de.bensoft.bukkit.buku.descgen.lib.DescGenerator;
import de.bensoft.bukkit.buku.descgen.lib.PluginSpecification;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mojo(name = "generate")
public class DescgenMavenPlugin extends AbstractMojo {

    @Parameter(required = true)
    private String name;

    @Parameter(required = true)
    private String version;

    @Parameter(required = true)
    private String main;

    @Parameter(defaultValue = "${project.basedir}/src/main/resources")
    private String outputFolder;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        setClassLoader();
        final PluginSpecification specification = new PluginSpecification(
                name,
                version,
                main,
                new HashMap<>()
        );

        final DescGenerator descGenerator = new DescGenerator(specification);
        descGenerator.generate(outputFolder);

    }

    private void setClassLoader() {
        try {
            final Set<URL> urls = new HashSet<>();
            final List<String> elements = project.getCompileClasspathElements();
            for (final String element : elements) {
                urls.add(new File(element).toURI().toURL());
            }

            ClassLoader contextClassLoader = URLClassLoader.newInstance(
                    urls.toArray(new URL[0]),
                    Thread.currentThread().getContextClassLoader());

            Thread.currentThread().setContextClassLoader(contextClassLoader);

        } catch (MalformedURLException | DependencyResolutionRequiredException e) {
            throw new RuntimeException(e);
        }
    }
}
