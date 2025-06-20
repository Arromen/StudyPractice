package view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Entity;
import service.EntityService;

import java.util.List;

public class MainView extends Application {
    private final EntityService entityService = new EntityService();
    private TableView<Entity> tableView = new TableView<>();
    private Pagination pagination;
    private final int rowsPerPage = 10;

    private void setupPagination() {
        int totalItems = entityService.getAllEntities().size();
        int pageCount = (int) Math.ceil((double) totalItems / rowsPerPage);
        pagination = new Pagination(pageCount, 0);
        pagination.setPageFactory(this::createPage);
    }

    private TableView<Entity> createPage(int pageIndex) {
        int from = pageIndex * rowsPerPage;
        int to = Math.min(from + rowsPerPage, entityService.getAllEntities().size());
        tableView.setItems(FXCollections.observableArrayList(
                entityService.getAllEntities().subList(from, to)
        ));
        return tableView;
    }

    @Override
    public void start(Stage stage) {
        // Настройка колонок таблицы
        TableColumn<Entity, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Entity, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        tableView.getColumns().addAll(nameCol, descCol);
        refreshTable();

        // Кнопки управления
        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> showAddDialog());

        Button editBtn = new Button("Edit");
        editBtn.setOnAction(e -> {
            Entity selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showEditDialog(selected);
            } else {
                showAlert("No item selected!");
            }
        });

        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> deleteSelectedEntity());

        // Поиск
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            tableView.setItems(FXCollections.observableArrayList(
                    entityService.searchEntities(newVal)
            ));
        });

        // Двойной клик для редактирования
        tableView.setRowFactory(tv -> {
            TableRow<Entity> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showEditDialog(row.getItem());
                }
            });
            return row;
        });

        // Основной layout
        VBox root = new VBox(
                searchField,
                tableView,
                new HBox(10, addBtn, editBtn, deleteBtn)
        );
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("CRUD App");
        stage.show();
    }

    private void refreshTable() {
        tableView.setItems(FXCollections.observableArrayList(
                entityService.getAllEntities()
        ));
    }

    private void showAddDialog() {
        Dialog<Entity> dialog = new Dialog<>();
        dialog.setTitle("Add New Entity");

        // Поля формы
        TextField nameField = new TextField();
        nameField.setPromptText("Name (3-50 chars)");
        TextArea descField = new TextArea();
        descField.setPromptText("Description (max 255 chars)");

        // Валидация
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().setContent(new VBox(10, nameField, descField));
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Обработка нажатия "Add"
        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButton) {
                String name = nameField.getText().trim();
                String desc = descField.getText().trim();

                // Валидация
                if (name.length() < 3 || name.length() > 50) {
                    showAlert("Name must be 3-50 characters!");
                    return null;
                }
                if (desc.length() > 255) {
                    showAlert("Description must be <= 255 characters!");
                    return null;
                }

                return new Entity(name, desc);
            }
            return null;
        });

        // Добавление сущности
        dialog.showAndWait().ifPresent(entity -> {
            entityService.addEntity(entity);
            refreshTable();
        });
    }

    private void showEditDialog(Entity entity) {
        Dialog<Entity> dialog = new Dialog<>();
        dialog.setTitle("Edit Entity");

        // Поля формы (предзаполненные)
        TextField nameField = new TextField(entity.getName());
        nameField.setPromptText("Name (3-50 chars)");
        TextArea descField = new TextArea(entity.getDescription());
        descField.setPromptText("Description (max 255 chars)");

        // Валидация
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().setContent(new VBox(10, nameField, descField));
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        // Обработка нажатия "Save"
        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButton) {
                String name = nameField.getText().trim();
                String desc = descField.getText().trim();

                // Валидация
                if (name.length() < 3 || name.length() > 50) {
                    showAlert("Name must be 3-50 characters!");
                    return null;
                }
                if (desc.length() > 255) {
                    showAlert("Description must be <= 255 characters!");
                    return null;
                }

                entity.setName(name);
                entity.setDescription(desc);
                return entity;
            }
            return null;
        });

        // Обновление сущности
        dialog.showAndWait().ifPresent(updatedEntity -> {
            entityService.updateEntity(updatedEntity);
            refreshTable();
        });
    }

    private void deleteSelectedEntity() {
        Entity selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Delete Entity");
            confirm.setHeaderText("Are you sure?");
            confirm.setContentText("This will delete: " + selected.getName());

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    entityService.deleteEntity(selected.getId());
                    refreshTable();
                }
            });
        } else {
            showAlert("No item selected!");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
