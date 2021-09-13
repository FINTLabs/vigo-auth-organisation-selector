package no.vigoiks.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Getter
@Configuration
public class AppConfig {
    @Value("${vigo.authentication.organisation.selector.idp-uri-template:https://idp.felleskomponent.no/nidp/saml2/spsend?id=%s&sid=1&target=%s}")
    private String idpUriTemplate;


}
