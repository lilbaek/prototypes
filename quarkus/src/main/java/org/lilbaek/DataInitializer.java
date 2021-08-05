package org.lilbaek;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.lilbaek.domain.RoomDbEntry;
import org.lilbaek.repository.RoomRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.logging.Logger;

@ApplicationScoped
public class DataInitializer {
	private final static Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());

	@Inject
	RoomRepository rooms;

	@Transactional
	public void onStart(@Observes StartupEvent ev) {
		LOGGER.info("The application is starting...");

		RoomDbEntry room = RoomDbEntry.builder().name("Room 1").build();
		this.rooms.persist(room);
		this.rooms.flush();
		this.rooms.listAll().forEach(p -> System.out.println("Room:" + p));

		LOGGER.info("Database initialized");
	}

	void onStop(@Observes ShutdownEvent ev) {
		LOGGER.info("The application is stopping...");
	}
}
