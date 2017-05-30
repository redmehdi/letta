package es.uvigo.esei.dgss.letta.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.Friendship;
import es.uvigo.esei.dgss.letta.domain.entities.FriendshipState;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.service.EventEJB;
import es.uvigo.esei.dgss.letta.service.UserEJB;

/**
 * 
 * @author world1mehdi
 *
 */

@ViewScoped
@ManagedBean(name = "friendshipController")
public class FriendshipController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EventEJB eventEJB;

	@Inject
	private Principal currentUserPrincipal;

	@Inject
	private UserEJB userEJB;
	
	private String friendshipState;
	
	private boolean beFriend;
	
	private String loginUser;
	
	private int idEvent;
	
	@PostConstruct
	public void init() {
		
		
	}
	
	public List<String> attendeesLogin(Event event) {
		ArrayList<String> arrayList = new ArrayList<>();
		if(event == null){
			return null;
		}
		for (User user : eventEJB.getAttendeesLogin(event)) {
			arrayList.add(user.getLogin());
		}

		return arrayList;
	}
	
	public FriendshipState state(String friend){
		final FriendshipState statef = userEJB.getFriendshipState(friend);
		return statef;
	}
	
	public void sendRequest() throws IOException {
		 final ExternalContext context =
		 FacesContext.getCurrentInstance().getExternalContext();
		
		 final String login =
		 context.getRequestParameterMap().getOrDefault("login", null);
		try {
			userEJB.sendRequest(login);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public String count(Event event) {
		final int count = eventEJB.countFriendsByEvent(event);
		final String countString = "number's friends who will attend the event is"+count+"";
		return countString;
	}
	
	public String getOwnerEventFriend(Event event) {
		final Friendship friendship = eventEJB.getEventOwnerFriend(event);
		if(friendship == null) {
			return null;
		}
		final String countString = friendship.getFriend().getLogin();
		return "<b>"+countString+"</b> you are friends";
	}
	
	public List<String> getLoginFriend(){
		List<String> name = new ArrayList<String>();
		for(User list : userEJB.getFriend()){
			name.add(list.getLogin());
		}
		return name;
	}
}
