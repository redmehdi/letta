package es.uvigo.esei.dgss.letta.jsf;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.Size;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.Event.Category;
import es.uvigo.esei.dgss.letta.service.EventEJB;
import es.uvigo.esei.dgss.letta.service.util.exceptions.UserNotAuthorizedException;

/**
 * {@linkplain ModifyEventController} is a JSF controller to modify LETTA's
 * events
 * 
 * @author Aitor Blanco Míguez
 *
 */
@ViewScoped
@ManagedBean(name = "modifyEventController")
public class ModifyEventController {
	@Inject
	EventEJB eventEJB;

	private boolean error = false;
	private String errorMessage;
	private Event event;
	private String title;
	private String shortDescription;
	private String location;
	private Date date;
	private Category type;
	private Map<String, Category> types = new HashMap<String, Category>();

	@PostConstruct
	public void init() throws IOException {
		final ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		
		final String id = context.getRequestParameterMap().getOrDefault("id", null);
		
		if(id == null){
			context.redirect("index.xhtml");			
		}else{
			event = eventEJB.getEvent(Integer.parseInt(id));
			
			if(event == null){
				context.redirect("index.xhtml");	
				
			}else{			
				title = event.getTitle();
				date = Date.from(event.getDate().atZone(ZoneId.systemDefault()).toInstant());
				shortDescription = event.getSummary();
				location = event.getLocation();
				type = event.getCategory();
			}
		}
		
		types = new HashMap<String, Category>();
		types.put("Cinema", Category.MOVIES);
		types.put("Literature", Category.BOOKS);
		types.put("Music", Category.MUSIC);
		types.put("Tv", Category.TELEVISION);
		types.put("Sports", Category.SPORTS);
		types.put("Internet", Category.INTERNET);
		types.put("Travels", Category.TRAVELS);
		types.put("Theatre", Category.THEATRE);
	}

	/**
	 * Modify an {@link Event}
	 * 
	 * @throws IOException
	 *             if an input/output error occurs
	 */
	public void doModify() throws IOException {
		final ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		try {
			final LocalDateTime date = LocalDateTime.ofInstant(
					this.date.toInstant(), ZoneId.systemDefault());
			event.setTitle(title);
			event.setSummary(shortDescription);
			event.setLocation(location);
			event.setDate(date);
			event.setCategory(type);

			eventEJB.modifyEvent(event);
			this.error = false;
			context.redirect("eventModified.xhtml");
		} catch (NullPointerException e) {
			this.error = true;
			setErrorMessage(e.getMessage());
		} catch (UserNotAuthorizedException e) {
			this.error = true;
			setErrorMessage("This user can not have privilegies to modify this event");
		}
	}

	/**
	 * Getter method of error variable
	 *
	 * @return error global variable
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * Getter method of title variable
	 *
	 * @return title global variable
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
	public void setTitle(final String title) {
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
	public void setShortDescription(final String shortDescription) {
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
	public void setLocation(final String location) {
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
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Getter method of type variable
	 *
	 * @return type global variable
	 */
	public Category getType() {
		return type;
	}

	/**
	 * Setter method of type variable
	 *
	 * @param type
	 *            global variable
	 */
	public void setType(final Category type) {
		this.type = type;
	}

	/**
	 * Getter method of types variable
	 *
	 * @return types global variable
	 */
	public Map<String, Category> getTypes() {
		return types;
	}

	/**
	 * Setter method of types variable
	 *
	 * @param types
	 *            global variable
	 */
	public void setTypes(final Map<String, Category> types) {
		this.types = types;
	}

	/**
	 * Getter method of errorMessage variable
	 *
	 * @return errorMEssage global variable
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Setter method of errorMessage variable
	 *
	 * @param errorMessage
	 *            global variable
	 */
	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
