package pl.sg.application.graphql;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@DgsScalar(name = "LocalDate")
public class LocalDateScalar implements Coercing<LocalDate, String> {

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
        if (dataFetcherResult instanceof LocalDate) {
            return FORMATTER.format((LocalDate) dataFetcherResult);
        } else {
            throw new CoercingSerializeException("Not a valid LocalDate");
        }
    }

    @Override
    public LocalDate parseValue(Object input) throws CoercingParseValueException {
        try {
            return LocalDate.parse(input.toString(), FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new CoercingParseValueException("Value is not a valid LocalDate", ex);
        }
    }

    @Override
    public LocalDate parseLiteral(Object input) throws CoercingParseLiteralException {
        if (input instanceof StringValue) {
            return this.parseValue(((StringValue) input).getValue());
        }
        throw new CoercingParseLiteralException("Value is not a valid LocalDate");
    }
}