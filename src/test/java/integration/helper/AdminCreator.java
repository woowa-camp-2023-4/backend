package integration.helper;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

@TestComponent
public class AdminCreator {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void createAdmin() {
		entityManager.createNativeQuery(" insert into members (name, email, role, password) "
				+ " values ('admin', 'admin@woowa.com', 'ADMIN', 'TubwRv8rH2IOdciIs9uvvw==4U3ZkLhSNMCL9zL+sbA+zw==')")
			.executeUpdate();
	}
}
