package de.cmuellerke.kundensuche.apitest;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class ApiTests implements WithAssertions {

    @Test
    void doATest() {
        log.debug("Hello World (debug)");
        log.info("Hello World (info)");
        log.error("Hello World (error)");
    }
}
