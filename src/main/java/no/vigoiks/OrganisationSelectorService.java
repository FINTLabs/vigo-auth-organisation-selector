package no.vigoiks;

import lombok.extern.slf4j.Slf4j;
import no.vigoiks.model.AuthenticationOrganisation;
import no.vigoiks.model.Image;
import no.vigoiks.model.NIDSAccessSettings;
import no.vigoiks.model.NIDSImage;
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
import java.util.stream.Collectors;

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

    @Cacheable("authentication-organisations")
    public List<AuthenticationOrganisation> getAuthenticationOrganisations() {
        return nidsSaml2TrustedIDPRepository
                .findAll()
                .stream()
                .map(nidsSaml2TrustedIDP -> nidsAccessSettingsRepository
                        .findOne(
                                LdapQueryBuilder
                                        .query()
                                        .base(nidsSaml2TrustedIDP.getDn())
                                        .where("objectClass")
                                        .is("nidsAccessSettings"))
                        .orElse(new NIDSAccessSettings()))
                .map(nidsAccessSettings -> AuthenticationOrganisation
                        .builder()
                        .displayName(nidsAccessSettings.getNidsCardText())
                        .url(String.format(idpUriTemplate, nidsAccessSettings.getNidsCardId()))
                        .image(getImage(nidsAccessSettings))
                        .build())
                .collect(Collectors.toList());
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
