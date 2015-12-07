package es.uvigo.esei.dgss.letta.jsf.pages;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import es.uvigo.esei.dgss.letta.domain.entities.Event;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

public class EventsList {

    public static class EventInfo {

        @FindBy(className = "event-title")
        private WebElement titleSpan;

        @FindBy(className = "event-summary")
        private WebElement summarySpan;

        @FindBy(className = "join-link")
        private WebElement joinLink;

        public String getTitle() {
            return titleSpan.getText();
        }

        public String getSummary() {
            return summarySpan.getText();
        }

        public boolean isEvent(final Event event) {
            return joinLink.getAttribute("href")
                  .endsWith("?id=" + event.getId());
        }

        public WebElement getJoinLink() {
            return joinLink;
        }

    }

    @FindBy(css = "div.event-info")
    private List<EventInfo> events;

    public List<EventInfo> getEventInfoList() {
        return events;
    }

    public EventInfo getEventInfoFor(final Event event) {
        return events.stream().filter(e -> e.isEvent(event)).findFirst()
              .orElseThrow(IllegalArgumentException::new);
    }

    public void join(final Event event) {
        guardHttp(getEventInfoFor(event).getJoinLink()).click();
    }

}
