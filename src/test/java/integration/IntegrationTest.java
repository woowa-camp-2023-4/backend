package integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.woowa.woowakit.WoowakitApplication;

import integration.helper.AdminCreator;
import integration.helper.DatabaseCleaner;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {
	WoowakitApplication.class, DatabaseCleaner.class, AdminCreator.class})
public class IntegrationTest {

	@LocalServerPort
	int port;

	@Autowired
	private DatabaseCleaner databaseCleaner;

	@Autowired
	private AdminCreator adminCreator;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
		adminCreator.createAdmin();
	}

	@AfterEach
	public void tearDown() {
		databaseCleaner.tableClear();
	}
}
