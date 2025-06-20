import model.Entity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationTest {
    @Test
    void testNameValidation() {
        Entity entity = new Entity("A", ""); // Слишком короткое имя
        assertThrows(IllegalArgumentException.class, () -> {
            if (entity.getName().length() < 3 || entity.getName().length() > 50) {
                throw new IllegalArgumentException("Name must be 3-50 characters");
            }
        });
    }

    @Test
    void testDescriptionValidation() {
        String longDesc = "a".repeat(256); // 256 символов
        Entity entity = new Entity("Valid", longDesc);
        assertThrows(IllegalArgumentException.class, () -> {
            if (entity.getDescription().length() > 255) {
                throw new IllegalArgumentException("Description too long");
            }
        });
    }
}
