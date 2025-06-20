package service;

import dao.Database;
import model.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EntityServiceTest {
    private EntityService service;

    @BeforeEach
    void setup() {
        service = new EntityService();
        // Очистка БД перед каждым тестом
        try (Connection conn = Database.getConnection()) {
            conn.createStatement().execute("DELETE FROM entities");
        } catch (SQLException e) {
            fail("Failed to clear DB", e);
        }
    }

    @Test
    void testAddEntity() {
        Entity entity = new Entity("Service Test", "Test Description");
        service.addEntity(entity);
        List<Entity> entities = service.getAllEntities();
        assertEquals(1, entities.size());
        assertEquals("Service Test", entities.get(0).getName());
    }

    @Test
    void testDeleteEntity() {
        Entity entity = new Entity("To Delete", "Delete me");
        service.addEntity(entity);
        service.deleteEntity(entity.getId());
        assertTrue(service.getAllEntities().isEmpty());
    }

    @Test
    void testSearchEntities() {
        service.addEntity(new Entity("Java", "Language"));
        service.addEntity(new Entity("Python", "Language"));
        List<Entity> results = service.searchEntities("Java");
        assertEquals(1, results.size());
        assertEquals("Java", results.get(0).getName());
    }
}
