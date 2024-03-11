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

@Path("/api/tags")
public class TagEndpoints {
    @Inject
    Validator validator;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTags() {
        List<Tag> tags = Tag.listAll();
        return Response.ok(tags).build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTag(Tag tag) {
        if (tag == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Tag cannot be null").build();
        }

//        else if (tag.title == null || tag.title.isEmpty()) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Title cannot be null or empty").build();
//        }

        Set<ConstraintViolation<Tag>> noteViolations = validator.validate(tag);

        // this line of code is converting a Set of ConstraintViolation<Note> objects into a Set of ConstraintViolation<?> objects
        Set<ConstraintViolation<?>> violations = new HashSet<>(noteViolations);

        if (!violations.isEmpty()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(violations))
                    .build();
        }

        tag.persist();

        if (tag.isPersistent()) {
            return Response.created(URI.create("/api/tags/" + tag.id)).entity(tag).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTagById(@PathParam("id") UUID id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID cannot be null").build();
        } else if (id.toString().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID cannot be empty").build();
        }

        Tag tag = Tag.findById(id);

        if (tag == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(tag).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteTag(@PathParam("id") UUID id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID cannot be null").build();
        } else if (id.toString().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID cannot be empty").build();
        }

        Tag tag = Tag.findById(id);

        if (tag == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        tag.delete();
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTag(@PathParam("id") UUID id, Tag tag) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID cannot be null").build();
        } else if (tag == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Tag cannot be null").build();
        }

//        else if (id.toString().isEmpty()) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("ID cannot be empty").build();
//        }
//        else if (tag.title == null || tag.title.isEmpty()) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Title cannot be null or empty").build();
//        }

        Set<ConstraintViolation<Tag>> noteViolations = validator.validate(tag);

        // this line of code is converting a Set of ConstraintViolation<Note> objects into a Set of ConstraintViolation<?> objects
        Set<ConstraintViolation<?>> violations = new HashSet<>(noteViolations);

        if (!violations.isEmpty()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(violations))
                    .build();
        }

        Tag tagToUpdate = Tag.findById(id);

        if (tagToUpdate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        tagToUpdate.title = tag.title;
        tagToUpdate.persist();

        return Response.ok(tagToUpdate).build();
    }
}