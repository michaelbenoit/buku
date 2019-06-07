package de.bensoft.bukkit.buku.descgen.maven;

import de.bensoft.bukkit.buku.descgen.lib.DescGenerator;
import de.bensoft.bukkit.buku.descgen.lib.PluginSpecification;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.HashMap;

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

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final PluginSpecification specification = new PluginSpecification(
                name,
                version,
                main,
                new HashMap<>()
        );

        final DescGenerator descGenerator = new DescGenerator(specification);
        descGenerator.generate(outputFolder);

    }
}
