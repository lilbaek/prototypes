package org.lilbaek.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.orm.panache.runtime.JpaOperations;
import org.lilbaek.domain.PatientDbEntry;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class PatientRepository implements PanacheRepositoryBase<PatientDbEntry, String> {

	@Transactional
	public PatientDbEntry save(PatientDbEntry entry) {
		EntityManager em = JpaOperations.INSTANCE.getEntityManager();
		if (entry.getId() == null) {
			em.persist(entry);
			return entry;
		} else {
			return em.merge(entry);
		}
	}
}
