package pl.sg.accountant.service;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.jooq.RecordValueReader;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.application.repository.DomainRepository;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.graphql.schema.types.*;

import java.util.List;
import java.util.UUID;

@DgsComponent
public class OtherPartiesDatafetcher {

    private final ModelMapper modelMapper;
    private final OtherPartiesService otherPartiesService;
    private final DomainRepository domainRepository;

    public OtherPartiesDatafetcher(ModelMapper modelMapper, OtherPartiesService otherPartiesService, DomainRepository domainRepository) {
        this.modelMapper = modelMapper;
        this.otherPartiesService = otherPartiesService;
        this.domainRepository = domainRepository;
        modelMapper.getConfiguration().addValueReader(new RecordValueReader());
        modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
    }

    @DgsQuery
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<Client> allClients(
            @RequestHeader("domainId") int domainId
    ) {
        return otherPartiesService.clients(domainRepository.getReferenceById(domainId))
                .stream()
                .map(record -> modelMapper.map(record, Client.class))
                .toList();
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Client createClient(
            @InputArgument("clientCreationInput") ClientCreationInput clientCreationInput,
            @RequestHeader("domainId") int domainId
    ) {
        return modelMapper.map(
                otherPartiesService.createClient(
                        clientCreationInput.getName(),
                        domainRepository.getReferenceById(domainId)),
                Client.class);
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Client updateClient(
            @InputArgument("clientUpdateInput") ClientUpdateInput clientUpdateInput,
            @RequestHeader("domainId") int domainId
    ) {
        return modelMapper.map(
                otherPartiesService.updateClient(
                        clientUpdateInput.getPublicId(),
                        domainRepository.getReferenceById(domainId),
                        clientUpdateInput.getName()),
                Client.class);
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String deleteClient(
            @InputArgument("clientPublicId") UUID clientPublicId,
            @RequestHeader("domainId") int domainId
    ) {
        otherPartiesService.deleteClient(
                clientPublicId,
                domainRepository.getReferenceById(domainId));
        return "OK";
    }
}