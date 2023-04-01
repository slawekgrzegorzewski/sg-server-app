package pl.sg.ip.graphql;

import com.netflix.graphql.dgs.context.DgsCustomContextBuilder;
import org.jooq.DSLContext;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class IntellectualPropertyContextBuilder implements DgsCustomContextBuilder<IntellectualPropertyContext> {

    private final DSLContext dslContext;
    private final ModelMapper modelMapper;

    public IntellectualPropertyContextBuilder(DSLContext dslContext, ModelMapper modelMapper) {
        this.dslContext = dslContext;
        this.modelMapper = modelMapper;
    }

    @Override
    public IntellectualPropertyContext build() {
        return new IntellectualPropertyContext(dslContext, modelMapper);
    }
}

