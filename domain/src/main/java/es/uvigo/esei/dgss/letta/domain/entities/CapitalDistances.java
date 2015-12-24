package es.uvigo.esei.dgss.letta.domain.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import es.uvigo.esei.dgss.letta.domain.util.annotations.VisibleForJPA;

/**
 * {@linkplain CapitalDistances} is a JPA entity that represents a distance 
 * between two capital cities.
 *
 * @author Borja Cordeiro González
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class CapitalDistances implements Serializable  {
	
    @Id
    @JoinColumn(name = "capital_A", referencedColumnName = "capital")
	private String capital_A;
	
    @Id
    @JoinColumn(name = "capital_B", referencedColumnName = "capital")
	@Column(length = 20, nullable = false)
	private String capital_B;
	
	@Column( nullable = false)
	private int distance;

	public String getCapital_A() {
		return capital_A;
	}

	public void setCapital_A(String capital_A) {
		this.capital_A = capital_A;
	}

	public String getCapital_B() {
		return capital_B;
	}

	public void setCapital_B(String capital_B) {
		this.capital_B = capital_B;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
	
    /**
     * Constructs a new instance of {@link CapitalDistances}. This empty constructor is
     * required by the JPA framework and <strong>should never be used
     * directly</strong>.
     */
    @VisibleForJPA CapitalDistances() { }
	
    /**
     * Constructs a new instance of {@link Event}.
     *
     * @param distance distance between two capitals
     * @param capital_A one capital
     * @param capital_B other capital
     * 
     * @throws NullPointerException If any of the given arguments is
     *         {@code null}.
     */
    public CapitalDistances(
            final int           distance,
            final String        capital_A,
            final String        capital_B
        ) throws NullPointerException {
            setCapital_A(capital_A);
            setCapital_B(capital_B);
            setDistance(distance);
        }


}
