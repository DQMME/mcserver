package de.dqmme.subserver;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class PluginLoader implements io.papermc.paper.plugin.loader.PluginLoader {
    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addDependency(new Dependency(new DefaultArtifact("net.axay:kspigot:1.20.3"), null));

        resolver.addRepository(new RemoteRepository.Builder("maven central", "default", "https://repo.maven.apache.org/maven2/").build());

        classpathBuilder.addLibrary(resolver);
    }
}
