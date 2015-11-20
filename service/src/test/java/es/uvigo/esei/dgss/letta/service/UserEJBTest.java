//package es.uvigo.esei.dgss.letta.service;
//
//import javax.inject.Inject;
//
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.arquillian.persistence.CleanupUsingScript;
//import org.jboss.arquillian.persistence.ShouldMatchDataSet;
//import org.jboss.arquillian.persistence.UsingDataSet;
//import org.jboss.shrinkwrap.api.Archive;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.JavaArchive;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import es.uvigo.esei.dgss.letta.domain.entities.Registration;
//import es.uvigo.esei.dgss.letta.domain.entities.RegistrationsDataset;
//import es.uvigo.esei.dgss.letta.domain.entities.User;
//import es.uvigo.esei.dgss.letta.domain.entities.UsersDataset;
//
//@RunWith(Arquillian.class)
//@CleanupUsingScript({ "cleanup.sql" })
//public class UserEJBTest {
//	@Inject
//	private UserEJB facade;
//	@Inject
//	private TestingMailerEJB tmejb;
//
//	@Deployment
//	public static Archive<?> createDeploymentPackage() {
//		return ShrinkWrap.create(JavaArchive.class, "test.jar")
//				.addClasses(UserEJB.class, TestingMailerEJB.class,
//						RegistrationsDataset.class, UsersDataset.class)
//				.addPackage(User.class.getPackage())
//				.addPackage(Registration.class.getPackage())
//				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
//				.addAsManifestResource("test-persistence.xml",
//						"persistence.xml");
//	}
//
//	@Test
//	@UsingDataSet("registrations-create.xml")
//	@ShouldMatchDataSet("registrations-create.xml")
//	public void testRegisterUser() {
//		
//	}
//}