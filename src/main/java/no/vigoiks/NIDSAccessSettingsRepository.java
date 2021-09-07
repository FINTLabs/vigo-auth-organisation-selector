package no.vigoiks;

import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NIDSAccessSettingsRepository extends LdapRepository<NIDSAccessSettings> {


}
