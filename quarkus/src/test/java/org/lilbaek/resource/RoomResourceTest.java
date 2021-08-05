package org.lilbaek.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.lilbaek.domain.RoomDbEntry;
import org.lilbaek.repository.RoomRepository;
import org.lilbaek.resource.model.CreateRoomModel;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@QuarkusTest
public class RoomResourceTest {

	@InjectMock
	RoomRepository repository;

	@Inject
	Jsonb jsonb;

	@Test
	public void getNotExisting_shouldReturn404() {
		when(this.repository.findByIdOptional(anyString()))
				.thenReturn(Optional.empty());
		given()
				.when().get("/rooms/" + UUID.randomUUID())
				.then()
				.statusCode(404);
		verify(this.repository, times(1)).findByIdOptional(anyString());
		verifyNoMoreInteractions(this.repository);
	}

	@Test
	public void getExisting_shouldReturn200() {
		var data = RoomDbEntry.builder().name("Room 1")
				.id(UUID.randomUUID().toString())
				.build();
		when(this.repository.findByIdOptional(anyString()))
				.thenReturn(Optional.ofNullable(data));
		given()
				.when().get("/rooms/test")
				.then()
				.statusCode(200)
				.log().all()
				.body("name", is("Room 1"));

		verify(this.repository, times(1)).findByIdOptional(anyString());
		verifyNoMoreInteractions(this.repository);
	}

	@Test
	public void create_shouldReturn200() {
		var data = RoomDbEntry.builder().name("Rooms")
				.id(UUID.randomUUID().toString())
				.build();
		when(this.repository.save(any(RoomDbEntry.class)))
				.thenReturn(data);
		given()
				.body(new CreateRoomModel("Rooms"))
				.contentType(ContentType.JSON)
			.when()
				.post("/rooms")
			.then()
				.statusCode(201)
				.log().all()
				.header("Location", notNullValue());

		verify(this.repository, times(1)).save(any(RoomDbEntry.class));
		verifyNoMoreInteractions(this.repository);
	}
}
