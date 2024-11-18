package src.service;

import src.model.Menu;

import java.io.IOException;

public class MenuCreatorV2 implements Menu {


    @Override
    public void getMenu() throws IOException {
        System.out.println("Esto es solo para demostrar el uso de la interfaz");
        System.out.println("Al implementarla en la nueva clase, tengo que definir su comportamiento");
        System.out.println("Y en este caso solo deje este mensaje.");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
