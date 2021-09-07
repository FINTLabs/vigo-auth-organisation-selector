package no.vigoiks;

import lombok.extern.slf4j.Slf4j;
import no.vigoiks.model.AuthenticationOrganisation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class Controller {

    private final OrganisationSelectorService organisationSelectorService;


    public Controller(OrganisationSelectorService organisationSelectorService) {
        this.organisationSelectorService = organisationSelectorService;
    }

    @GetMapping("organisations")
    public ResponseEntity<List<AuthenticationOrganisation>> getOrganisations() {
        return ResponseEntity.ok(organisationSelectorService.getAuthenticationOrganisations());
    }
}
