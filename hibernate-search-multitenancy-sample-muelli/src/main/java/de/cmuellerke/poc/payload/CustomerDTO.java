package de.cmuellerke.poc.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
    private String id;
    private String forename;
    private String familyname;
    private String tenantId;
}
