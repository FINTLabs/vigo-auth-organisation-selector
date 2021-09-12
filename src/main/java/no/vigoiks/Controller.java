package no.vigoiks;

import lombok.extern.slf4j.Slf4j;
import no.vigoiks.model.AuthenticationContract;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class Controller {

    private final ContractService contractService;


    public Controller(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("contract/customer")
    public ResponseEntity<List<AuthenticationContract>> getCustomerContracts() {
        return ResponseEntity.ok(contractService.getCustomerContracts());
    }

    @GetMapping("contract/common")
    public ResponseEntity<List<AuthenticationContract>> getCommonContracts() {
        return ResponseEntity.ok(contractService.getCommonContracts());
    }
}
