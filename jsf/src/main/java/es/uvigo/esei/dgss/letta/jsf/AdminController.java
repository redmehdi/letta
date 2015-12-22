package es.uvigo.esei.dgss.letta.jsf;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.service.UserEJB;

/**
 * {@linkplain AdminController} is a JSF controller to manage {@link User}s.
 *
 * @author Alberto Pardellas Soto
 *
 */
@RequestScoped
@ManagedBean(name = "adminController")
public class AdminController {
	@Inject
	private UserEJB userEJB;
	
    /**
     * Retrieves a {@link List} of {@link User}s that they exist in the database.
     *
     * @return an ordered List of {@link User}s.
     */
	public List<User> getUsers() {
		return userEJB.getUsers();
	}
}