package es.uvigo.esei.dgss.letta.domain.entities;

import static es.uvigo.esei.dgss.letta.domain.entities.CapitalParameters.aDistance;
import static es.uvigo.esei.dgss.letta.domain.entities.CapitalParameters.aNonExistentCapital;
import static es.uvigo.esei.dgss.letta.domain.entities.CapitalParameters.anotherNonExistentCapital;
import static es.uvigo.esei.dgss.letta.domain.entities.CapitalParameters.aCapital;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CapitalDistancesTest {
	
	@Test
    public void testConstructor() {
        final String capital_A = aNonExistentCapital();
        final String capital_B = anotherNonExistentCapital();
        final int	 distance  = aDistance();
        CapitalDistances capDistance = new CapitalDistances(capital_A, capital_B, distance);
        assertThat(capDistance.getCapital_A(), is(equalTo(capital_A)));
        assertThat(capDistance.getCapital_B(), is(equalTo(capital_B)));
        assertThat(capDistance.getDistance(), is(equalTo(distance)));
    }
	

	@Test
    public void testConstructorException() {
        final String capital_A = aCapital();
        final String capital_B = aNonExistentCapital();
        final int	 distance  = aDistance();
        CapitalDistances capDistance = new CapitalDistances(capital_A, capital_B, distance);
    }


}
