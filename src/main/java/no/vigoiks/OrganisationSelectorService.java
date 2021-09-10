package no.vigoiks;

import lombok.extern.slf4j.Slf4j;
import no.vigoiks.model.*;
import no.vigoiks.repository.NIDSAccessSettingsRepository;
import no.vigoiks.repository.NIDSImageRepository;
import no.vigoiks.repository.NIDSSaml2TrustedIDPRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class OrganisationSelectorService {

    @Value("${vigo.authentication.organisation.selector.idp-uri-template:https://idp.felleskomponent.no/nidp/saml2/spsend?id=%s&sid=1}")
    private String idpUriTemplate;

    private final NIDSSaml2TrustedIDPRepository nidsSaml2TrustedIDPRepository;
    private final NIDSAccessSettingsRepository nidsAccessSettingsRepository;
    private final NIDSImageRepository nidsImageRepository;

    public OrganisationSelectorService(NIDSSaml2TrustedIDPRepository nidsSaml2TrustedIDPRepository, NIDSAccessSettingsRepository nidsAccessSettingsRepository, NIDSImageRepository nidsImageRepository) {
        this.nidsSaml2TrustedIDPRepository = nidsSaml2TrustedIDPRepository;
        this.nidsAccessSettingsRepository = nidsAccessSettingsRepository;
        this.nidsImageRepository = nidsImageRepository;
    }

    @PostConstruct
    public void init() {
    }

    @Cacheable("customer-contracts")
    public List<AuthenticationContract> getCustomerContracts() {
        return getContracts()
                .filter(AuthenticationContract::isCustomer)
                .collect(Collectors.toList());
    }

    @Cacheable("common-contracts")
    public List<AuthenticationContract> getCommonContracts() {
        return getContracts()
                .filter(AuthenticationContract::isCommon)
                .collect(Collectors.toList());
    }

    private Stream<AuthenticationContract> getContracts() {
        return nidsSaml2TrustedIDPRepository
                .findAll()
                .stream()
                .map(getNIDSContract())
                .map(getAuthenticationContract());
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
