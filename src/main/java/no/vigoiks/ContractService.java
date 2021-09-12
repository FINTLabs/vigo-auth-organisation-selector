package no.vigoiks;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.vigoiks.model.*;
import no.vigoiks.repository.NIDSAccessSettingsRepository;
import no.vigoiks.repository.NIDSImageRepository;
import no.vigoiks.repository.NIDSSaml2TrustedIDPRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContractService {

    @Value("${vigo.authentication.organisation.selector.idp-uri-template:https://idp.felleskomponent.no/nidp/saml2/spsend?id=%s&sid=1}")
    private String idpUriTemplate;

    private final NIDSSaml2TrustedIDPRepository nidsSaml2TrustedIDPRepository;
    private final NIDSAccessSettingsRepository nidsAccessSettingsRepository;
    private final NIDSImageRepository nidsImageRepository;

    @Getter
    private List<AuthenticationContract> commonContracts;
    @Getter
    private List<AuthenticationContract> customerContracts;

    public ContractService(NIDSSaml2TrustedIDPRepository nidsSaml2TrustedIDPRepository, NIDSAccessSettingsRepository nidsAccessSettingsRepository, NIDSImageRepository nidsImageRepository) {
        this.nidsSaml2TrustedIDPRepository = nidsSaml2TrustedIDPRepository;
        this.nidsAccessSettingsRepository = nidsAccessSettingsRepository;
        this.nidsImageRepository = nidsImageRepository;
    }

    @PostConstruct
    public void init() {
        refreshContracts();
    }

    @Scheduled(cron = "${vigo.authentication.organisation.selector.refresh.cron:0 */5 * * * *}")
    public void refreshContracts() {
        log.info("Refreshing contracts...");
        List<AuthenticationContract> contracts = getContracts();
        customerContracts = filterContracts(contracts, AuthenticationContract::isCustomer);
        commonContracts = filterContracts(contracts, AuthenticationContract::isCommon);
        log.info("{} common contracts found", commonContracts.size());
        log.info("{} customer contracts found", customerContracts.size());
    }

    private List<AuthenticationContract> filterContracts(List<AuthenticationContract> contracts, Predicate<AuthenticationContract> filter) {
        return contracts
                .stream()
                .filter(filter)
                .collect(Collectors.toList());
    }


    private List<AuthenticationContract> getContracts() {
        return nidsSaml2TrustedIDPRepository
                .findAll()
                .stream()
                .map(getNIDSContract())
                .map(getAuthenticationContract())
                .collect(Collectors.toList());
    }

    private Function<NIDSAccessSettings, AuthenticationContract> getAuthenticationContract() {
        return nidsAccessSettings ->
                AuthenticationContract
                        .create(
                                nidsAccessSettings,
                                getImage(nidsAccessSettings),
                                String.format(idpUriTemplate, nidsAccessSettings.getNidsCardId()
                                )
                        );
    }

    private Function<NIDSSaml2TrustedIDP, NIDSAccessSettings> getNIDSContract() {
        return nidsSaml2TrustedIDP -> nidsAccessSettingsRepository
                .findOne(
                        LdapQueryBuilder
                                .query()
                                .base(nidsSaml2TrustedIDP.getDn())
                                .where("objectClass")
                                .is("nidsAccessSettings"))
                .orElse(new NIDSAccessSettings());
    }

    private Image getImage(NIDSAccessSettings nidsAccessSettings) {
        Optional<NIDSImage> nidsImage = nidsImageRepository.findOne(
                LdapQueryBuilder
                        .query()
                        .base(nidsAccessSettings.getNidsImageReference())
                        .where("objectClass")
                        .is("nidsImage")
        );

        return nidsImage
                .map(image ->
                        Image.builder()
                                .base64Image(image.getNidsImage())
                                .mimeType(image.getNidsImageMimeType())
                                .build()
                )
                .orElse(null);

    }
}
