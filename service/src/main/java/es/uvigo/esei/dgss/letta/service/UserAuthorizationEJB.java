package es.uvigo.esei.dgss.letta.service;

import java.security.Principal;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.uvigo.esei.dgss.letta.domain.entities.User;

import static java.util.Optional.ofNullable;

/**
 * {@linkplain UserAuthorizationEJB} is a service bean providing useful
 * authorization and authentication methods, built around {@link Principal}.
 *
 * @author Alberto Gutiérrez Jácome
 * @author Adrián Rodríguez Fariña
 */
@Stateless
public class UserAuthorizationEJB {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Principal principal;

    /**
     * Retrieves the current identified {@link User} from the database. It uses
     * the current {@link Principal#getName() Principal name} as the login to be
     * found. Only the "USER" {@linkplain RolesAllowed role is allowed} to call
     * this method. If the user is not found, then a {@link SecurityException}
     * is thrown.
     *
     * @return The {@link User} that is currently identified in the system.
     *
     * @throws SecurityException if the login provided by the principal is not
     *         found in the database (!!!).
     */
    @RolesAllowed({"USER", "ADMIN"})
    public User getCurrentUser() throws SecurityException {
        return ofNullable(em.find(User.class, principal.getName()))
              .orElseThrow(SecurityException::new);
    }

}
