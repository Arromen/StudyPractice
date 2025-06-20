package dao;

import model.Entity;

import java.util.List;
import java.util.UUID;

public interface EntityDAO {
    void add(Entity entity);
    void update(Entity entity);
    void delete(UUID id);
    Entity getById(UUID id);
    List<Entity> getAll();
    List<Entity> search(String keyword);
}
