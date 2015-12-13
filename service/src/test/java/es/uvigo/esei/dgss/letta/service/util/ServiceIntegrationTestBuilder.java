package es.uvigo.esei.dgss.letta.service.util;

import javax.enterprise.inject.Alternative;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.domain.matchers.IsEqualToUser;
import es.uvigo.esei.dgss.letta.domain.util.converters.LocalDateTimeConverter;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EventAlredyJoinedException;
import es.uvigo.esei.dgss.letta.service.util.mail.TestingMailer;
import es.uvigo.esei.dgss.letta.service.util.security.RoleCaller;
import es.uvigo.esei.dgss.letta.service.util.security.TestPrincipal;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;

/**
 * Utility class providing an Arquillian's {@link Deployment} builder for
 * service layer integration tests. This factors out the repetitive and
 * error-prone stuff to every integration test, mainly: creating the deployment
 * package.
 * <br>
 * The implementation assumes that all domain entities should be packaged, along
 * the service's exceptions sub-package and all testing utils (datasets and
 * matchers). It will also pack the {@code test-persistence.xml} and {@code
 * jboss-web.xml} resources. Extra methods are provided as a fluent API to
 * add every existing {@link Alternative} and required {@link Class Classes} to
 * the package.
 *
 * @author Alberto Gutiérrez Jácome
 * @author Adrián Rodríguez Fariña
 */
public final class ServiceIntegrationTestBuilder {

    private boolean mailer    = false;
    private boolean principal = false;

    private final WebArchive deployment = create(WebArchive.class, "test.war");

    private ServiceIntegrationTestBuilder() {
        deployment.addClass(ServiceIntegrationTestBuilder.class);
        deployment.addPackage(User.class.getPackage());
        deployment.addPackage(LocalDateTimeConverter.class.getPackage());
        deployment.addPackage(EventAlredyJoinedException.class.getPackage());
        deployment.addPackage(IsEqualToUser.class.getPackage());
        deployment.addAsResource("test-persistence.xml", "META-INF/persistence.xml");
        deployment.addAsWebInfResource("jboss-web.xml");
    }

    public static ServiceIntegrationTestBuilder deployment() {
        return new ServiceIntegrationTestBuilder();
    }

    public ServiceIntegrationTestBuilder withTestPrincipal() {
        deployment.addPackage(RoleCaller.class.getPackage());
        deployment.addPackage(TestPrincipal.class.getPackage());
        principal = true;
        return this;
    }

    public ServiceIntegrationTestBuilder withTestMailer() {
        deployment.addPackage(TestingMailer.class.getPackage());
        mailer = true;
        return this;
    }

    public ServiceIntegrationTestBuilder withClasses(final Class<?> ... classes) {
        deployment.addClasses(classes);
        return this;
    }

    public ServiceIntegrationTestBuilder withPackages(final Package ... packages) {
        deployment.addPackages(false, packages);
        return this;
    }

    public Archive<WebArchive> build() {
        final Asset beans =
              mailer && principal ? new ClassLoaderAsset("beans-mailer-principal.xml")
            : mailer              ? new ClassLoaderAsset("beans-mailer.xml")
            : principal           ? new ClassLoaderAsset("beans-principal.xml")
            : EmptyAsset.INSTANCE;

        return deployment.addAsWebInfResource(beans, "beans.xml");
    }
}
