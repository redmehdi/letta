package es.uvigo.esei.dgss.letta.domain.util.converters;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static java.util.Objects.isNull;

/**
 * {@linkplain AttributeConverter Converter} between {@link LocalDateTime}
 * instances and {@link Timestamp} ones. This allows LocalDateTime objects to
 * be directly persisted as SQL Timestamps at the database level, and vice versa.
 *
 * @author Alberto Gutiérrez Jácome
 */
@Converter(autoApply = true)
public final class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(final LocalDateTime value) {
        return isNull(value) ? null : Timestamp.valueOf(value);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(final Timestamp value) {
        return isNull(value) ? null : value.toLocalDateTime();
    }


}
