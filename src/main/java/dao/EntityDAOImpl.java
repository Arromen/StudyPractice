package dao;

import model.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityDAOImpl implements EntityDAO  {
    private static final Logger logger = LoggerFactory.getLogger(EntityDAOImpl.class);

    @Override
    public void add(Entity entity) {
        String sql = "INSERT INTO entities(id, name, description, createdAt, updatedAt) VALUES(?,?,?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getId().toString());
            stmt.setString(2, entity.getName());
            stmt.setString(3, entity.getDescription());
            stmt.setString(4, entity.getCreatedAt().toString());
            stmt.setString(5, entity.getUpdatedAt().toString());
            stmt.executeUpdate();
            logger.info("Added entity: {}", entity.getName());
        } catch (SQLException e) {
            logger.error("Failed to add entity", e);
        }
    }

    @Override
    public void update(Entity entity) {
        String sql = "UPDATE entities SET name=?, description=?, updatedAt=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getDescription());
            stmt.setString(3, LocalDateTime.now().toString());
            stmt.setString(4, entity.getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM entities WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Entity getById(UUID id) {
        String sql = "SELECT * FROM entities WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Entity> getAll() {
        List<Entity> entities = new ArrayList<>();
        String sql = "SELECT * FROM entities";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    @Override
    public List<Entity> search(String keyword) {
        List<Entity> entities = new ArrayList<>();
        String sql = "SELECT * FROM entities WHERE name LIKE ? OR description LIKE ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    private Entity mapResultSetToEntity(ResultSet rs) throws SQLException {
        Entity entity = new Entity();
        entity.setId(UUID.fromString(rs.getString("id")));
        entity.setName(rs.getString("name"));  // Автоматически устанавливает значение в StringProperty
        entity.setDescription(rs.getString("description"));
        entity.setCreatedAt(LocalDateTime.parse(rs.getString("createdAt")));
        entity.setUpdatedAt(LocalDateTime.parse(rs.getString("updatedAt")));
        return entity;
    }
}
