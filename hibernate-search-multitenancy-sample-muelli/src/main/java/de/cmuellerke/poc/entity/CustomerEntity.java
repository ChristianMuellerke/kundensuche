package de.cmuellerke.poc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import java.util.UUID;

@Entity
@Indexed
@Table(name = "customers", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tenant_id", "customer_id"})
})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity extends AbstractBaseEntity {

    @Id
    @Column(name = "customer_id", updatable = false, nullable = false)
    @Builder.Default
    private final UUID customerId = UUID.randomUUID();

    @NotBlank
    @Size(max = 40)
    @FullTextField
    private String forename;

    @NotBlank
    @Size(max = 40)
    @FullTextField
    private String familyname;

    @NotBlank
    @Size(max = 120)
    @NonStandardField(valueBinder = @ValueBinderRef(type = FullnameValueBinder.class))
    private String fullname;
    
}
