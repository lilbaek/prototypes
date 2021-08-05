package org.lilbaek.resource;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.lilbaek.domain.RoomDbEntry;
import org.lilbaek.framework.NotFoundException;
import org.lilbaek.repository.RoomRepository;
import org.lilbaek.resource.model.CreateRoomModel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.Response.ok;

@Path("/rooms")
@RequestScoped
public class RoomResource {

	private final RoomRepository repository;

	@Context
	UriInfo uriInfo;

	@Inject
	public RoomResource(RoomRepository repository) {
		this.repository = repository;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get all entries", description = "Get all entries")
	@APIResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, implementation = RoomDbEntry.class)))
	public Response getFindAll() {
		return ok(this.repository.findAll().list()).build();
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get entry by id")
	@APIResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.OBJECT, implementation = RoomDbEntry.class)))
	public Response getPostById(@Parameter(name = "id", in = ParameterIn.PATH, description = "room id") @PathParam("id") final String id) {
		return this.repository.findByIdOptional(id)
				.map(entry -> ok(entry).build())
				.orElseThrow(
						() -> new NotFoundException(id)
				);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Create a new entry")
	@APIResponse(responseCode = "201", headers = {@Header(name = "Location", description = "The URL of the new entry")})
	public Response savePost(@RequestBody(content = { @Content(schema = @Schema(type = SchemaType.OBJECT, implementation = CreateRoomModel.class))}) @Valid CreateRoomModel entry) {
		var saved = this.repository.save(RoomDbEntry.builder().name(entry.getName()).build());
		return created(
				uriInfo.getBaseUriBuilder()
						.path("/rooms/{id}")
						.build(saved.getId())
		).build();
	}
}
