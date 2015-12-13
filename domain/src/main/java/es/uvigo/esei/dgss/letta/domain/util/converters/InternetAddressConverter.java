package es.uvigo.esei.dgss.letta.domain.util.converters;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static java.util.Objects.isNull;
/**
 * {@linkplain AttributeConverter Converter} between {@link InternetAddress}
 * instances and {@link String} ones. This allows InternetAddress objects to
 * be directly persisted as SQL Varchars at the database level, and vice versa.
 *
 * @author Alberto Gutiérrez Jácome
 */
@Converter(autoApply = true)
public class InternetAddressConverter implements AttributeConverter<InternetAddress, String> {

    @Override
    public String convertToDatabaseColumn(final InternetAddress value) {
        return isNull(value) ? null : value.toString();
    }

    @Override
    public InternetAddress convertToEntityAttribute(final String value) {
        try {
            return isNull(value) ? null : new InternetAddress(value);
        } catch (final AddressException ae) {
            throw new IllegalStateException(
                "Invalid email address stored in database: " + value
            );
        }
    }

}
