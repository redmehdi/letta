package es.uvigo.esei.dgss.letta.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 100, nullable = false)
	private String title;

	@Column(length = 1000, nullable = false)
	private String body;
	
	/**
     * Default constructor
     */
	public Notification(){}

	/**
	 * Constructor of {@link Notification}
	 * 
	 * @param title
	 *            of the {@link Notification}
	 * @param body
	 *            of the {@link Notification}
	 */
	public Notification(String title, String body) {
		this.title = title;
		this.body = body;
	}

	/**
	 * Returns the title of the {@link Notification}
	 * 
	 * @return the title of the {@link Notification}
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the {@link Notification}
	 * 
	 * @param title
	 *            of the {@link Notification}
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the body of the {@link Notification}
	 * 
	 * @return the body of the {@link Notification}
	 */
	public String getBody() {
		return body;
	}

	/**
	 * Sets the body of the {@link Notification}
	 * 
	 * @param body
	 *            of the {@link Notification}
	 */
	public void setBody(String body) {
		this.body = body;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
