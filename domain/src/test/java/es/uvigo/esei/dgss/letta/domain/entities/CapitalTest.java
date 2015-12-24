package es.uvigo.esei.dgss.letta.domain.entities;

import static es.uvigo.esei.dgss.letta.domain.entities.CapitalParameters.aNonExistentCapital;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CapitalTest {
	
	@Test
    public void testConstructor() {
        final String capital = aNonExistentCapital();
        Capital cap = new Capital(capital);
        assertThat(cap.getCapital(), is(equalTo(capital)));
    }

}
