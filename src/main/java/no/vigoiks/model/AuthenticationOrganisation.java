package no.vigoiks.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationOrganisation {
    private String displayName;
    private String url;
}
