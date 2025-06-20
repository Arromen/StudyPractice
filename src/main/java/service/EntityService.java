package service;

import dao.EntityDAO;
import dao.EntityDAOImpl;
import model.Entity;

import java.util.List;
import java.util.UUID;

public class EntityService {
    private final EntityDAO entityDAO;

    public EntityService() {
        this.entityDAO = new EntityDAOImpl();
    }

    public void addEntity(Entity entity) {
        entityDAO.add(entity);
    }

    public void updateEntity(Entity entity) {
        entityDAO.update(entity);
    }

    public void deleteEntity(UUID id) {
        entityDAO.delete(id);
    }

    public List<Entity> getAllEntities() {
        return entityDAO.getAll();
    }

    public List<Entity> searchEntities(String keyword) {
        return entityDAO.search(keyword);
    }
}
