package es.uvigo.esei.dgss.letta.jsf;

import static es.uvigo.esei.dgss.letta.domain.entities.EventType.CINEMA;
import static es.uvigo.esei.dgss.letta.domain.entities.EventType.INTERNET;
import static es.uvigo.esei.dgss.letta.domain.entities.EventType.LITERATURE;
import static es.uvigo.esei.dgss.letta.domain.entities.EventType.MUSIC;
import static es.uvigo.esei.dgss.letta.domain.entities.EventType.SPORTS;
import static es.uvigo.esei.dgss.letta.domain.entities.EventType.THEATRE;
import static es.uvigo.esei.dgss.letta.domain.entities.EventType.TRAVELS;
import static es.uvigo.esei.dgss.letta.domain.entities.EventType.TV;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.validation.constraints.Size;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.EventType;

/**
 * {@linkplain CreateEventController} is a {@link JSFController} to create
 * LETTA's events
 * 
 * @author jacasanova
 * @author redouane
 *
 */
@RequestScoped
@ManagedBean(name = "createEventController")
public class CreateEventController implements JSFController {

	private String error;
	private String title;
	private String shortDescription;
	private String location;
	private Date date;
	private EventType type;
	private Map<String, EventType> types = new HashMap<String, EventType>();
	private Event createdEvent;

	@PostConstruct
	public void init() {
		types = new HashMap<String, EventType>();
		types.put("Cinema", CINEMA);
		types.put("Literature", LITERATURE);
		types.put("Music", MUSIC);
		types.put("Tv", TV);
		types.put("Sports", SPORTS);
		types.put("Internet", INTERNET);
		types.put("Travels", TRAVELS);
		types.put("Theatre", THEATRE);
	}

	public String doCreate() {
		setCreatedEvent(
				new Event(type, title, shortDescription, date, location));
		System.out.println("Tipo  " + type);
		System.out.println("Title " + title);
		System.out.println("Descripcion " + shortDescription);
		System.out.println("Fecha " + date.toString());
		System.out.println("Localizacion " + location);
		// registration = new User(login, password, email);
		// try{
		// userEJB.registerUser(registration);
		// error = false;
		// return redirectTo("index.xhtml");
		// }catch(final LoginDuplicateException e){
		// error = true;
		// errorMessage = "Login already exists";
		// return getRootViewId();
		// } catch (EmailDuplicateException e) {
		// error = true;
		// errorMessage = "Email already exists";
		// return getRootViewId();
		// }
		return "";
	}

	/**
	 * Getter method of error variable
	 * 
	 * @return error global variable
	 */
	public String getError() {
		return error;
	}

	/**
	 * Setter method of error variable
	 * 
	 * @param error
	 *            global variable
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Getter method of title variable
	 * 
	 * @return method variable
	 */
	@Size(min = 1, max = 20, message = "Title must be between 1 and 20 characters")
	public String getTitle() {
		return title;
	}

	/**
	 * Setter method of title variable
	 * 
	 * @param title
	 *            global variable
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter method of shortDescription variable
	 * 
	 * @return shortDescription variable
	 */
	@Size(min = 1, max = 50, message = "Description must be between 1 and 50 characters")
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * Setter method of shortDescription variable
	 * 
	 * @param shortDescription
	 *            global variable
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * Getter method of location variable
	 * 
	 * @return location global variable
	 */
	@Size(min = 1, max = 100, message = "Location must be between 1 and 20 characters")
	public String getLocation() {
		return location;
	}

	/**
	 * Setter method of location variable
	 * 
	 * @param location
	 *            global variable
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Getter method of date variable
	 * 
	 * @return global variable
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Setter method of date variable
	 * 
	 * @param date
	 *            global variable
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Getter method of type variable
	 * 
	 * @return type global variable
	 */
	public EventType getType() {
		return type;
	}

	/**
	 * Setter method of type variable
	 * 
	 * @param type
	 *            global variable
	 */
	public void setType(EventType type) {
		this.type = type;
	}

	/**
	 * Getter method of types variable
	 * 
	 * @return types global variable
	 */
	public Map<String, EventType> getTypes() {
		return types;
	}

	/**
	 * Setter method of types variable
	 * 
	 * @param types
	 *            global variable
	 */
	public void setTypes(Map<String, EventType> types) {
		this.types = types;
	}

	/**
	 * Getter method of createdEvent variable
	 * 
	 * @return createdEvent global variable
	 */
	public Event getCreatedEvent() {
		return createdEvent;
	}

	/**
	 * Setter method of createdEvent variable
	 * 
	 * @param createdEvent
	 *            global variable
	 */
	public void setCreatedEvent(Event createdEvent) {
		this.createdEvent = createdEvent;
	}

}
