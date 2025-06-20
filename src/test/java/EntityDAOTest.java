import dao.Database;
import dao.EntityDAO;
import dao.EntityDAOImpl;
import model.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class EntityDAOTest {
    private EntityDAO dao = new EntityDAOImpl();

    @BeforeEach
    void clearDatabase() {
        // Очистка таблицы перед каждым тестом
        try (Connection conn = Database.getConnection()) {
            conn.createStatement().execute("DELETE FROM entities");
        } catch (SQLException e) {
            fail("Failed to clear DB", e);
        }
    }

    @Test
    void testAddAndGetEntity() {
        Entity entity = new Entity("Test", "Description");
        dao.add(entity);
        Entity saved = dao.getById(entity.getId());
        assertEquals("Test", saved.getName());
    }

    @Test
    void testSearch() {
        dao.add(new Entity("Java", "Language"));
        dao.add(new Entity("Python", "Language"));
        List<Entity> results = dao.search("Java");
        assertEquals(1, results.size());
    }
}
