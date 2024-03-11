package org.acme.Entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Tag")
public class Tag extends PanacheEntityBase {
    @Id
    @GeneratedValue(generator = "UUID")
    public UUID id;

    @NotBlank(message = "Tag's title cannot be blank")
    public String title;
}
