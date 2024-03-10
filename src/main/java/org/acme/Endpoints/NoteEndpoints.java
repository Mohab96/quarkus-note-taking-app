package org.acme.Endpoints;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.Entities.Note;
import org.acme.Entities.Tag;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/api/notes")
public class NoteEndpoints {
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

        note.persist();

        if (note.isPersistent()) {
            return Response.created(URI.create("/api/notes" + note.id)).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(note).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNoteById(@PathParam("id") UUID id) {
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
        Note note = Note.findById(id);
        if (note == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        note.delete();
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateNote(@PathParam("id") UUID id, Note note) {
        Note noteToUpdate = Note.findById(id);
        if (noteToUpdate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        noteToUpdate.title = note.title;
        noteToUpdate.body = note.body;
        noteToUpdate.persist();

        return Response.ok(noteToUpdate).build();
    }

    @GET
    @Path("/{id}/tags")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listTags() {
        List<Tag> tags = Tag.listAll();
        return Response.ok(tags).build();
    }

    @POST
    @Path("/{id}/tags")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTag(@PathParam("id") UUID id, Tag tag) {
        Note note = Note.findById(id);

        if (note == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        note.tagsList.add(tag);
        note.persist();

        if (note.isPersistent()) {
            return Response.created(URI.create("/api/notes/" + note.id + "/tags/" + tag.id)).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(note).build();
    }

    @DELETE
    @Path("/{id}/tags/{tagId}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeTag(@PathParam("id") UUID id, @PathParam("tagId") UUID tagId) {
        Note note = Note.findById(id);
        Tag tag = Tag.findById(tagId);

        note.tagsList.remove(tag);
        note.persist();

        if (note.isPersistent()) {
            return Response.noContent().build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(note).build();
    }
}
