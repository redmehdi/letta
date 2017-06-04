package es.uvigo.esei.dgss.letta.jsf;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.Principal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.model.StreamedContent;

import es.uvigo.esei.dgss.letta.bean.UserBean;
import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.Friendship;
import es.uvigo.esei.dgss.letta.domain.entities.FriendshipState;
import es.uvigo.esei.dgss.letta.service.EventEJB;
import es.uvigo.esei.dgss.letta.service.UserEJB;
import es.uvigo.esei.dgss.letta.transform.UserTransform;

/**
 * 
 * @author world1mehdi
 *
 */

@ViewScoped
@ManagedBean(name = "friendshipController")
public class FriendshipController extends UserTransform implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EventEJB eventEJB;

	@Inject Principal currentUserPrincipal;

	@Inject
	private UserEJB userEJB;

	public List<UserBean> attendeesLogin(Event event) {
		if (event == null) {
			return null;
		}
		return transformUsers(eventEJB.getAttendeesLogin(event), this.currentUserPrincipal.getName());
	}

	public FriendshipState state(String friend) {
		return userEJB.getFriendshipState(friend);
	}

	public void sendRequest() throws IOException {
		final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

		final String login = context.getRequestParameterMap().getOrDefault("login", null);
		try {
			userEJB.sendRequest(login);
			context.redirect("index.xhtml");
		} catch (final Exception e) {
			// TODO: handle exception
		}
	}
	
	public void acceptRequest() throws IOException {
		final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

		final String login = context.getRequestParameterMap().getOrDefault("login", null);
		try {
			userEJB.acceptOrRejectFriendRequest(login, true);
			context.redirect("index.xhtml");
		} catch (final Exception e) {
			// TODO: handle exception
		}
	}
	
	public void cancelRequest() throws IOException {
		final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

		final String login = context.getRequestParameterMap().getOrDefault("login", null);
		userEJB.cancelFriendship(login);
		context.redirect("index.xhtml");
		try {
			userEJB.cancelFriendship(login);
		} catch (final Exception e) {
			// TODO: handle exception
		}
	}
	
	public void cancelPending() throws IOException {
		final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

		final String login = context.getRequestParameterMap().getOrDefault("login", null);
		userEJB.cancelPendingRequest(login);
		context.redirect("index.xhtml");
		try {
			userEJB.cancelFriendship(login);
		} catch (final Exception e) {
			// TODO: handle exception
		}
	}
	
	public void rejectRequest() throws IOException {
		final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

		final String login = context.getRequestParameterMap().getOrDefault("login", null);
		try {
			userEJB.acceptOrRejectFriendRequest(login, false);
			context.redirect("index.xhtml");
		} catch (final Exception e) {
			// TODO: handle exception
		}
	}

	public String count(Event event) {
		final int count = eventEJB.countFriendsByEvent(event);
		final String countString = "" + count + "";
		return countString;
	}

	public UserBean getOwnerEventFriend(Event event) {
		final Friendship friendship = eventEJB.getEventOwnerFriend(event);
		if (friendship == null) {
			return null;
		}
		return transformUser(friendship.getFriend(), this.currentUserPrincipal.getName());
	}

	public List<UserBean> getFriendData() {
		return transformUsers(userEJB.getFriend(), this.currentUserPrincipal.getName());
	}

}
