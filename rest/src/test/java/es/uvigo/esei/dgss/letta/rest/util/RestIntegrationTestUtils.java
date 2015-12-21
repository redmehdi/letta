package es.uvigo.esei.dgss.letta.rest.util;

import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.rest.JaxRsActivator;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class RestIntegrationTestUtils {

    // Disallow construction
    private RestIntegrationTestUtils() { }

    public static final GenericType<List<User>>  asUserList  = new GenericType<List<User>>() { };
    public static final GenericType<Set<User>>   asUserSet   = new GenericType<Set<User>>() { };
    public static final GenericType<List<Event>> asEventList = new GenericType<List<Event>>() { };

    public static WebTarget buildResourceTarget(
        final URL deploymentURL, final Class<?> clazz
    ) {
        return ClientBuilder.newClient()
              .target(deploymentURL.toString())
              .path(getResourcePathOf(clazz));
    }

    public static String getResourcePathOf(final Class<?> clazz) {
        final String prefix = JaxRsActivator.class.getAnnotation(
            ApplicationPath.class
        ).value().substring(1);

        return prefix + "/" + clazz.getAnnotation(Path.class).value();
    }

    public static String getAuthHeaderContent(
        final String login, final String password
    ) {
        final byte[] token = (login + ":" + password).getBytes(UTF_8);
        return "Basic " + Base64.getEncoder().encodeToString(token);
    }

}
