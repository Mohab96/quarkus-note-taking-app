package org.acme.Entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Note")
public class Note extends PanacheEntityBase {
    @Id
    @GeneratedValue(generator = "UUID")
    public UUID id;

    @NotBlank(message = "Note's body cannot be blank")
    @Size(min = 2, max = 50, message = "Note's title must be from 2 to 50 characters")
    public String title;

    @NotBlank(message = "Note's body cannot be blank")
    public String body;

//    @ManyToMany(mappedBy = "notesList")
//    public List<Tag> tagsList = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "note_tag",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    public List<Tag> tagsList = new ArrayList<>();
}
