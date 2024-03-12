package org.acme.Endpoints;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.Entities.Note;
import org.acme.Entities.Tag;
import org.acme.Utils.ErrorResponse;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Path("/api/notes")
public class NoteEndpoints {
    @Inject
    Validator validator;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllNotes() {
        List<Note> notes = Note.listAll();
        return Response.ok(notes).build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNote(Note note) {
        if (note == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Note cannot be null").build();
        }

//        else if (note.title == null || note.title.isEmpty()) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Title cannot be null or empty").build();
//        } else if (note.body == null || note.body.isEmpty()) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Body cannot be null or empty").build();
//        }

        Set<ConstraintViolation<Note>> noteViolations = validator.validate(note);

        // this line of code is converting a Set of ConstraintViolation<Note> objects into a Set of ConstraintViolation<?> objects
        Set<ConstraintViolation<?>> violations = new HashSet<>(noteViolations);

        if (!violations.isEmpty()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(violations))
                    .build();
        }

        note.persist();

        if (note.isPersistent()) {
            return Response.created(URI.create("/api/notes" + note.id)).entity(note).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNoteById(@PathParam("id") UUID id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID cannot be null").build();
        } else if (id.toString().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID cannot be empty").build();
        }

        Note note = Note.findById(id);

        if (note == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(note).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteNote(@PathParam("id") UUID id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID cannot be null").build();
        } else if (id.toString().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID cannot be empty").build();
        }

        Note note = Note.findById(id);
        if (note == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        note.delete();
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateNote(@PathParam("id") UUID id, Note newNote) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID cannot be null").build();
        }

//        else if (id.toString().isEmpty()) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("ID cannot be empty").build();
//        }

        Set<ConstraintViolation<Note>> noteViolations = validator.validate(newNote);
        // this line of code is converting a Set of ConstraintViolation<Note> objects into a Set of ConstraintViolation<?> objects
        Set<ConstraintViolation<?>> violations = new HashSet<>(noteViolations);

        if (!violations.isEmpty()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(violations))
                    .build();
        }

        Note oldNote = Note.findById(id);

        if (oldNote == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (newNote.title != null && !newNote.title.isEmpty())
            oldNote.title = newNote.title;

        if (newNote.body != null && !newNote.body.isEmpty())
            oldNote.body = newNote.body;

        oldNote.persist();

        if (oldNote.isPersistent())
            return Response.ok(oldNote).build();

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/{id}/tags/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listTags(@PathParam("id") UUID note_id) {
        if (note_id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID cannot be null").build();
        } else if (note_id.toString().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID cannot be empty").build();
        }

        Note note = Note.findById(note_id);

        if (note == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Tag> tags = note.tagsList;
        return Response.ok(tags).build();
    }

    @POST
    @Path("/{id}/tags/{tagId}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTag(@PathParam("id") UUID id, @PathParam("tagId") UUID tagId) {
        if (id == null || tagId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else if (id.toString().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID cannot be empty").build();
        }

        Note note = Note.findById(id);
        Tag tag = Tag.findById(tagId);

        if (note == null || tag == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (note.tagsList.contains(tag)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Tag already exists").build();
        }

        note.tagsList.add(tag);
        note.persist();

        if (note.isPersistent()) {
            return Response.created(URI.create("/api/notes/" + note.id + "/tags/" + tag.id)).entity(note).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Path("/{id}/tags/{tagId}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeTag(@PathParam("id") UUID id, @PathParam("tagId") UUID tagId) {
        Note note = Note.findById(id);
        Tag tag = Tag.findById(tagId);

        if (note == null || tag == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (!note.tagsList.contains(tag)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Tag does not exist").build();
        }

        note.tagsList.remove(tag);
        note.persist();

        if (note.isPersistent()) {
            return Response.noContent().build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
