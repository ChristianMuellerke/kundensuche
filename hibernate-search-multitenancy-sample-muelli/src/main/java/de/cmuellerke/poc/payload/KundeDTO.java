package de.cmuellerke.poc.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KundeDTO {
    private String id;
    private String vorname;
    private String nachname;
    private String tenantId;
}
