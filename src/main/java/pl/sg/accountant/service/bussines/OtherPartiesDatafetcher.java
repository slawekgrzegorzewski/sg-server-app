package pl.sg.accountant.service.bussines;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.jooq.RecordValueReader;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.accountant.model.bussines.Supplier;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.graphql.schema.types.*;

import java.util.List;
import java.util.UUID;

@DgsComponent
public class OtherPartiesDatafetcher {

    private final ModelMapper modelMapper;
    private final OtherPartiesService otherPartiesService;

    public OtherPartiesDatafetcher(ModelMapper modelMapper, OtherPartiesService otherPartiesService) {
        this.modelMapper = modelMapper;
        this.otherPartiesService = otherPartiesService;
        modelMapper.getConfiguration().addValueReader(new RecordValueReader());
        modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
    }

    @DgsQuery
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<Client> allClients(
            @RequestHeader("domainId") int domainId
    ) {
        return otherPartiesService.clients(domainId)
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
                        domainId),
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
                        domainId,
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
                domainId);
        return "OK";
    }

    @DgsQuery
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<Supplier> allSuppliers(
            @RequestHeader("domainId") int domainId
    ) {
        return otherPartiesService.suppliers(domainId)
                .stream()
                .map(record -> modelMapper.map(record, Supplier.class))
                .toList();
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Supplier createSupplier(
            @InputArgument("supplierCreationInput") SupplierCreationInput supplierCreationInput,
            @RequestHeader("domainId") int domainId
    ) {
        return modelMapper.map(
                otherPartiesService.createSupplier(
                        supplierCreationInput.getName(),
                        domainId),
                Supplier.class);
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Supplier updateSupplier(
            @InputArgument("supplierUpdateInput") SupplierUpdateInput supplierUpdateInput,
            @RequestHeader("domainId") int domainId
    ) {
        return modelMapper.map(
                otherPartiesService.updateSupplier(
                        supplierUpdateInput.getPublicId(),
                        domainId,
                        supplierUpdateInput.getName()),
                Supplier.class);
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String deleteSupplier(
            @InputArgument("supplierPublicId") UUID supplierPublicId,
            @RequestHeader("domainId") int domainId
    ) {
        otherPartiesService.deleteSupplier(
                supplierPublicId,
                domainId);
        return "OK";
    }
}