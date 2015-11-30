package es.uvigo.esei.dgss.letta.jsf.pages;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import es.uvigo.esei.dgss.letta.domain.entities.Event;

public class EventsList {
	@FindBy(css = "div.overlay")
	private List<EventRow> rowEvent;
	
	public EventRow getEventRow(final Event event) {
		for (EventRow row : rowEvent) {
			if (row.hasEvent(event))
				return row;
		}		
		throw new IllegalArgumentException("No row for event: " + event.getId());
	}
	
	public void join(final Event event){
		guardHttp(this.getEventRow(event).getButtonJoin()).click();
	}
	
	public static class EventRow {
		@FindBy(className = "buttonJoin")
		private WebElement buttonJoin;

		@FindBy(className = "eventId")
		private WebElement eventId;
		
		public boolean hasEvent(final Event event) {
			final String id = Integer.toString(event.getId());
			final String eventIdValue = this.getEventId().getAttribute("value");
			
			return id.equals(eventIdValue);
		}
		
		public WebElement getButtonJoin() {
			return this.buttonJoin;
		}
		
		public WebElement getEventId(){
			return this.eventId;
		}
	}
}
