package no.vigoiks.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationContract {
    private String displayName;
    private String url;
    private Image image;
    private boolean customer;
    private boolean common;

    public static AuthenticationContract create(NIDSAccessSettings nidsAccessSettings, Image image, String url) {
        AuthenticationCardId authenticationCardId = AuthenticationCardId.create(nidsAccessSettings.getNidsCardId());
        AuthenticationContractBuilder authenticationOrganisationBuilder = AuthenticationContract
                .builder()
                .customer(false)
                .common(false)
                .image(image)
                .url(url)
                .displayName(nidsAccessSettings.getNidsCardText());

        if (authenticationCardId.getType().equals(AuthenticationCardId.TYPE_CUSTOMER)) {
            authenticationOrganisationBuilder.customer(true);
        }
        if (authenticationCardId.getType().equals(AuthenticationCardId.TYPE_COMMON)) {
            authenticationOrganisationBuilder.common(true);
        }

        return authenticationOrganisationBuilder.build();
    }
}
