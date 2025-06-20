package dao;

import model.Entity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EntityDAOImplTest {
    private static EntityDAO dao;
    private Entity testEntity;

    @BeforeAll
    static void setupDatabase() {
        Database.init(); // Инициализация БД
        dao = new EntityDAOImpl();
    }

    @BeforeEach
    void createTestEntity() {
        testEntity = new Entity("Test Entity", "This is a test");
        dao.add(testEntity);
    }

    @AfterEach
    void cleanup() {
        // Очистка таблицы после каждого теста
        try (Connection conn = Database.getConnection()) {
            conn.createStatement().execute("DELETE FROM entities");
        } catch (SQLException e) {
            fail("Failed to clear DB", e);
        }
    }

    @Test
    void testAddEntity() {
        Entity savedEntity = dao.getById(testEntity.getId());
        assertNotNull(savedEntity);
        assertEquals("Test Entity", savedEntity.getName());
    }

    @Test
    void testUpdateEntity() {
        testEntity.setName("Updated Name");
        dao.update(testEntity);
        Entity updated = dao.getById(testEntity.getId());
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    void testDeleteEntity() {
        dao.delete(testEntity.getId());
        assertNull(dao.getById(testEntity.getId()));
    }

    @Test
    void testGetAllEntities() {
        dao.add(new Entity("Entity 2", "Description 2"));
        List<Entity> entities = dao.getAll();
        assertEquals(2, entities.size());
    }

    @Test
    void testSearchEntities() {
        List<Entity> results = dao.search("Test");
        assertEquals(1, results.size());
        assertEquals("Test Entity", results.get(0).getName());
    }
}
