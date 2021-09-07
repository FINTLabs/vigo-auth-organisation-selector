package no.vigoiks.model;

import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Data
@Entry(objectClasses = {"top", "nidsAccessSettings"})
public final class NIDSAccessSettings {

    @Id
    private Name dn;


    @Attribute(name = "nidsCardID")
    private String nidsCardId;

    @Attribute(name = "nidsCardText")
    private String nidsCardText;
}
