package pl.sg.application.controller;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sg.application.security.annotations.TokenBearerAuth;

@RestController
@RequestMapping("/graphql")
public class GraphqlSchemaControllerImpl implements GraphqlSchemaController {
    private final GraphQLSchema schema;

    public GraphqlSchemaControllerImpl(GraphQLSchema schema) {
        this.schema = schema;
    }

    @Override
    @GetMapping("/schema")
    @TokenBearerAuth(domainMember = false)
    public String schema() {
        return new SchemaPrinter(SchemaPrinter.Options.defaultOptions().includeDirectives(false)).print(schema);
    }
}
