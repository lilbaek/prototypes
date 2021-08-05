package org.lilbaek.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.orm.panache.runtime.JpaOperations;
import org.lilbaek.domain.RoomDbEntry;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class RoomRepository implements PanacheRepositoryBase<RoomDbEntry, String> {

	@Transactional
	public RoomDbEntry save(RoomDbEntry entry) {
		EntityManager em = JpaOperations.INSTANCE.getEntityManager();
		if (entry.getId() == null) {
			em.persist(entry);
			return entry;
		} else {
			return em.merge(entry);
		}
	}
}
