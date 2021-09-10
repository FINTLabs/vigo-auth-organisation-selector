package no.vigoiks.model;

import lombok.Data;

@Data
public class AuthenticationCardId {
    public static final String TYPE_UNKNOWN = "unknown";
    public static final String TYPE_CUSTOMER = "customer";
    public static final String TYPE_COMMON = "common";

    private String id;
    private String type;

    public static AuthenticationCardId create(String id) {
        AuthenticationCardId authenticationCardId = new AuthenticationCardId();
        if (id.startsWith("urn:")) {
            String[] split = id.split(":");
            authenticationCardId.setId(split[2]);
            authenticationCardId.setType(split[1]);
            return authenticationCardId;
        }
        authenticationCardId.setId(id);
        authenticationCardId.setType(TYPE_UNKNOWN);
        return authenticationCardId;
    }
}
