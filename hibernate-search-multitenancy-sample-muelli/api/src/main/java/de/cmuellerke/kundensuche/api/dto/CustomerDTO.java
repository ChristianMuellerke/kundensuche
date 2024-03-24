package de.cmuellerke.kundensuche.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
    private String id;
    private String forename;
    private String familyname;
    private String tenantId;
    private String fullname;
}
