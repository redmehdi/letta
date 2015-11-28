package es.uvigo.esei.dgss.letta.service;

import javax.ejb.EJB;
import javax.ejb.EJBAccessException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import es.uvigo.esei.dgss.letta.domain.entities.IsEqualsToUser;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.domain.entities.UsersDataset;
import es.uvigo.esei.dgss.letta.service.util.security.RoleCaller;
import es.uvigo.esei.dgss.letta.service.util.security.TestPrincipal;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import static es.uvigo.esei.dgss.letta.domain.entities.IsEqualsToUser.equalsToUser;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.nonExistentUser;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.users;

@RunWith(Arquillian.class)
@CleanupUsingScript("cleanup.sql")
public class UserAuthorizationEJBTest {

    @Inject
    private UserAuthorizationEJB auth;

    @Inject
    private TestPrincipal principal;

    @EJB(name = "user-caller")
    private RoleCaller asUser;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Deployment
    public static Archive<?> createDeploymentPackage() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
              .addClasses(UserAuthorizationEJB.class)
              .addPackage(UsersDataset.class.getPackage())
              .addPackage(IsEqualsToUser.class.getPackage())
              .addPackage(TestPrincipal.class.getPackage())
              .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
              .addAsWebInfResource("jboss-web.xml")
              .addAsWebInfResource("beans-principal.xml", "beans.xml");
    }

    @Test
    @UsingDataSet("users.xml")
    @ShouldMatchDataSet("users.xml")
    public void testGetCurrentUserReturnsTheCurrentIdentifiedUser() {
        for (final User expected : users()) {
            principal.setName(expected.getLogin());

            final User actual = asUser.throwingCall(auth::getCurrentUser);
            assertThat(actual, is(equalsToUser(expected)));
        }
    }

    @Test
    public void testGetCurrentUserFailsIfIdentifiedUserIsNotFoundInDatabase() {
        thrown.expect(EJBTransactionRolledbackException.class);
        thrown.expectCause(is(instanceOf(SecurityException.class)));

        principal.setName(nonExistentUser().getLogin());
        asUser.throwingRun(auth::getCurrentUser);
    }

    @Test
    public void testGetCurrentUserCannotByCalledByUnauthorizeedUsers() {
        thrown.expect(EJBAccessException.class);

        auth.getCurrentUser();
    }

}
