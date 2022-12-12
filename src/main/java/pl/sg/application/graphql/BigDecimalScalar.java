package pl.sg.application.graphql;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.math.BigDecimal;

@DgsScalar(name = "BigDecimal")
public class BigDecimalScalar implements Coercing<BigDecimal, String> {
    @Override
    public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
        if (dataFetcherResult instanceof BigDecimal) {
            return ((BigDecimal) dataFetcherResult).toPlainString();
        } else {
            throw new CoercingSerializeException("Not a valid BigDecimal");
        }
    }

    @Override
    public BigDecimal parseValue(Object input) throws CoercingParseValueException {
        try {
            return new BigDecimal(input.toString());
        } catch (NumberFormatException ex) {
            throw new CoercingParseValueException("Value is not a valid BigDecimal", ex);
        }
    }

    @Override
    public BigDecimal parseLiteral(Object input) throws CoercingParseLiteralException {
        if (input instanceof StringValue) {
            return this.parseValue(((StringValue) input).getValue());
        }
        throw new CoercingParseLiteralException("Value is not a valid BigDecimal");
    }
}