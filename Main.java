import src.service.MenuCreator;

import java.io.IOException;
// Nombre y apellido: Marino, Pablo Jesus
// DNI: 35627837
// Legajo:
public class Main {
    public static void main(String[] args) {
        MenuCreator menu = new MenuCreator();

            try {
                menu.getMenu();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }
}
