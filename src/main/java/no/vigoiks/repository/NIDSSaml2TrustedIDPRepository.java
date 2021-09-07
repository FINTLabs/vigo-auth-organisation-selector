package no.vigoiks.repository;

import no.vigoiks.model.NIDSSaml2TrustedIDP;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NIDSSaml2TrustedIDPRepository extends LdapRepository<NIDSSaml2TrustedIDP> {
}
