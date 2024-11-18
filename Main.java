import src.model.Menu;
import src.service.MenuCreator;
import src.service.MenuCreatorV2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// Nombre y apellido: Marino, Pablo Jesus
// DNI: 35627837
// Legajo:
public class Main {
    public static void main(String[] args) {
        Menu menu;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ingrese con cual interfaz comenzar: ");
        System.out.println("1 - interfaz con todos los comandos disponibles");
        System.out.println("2 - interfaz con un mensaje que solo sirve \n para demostrar" +
                " el uso de las interfaces en java");
            try {
                int option =  Integer.parseInt(reader.readLine().trim());


                menu = switch (option) {
                    case 2 -> new MenuCreatorV2();
                    default -> new MenuCreator();
                };


                menu.getMenu();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
}
