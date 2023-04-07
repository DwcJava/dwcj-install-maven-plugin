package org.dwcj;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

/**
 * DwcjInstall installs the jar in the target dir into BBj.
 */
@Mojo(name = "install", defaultPhase = LifecyclePhase.INSTALL,
    requiresDependencyResolution = ResolutionScope.RUNTIME)
public class DwcjInstall extends AbstractMojo {
  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  MavenProject project;

  @Parameter(property = "deployurl", required = true, readonly = true)
  private String deployurl;

  @Parameter(property = "username", required = false, readonly = true)
  private String username;

  @Parameter(property = "password", required = false, readonly = true)
  private String password;

  @Parameter(property = "token", required = false, readonly = true)
  private String token;

  @Parameter(property = "classname", required = false, readonly = true)
  private String classname;

  @Parameter(property = "publishname", required = false, readonly = true)
  private String publishname;

  @Parameter(property = "debug", required = false, readonly = true)
  private String debug;

  /**
   * The execute method.
   *
   * @throws MojoExecutionException when something fails.
   *
   */
  public void execute() throws MojoExecutionException {
    getLog().info("-------DWCJ Deploy to Server:-------------");
    String buildDirectory = project.getBuild().getDirectory();
    String finalName = project.getBuild().getFinalName();
    java.io.File theJar;
    theJar = new java.io.File(buildDirectory, finalName + ".jar");
    if (!theJar.exists()) {
      getLog().error(
          theJar.getAbsolutePath() + " does not exist! Did you configure a different output dir?");
      return;
    }

    getLog().info("Installing DWCJ App using URL: " + deployurl);


    MimeMultipartData mimeMultipartData = null;
    try {
      mimeMultipartData = MimeMultipartData.newBuilder().withCharset(StandardCharsets.UTF_8)
          .addFile("jar", theJar.toPath(), Files.probeContentType(theJar.toPath())).build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    HttpRequest request = null;
    try {
      request = HttpRequest.newBuilder().header("Content-Type", mimeMultipartData.getContentType())
          .POST(mimeMultipartData.getBodyPublisher()).uri(URI.create(deployurl)).build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    var httpClient = HttpClient.newBuilder().build();
    try {
      var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      getLog().info(response.body());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
