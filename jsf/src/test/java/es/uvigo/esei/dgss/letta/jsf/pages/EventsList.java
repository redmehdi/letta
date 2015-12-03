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
	
	public void join(final Event event) {
		guardHttp(this.getEventRow(event).getJoinLink()).click();
	}
	
	public static class EventRow {
		@FindBy(className = "join-link")
		private WebElement joinLink;
		
		public boolean hasEvent(final Event event) {
			final String link = this.joinLink.getAttribute("href");
			
			return link.endsWith("?id=" + event.getId());
		}
		
		public WebElement getJoinLink() {
			return joinLink;
		}
	}
}
