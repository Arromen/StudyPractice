import dao.Database;
import view.MainView;

public class Main {
    public static void main(String[] args) {
        Database.init(); // Инициализация БД
        MainView.launch(MainView.class, args); // Запуск JavaFX
    }
}
