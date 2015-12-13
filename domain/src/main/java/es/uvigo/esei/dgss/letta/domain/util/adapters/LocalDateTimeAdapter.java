package es.uvigo.esei.dgss.letta.domain.util.adapters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * {@linkplain XmlAdapter Adapter} between {@link LocalDateTime} instances and
 * {@link String} ones. Required in order to make Wildfly's Jackson version to
 * properly convert between LocalDateTime instances to JSON values.
 * <br>
 * Fields or methods that return a LocalDateTime should be annotated with an
 * {@link XmlJavaTypeAdapter} annotation pointing to this class.
 * <br>
 * See also (as an automatic alternative to this):
 * https://github.com/FasterXML/jackson-datatype-jsr310
 *
 * @author Alberto Gutiérrez Jácome
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(final String value) throws Exception {
        return LocalDateTime.parse(value, ISO_LOCAL_DATE_TIME);
    }

    @Override
    public String marshal(final LocalDateTime value) throws Exception {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(value);
    }

}