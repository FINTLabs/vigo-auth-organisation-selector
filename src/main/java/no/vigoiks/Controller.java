package no.vigoiks;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import no.vigoiks.config.AppConfig;
import no.vigoiks.model.AuthenticationContract;
import no.vigoiks.model.RedirectProperties;
import no.vigoiks.model.RedirectResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class Controller {

    private final ContractService contractService;
    private final AppConfig config;
    private final MeterRegistry meterRegistry;


    public Controller(ContractService contractService, AppConfig config, MeterRegistry meterRegistry) {
        this.contractService = contractService;
        this.config = config;
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("contract/customer")
    public ResponseEntity<List<AuthenticationContract>> getCustomerContracts() {
        return ResponseEntity.ok(contractService.getCustomerContracts());
    }

    @GetMapping("contract/common")
    public ResponseEntity<List<AuthenticationContract>> getCommonContracts() {
        return ResponseEntity.ok(contractService.getCommonContracts());
    }

    @PostMapping("contract/redirect")
    public ResponseEntity<RedirectResponse> redirect(@RequestBody RedirectProperties redirectProperties) {
        meterRegistry.counter("vigo.common.auth.contract.redirect", "id", redirectProperties.getId(), "target", redirectProperties.getTarget()).increment();
        RedirectResponse redirectResponse = new RedirectResponse();
        UriComponentsBuilder url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("idp.felleskomponent.no")
                .path("/nidp/saml2/spsend");

        if (StringUtils.hasText(redirectProperties.getId())) {
            url.queryParam("id", redirectProperties.getId());
        }

        if (StringUtils.hasText(redirectProperties.getTarget())) {
            url.queryParam("target", encodeValue(redirectProperties.getTarget()));
        }

        if (StringUtils.hasText(redirectProperties.getSid())) {
            url.queryParam("sid", redirectProperties.getSid());
        }

        redirectResponse.setUrl(url.build().toUriString());
        return ResponseEntity.ok(redirectResponse);
    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.info("Shitt");
            return value;
        }
    }

}
