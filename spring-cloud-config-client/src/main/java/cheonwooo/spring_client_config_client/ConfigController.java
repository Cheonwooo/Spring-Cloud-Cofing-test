package cheonwooo.spring_client_config_client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ConfigController {

    private final MyConfig myConfig;

    @GetMapping("/config")
    public ResponseEntity<String> config() {
        log.info("{}", myConfig);
        return ResponseEntity.ok(myConfig.toString());
    }
}
