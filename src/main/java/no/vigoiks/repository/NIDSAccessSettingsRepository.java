package no.vigoiks.repository;

import no.vigoiks.model.NIDSAccessSettings;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NIDSAccessSettingsRepository extends LdapRepository<NIDSAccessSettings> {


}
