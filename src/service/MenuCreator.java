package src.service;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.System;
import java.util.Objects;
import java.util.Scanner;

public class MenuCreator {
    private BufferedReader reader;
    private String userName;
    private int opcion;

    public void getMenu() throws IOException {
        System.out.println("**********************");
        System.out.println("**********************");
        System.out.println("****Inventory Sys*****");
        System.out.println("**********V1**********");
        System.out.println("**********************");

        System.out.println("Ingrese nombre de usuario: ");

        try {
            userName = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Ingrese una opción a ejecutar");
        do {
            System.out.println();
            System.out.println("1- Ingresar producto");
            System.out.println("2- Eliminar/Modificar producto");
            System.out.println("3- Buscar producto");
            System.out.println("4- Ingresar reserva cliente");
            System.out.println("99- Salir");
            System.out.println();
            try {
                opcion = Integer.parseInt(reader.readLine());
            } catch (Exception e) {
                System.out.println("Ingrese la opcion correcta");
                opcion = 0;
            }


            switch (opcion) {
                case 1:
                    System.out.println("Ingresar producto");
                    break;
                case 2:
                    System.out.println("Eliminar/Modificar producto");
                    break;
                case 3:
                    System.out.println("Buscar producto");
                    break;
                case 4:
                    System.out.println("Ingresar reserva cliente");
                    break;
                default:
                    if (opcion != 99) {
                        System.out.println("No hay opción para eso");
                        break;
                    }
                    System.out.println("Saliendo del sistema...");
                    break;

            }

        } while (opcion != 99);
        System.exit(0);
    }

    public MenuCreator() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.opcion = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuCreator that = (MenuCreator) o;
        return opcion == that.opcion && Objects.equals(reader, that.reader) && Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reader, userName, opcion);
    }
}
