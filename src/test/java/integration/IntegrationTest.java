package integration;

import com.woowa.woowakit.WoowakitApplication;
import integration.helper.DatabaseCleaner;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {
    WoowakitApplication.class, DatabaseCleaner.class})
public class IntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    public void tearDown() {
        databaseCleaner.tableClear();
    }
}
