package no.vigoiks.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Getter
@Configuration
public class AppConfig {
    @Value("${vigo.authentication.organisation.selector.idp-redirect-uri:https://idp.felleskomponent.no/nidp/saml2/spsend}")
    private String idpRedirectUri;
}
