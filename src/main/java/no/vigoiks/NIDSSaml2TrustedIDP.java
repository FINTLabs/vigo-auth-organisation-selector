package no.vigoiks;

import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Data
@Entry(objectClasses = {"top", "nidsSaml2TrustedIDP"}, base = "cn=cluster,cn=nids,ou=accessManagerContainer,o=novell")
public class NIDSSaml2TrustedIDP {

    @Id
    private Name dn;

    @Attribute(name = "nidsEnabled")
    private boolean nidsEnabled;

    @Attribute(name = "nidsDisplayName")
    private String nidsDisplayName;
}
