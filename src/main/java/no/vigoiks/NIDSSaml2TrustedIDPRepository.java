package no.vigoiks;

import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NIDSSaml2TrustedIDPRepository extends LdapRepository<NIDSSaml2TrustedIDP> {
}
