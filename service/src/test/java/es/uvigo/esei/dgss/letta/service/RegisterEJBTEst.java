package es.uvigo.esei.dgss.letta.service;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

import es.uvigo.esei.dgss.letta.domain.entities.User;

@RunWith(Arquillian.class)
@CleanupUsingScript({ "cleanup.sql" })
public class RegisterEJBTEst {
	@Inject
	private RegisterEJB facade;

	@Deployment
	public static Archive<?> createDeploymentPackage() {
		return ShrinkWrap.create(JavaArchive.class, "test.jar")
				.addClass(RegisterEJB.class)
				.addPackage(User.class.getPackage())
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsManifestResource("test-persistence.xml",
						"persistence.xml");
	}

//	@Test
//	public void testCreateUser() {
//		final User u = new User("login", "password", "email");
//		assertThat(u, is(not(null)));
//	}
//
//	@Test(expected = NullPointerException.class)
//	public void testCreateNullUser() {
//		new User(null, "password", "email");
//	}
//	
//	@Test
//	public void testConfirmRegistration() {
//		facade.userConfirmation("login");
//		assertThat(facade.userConfirmation("login").isConfirmed(), is(true));
//	}
}