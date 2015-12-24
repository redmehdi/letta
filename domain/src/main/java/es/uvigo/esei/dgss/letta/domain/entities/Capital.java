package es.uvigo.esei.dgss.letta.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import es.uvigo.esei.dgss.letta.domain.util.annotations.VisibleForJPA;

@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Capital {
	
	@Id
	@Column(length = 20, nullable = false)
	private String capital;
	
    /**
     * Constructs a new instance of {@link Capital}. This empty constructor is
     * required by the JPA framework and <strong>should never be used
     * directly</strong>.
     */
    @VisibleForJPA Capital() { }
    
    /**
     * Capital
     * 
     * @param c a neme of a Capital
     */
	public Capital(String c) {
		setCapital(c);
	}

	/**
	 * Returns the name of the {@link Capital}
	 * 
	 * @return the name of the {@link Capital}
	 */
	public String getCapital() {
		return capital;
	}
	
	/**
	 * Sets the name of the {@link Capital}
	 * 
	 * @param capital
	 *      name of the {@link Capital}
	 */
	public void setCapital(String capital) {
		this.capital = capital;
	}
	


	
}
