package org.acme.Endpoints;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.Entities.Tag;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/api/tags")
public class TagEndpoints {
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
        tag.persist();

        if (tag.isPersistent()) {
            return Response.created(URI.create("/api/tags/" + tag.id)).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(tag).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTagById(@PathParam("id") UUID id) {
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
        Tag tagToUpdate = Tag.findById(id);
        if (tagToUpdate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        tagToUpdate.title = tag.title;
        tagToUpdate.persist();

        return Response.ok(tagToUpdate).build();
    }
}