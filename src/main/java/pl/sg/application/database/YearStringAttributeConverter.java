package pl.sg.application.database;

import javax.persistence.AttributeConverter;
import java.time.Year;

public class YearStringAttributeConverter implements AttributeConverter<Year, String> {
    @Override
    public String convertToDatabaseColumn(Year attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public Year convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Year.parse(dbData);
    }
}
