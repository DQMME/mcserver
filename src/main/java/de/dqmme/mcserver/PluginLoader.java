package de.dqmme.mcserver;

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
        resolver.addDependency(new Dependency(new DefaultArtifact("org.litote.kmongo:kmongo-coroutine-serialization:4.11.0"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("com.fasterxml.jackson.core:jackson-core:2.17.0"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("com.mattmalec:Pterodactyl4J:2.BETA_141"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("de.rapha149.signgui:signgui:2.3.2"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("com.google.code.gson:gson:2.10.1"), null));

        resolver.addRepository(new RemoteRepository.Builder("maven central", "default", "https://repo.maven.apache.org/maven2/").build());
        resolver.addRepository(new RemoteRepository.Builder("mattmalec-repo", "default", "https://repo.mattmalec.com/repository/releases").build());

        classpathBuilder.addLibrary(resolver);
    }
}
