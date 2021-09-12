package no.vigoiks.model;


import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Data
@Entry(objectClasses = {"top", "nidsImage"})
public final class NIDSImage {


    @Id
    private Name dn;

    @Attribute(name = "nidsImage")
    private String nidsImage;

    @Attribute(name = "nidsImageMimeType")
    private String nidsImageMimeType;

}
