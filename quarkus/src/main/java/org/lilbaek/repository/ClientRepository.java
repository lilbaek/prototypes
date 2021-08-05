package org.lilbaek.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.orm.panache.runtime.JpaOperations;
import org.lilbaek.domain.ClientDbEntry;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class ClientRepository implements PanacheRepositoryBase<ClientDbEntry, String> {

	@Transactional
	public ClientDbEntry save(ClientDbEntry entry) {
		EntityManager em = JpaOperations.INSTANCE.getEntityManager();
		if (entry.getId() == null) {
			em.persist(entry);
			return entry;
		} else {
			return em.merge(entry);
		}
	}
}
