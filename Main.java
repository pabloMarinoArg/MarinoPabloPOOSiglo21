import src.service.MenuCreator;

import java.io.IOException;
import java.util.Scanner;

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
