package es.uvigo.esei.dgss.letta.rest.util;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import es.uvigo.esei.dgss.letta.rest.JaxRsActivator;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;

public final class RestIntegrationTestBuilder {

    private boolean mailer = false;

    private final WebArchive deployment = create(WebArchive.class, "test.war");

    private RestIntegrationTestBuilder() {
        deployment.addClass(JaxRsActivator.class);
        deployment.addPackages(true, getClass().getPackage());
        deployment.addPackages(true, "es.uvigo.esei.dgss.letta.domain");
        deployment.addPackages(true, "es.uvigo.esei.dgss.letta.service");
        deployment.addPackages(true, "es.uvigo.esei.dgss.letta.rest.util");
        deployment.addAsResource("test-persistence.xml", "META-INF/persistence.xml");
        deployment.addAsWebInfResource("jboss-web.xml");
        deployment.addAsWebInfResource("web.xml");
    }

    public static RestIntegrationTestBuilder deployment() {
        return new RestIntegrationTestBuilder();
    }

    public RestIntegrationTestBuilder withTestMailer() {
        mailer = true;
        return this;
    }

    public RestIntegrationTestBuilder withClasses(final Class<?> ... classes) {
        deployment.addClasses(classes);
        return this;
    }

    public RestIntegrationTestBuilder withPackages(final Package ... packages) {
        deployment.addPackages(false, packages);
        return this;
    }

    public Archive<WebArchive> build() {
        final Asset beans = mailer
            ? new ClassLoaderAsset("beans-mailer.xml")
            : EmptyAsset.INSTANCE;

        return deployment.addAsWebInfResource(beans, "beans.xml");
    }

}
