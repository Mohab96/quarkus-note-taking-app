package org.acme.Entities;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Entity
@Table(name = "TaggedItem")
public class TaggedItem  extends PanacheEntityBase {
    @Id
    @GeneratedValue(generator = "UUID")
    public UUID id;

    @NotBlank(message = "Note's body cannot be blank")
    public UUID note;

    @NotBlank(message = "Note's body cannot be blank")
    public UUID tag;
}
