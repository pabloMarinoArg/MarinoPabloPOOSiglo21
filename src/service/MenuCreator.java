package src.service;

import src.model.Section;
import src.model.StorableItem;
import src.model.StorageStructure;
import src.repository.GeneralRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuCreator {
    private final BufferedReader reader;
    private String userName;
    private int opcion;
    private GeneralRepository repository;
    private StorableItemService itemService;
    private StorageStructureService storageStructureService;

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
                    addItemToLobbyProcess();
                    break;
                case 2:
                    addOrSubstractItem();
                    break;
                case 3:
                    System.out.println("Eliminar/Modificar producto");
                    break;
                case 4:
                    System.out.println("Buscar producto");
                    break;
                case 5:
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

    private void addOrSubstractItem() throws IOException {
        System.out.println("Asignar producto a estanteria");
        storageStructureService.getAll();
        repository.getItemsListLobby().forEach(storableItem -> {
            System.out.println(storableItem.getStringItemD());
        });

        System.out.println("Ingrese el codigo del item a guardar: ");
        var getCodeFromUser = Integer.parseInt(reader.readLine());
        var item = itemService.getItemByCode(getCodeFromUser);
        if (item.isEmpty()) {
            System.out.printf("No existe el item con codigo: %d", getCodeFromUser);
            return;
        }

        System.out.println("Ingrese el id de la estanteria donde lo guardara: ");
        var idRack = Long.parseLong(reader.readLine());
        var rack = storageStructureService.findStorageStructureById(idRack);
        if (rack.isEmpty()) {
            System.out.printf("No existe la estanteria con id: %d", idRack);
            return;
        }
        System.out.println("Que cantidad del stock disponible va guardar en la estanteria?");
        var stockGuardado = Integer.parseInt(reader.readLine());
        if (item.get().getStock() < stockGuardado) {
            System.out.printf("Revisar la cantidad a guardar, es mayor a la disponible");
            return;
        }
        if (!itemService.quitarStock(stockGuardado, getCodeFromUser)) {
            System.out.println("No se pudo descontar el item - Error");
            return;
        }
        var itemToStorageStructure = item.get();
        itemToStorageStructure.setStock(stockGuardado);
        List<Section> sectionList = repository.getWarehouse().getSectionList();
        storageStructureService.addStorableItemToRack(item.get(), idRack, sectionList.stream()
                .flatMap(section -> section.getRackList().stream())
                .collect(Collectors.toList()));
    }

    private void addItemToLobbyProcess() throws IOException {
        System.out.println("Ingresar lote de producto a lobby");
        System.out.println("Recepcion de productos nuevos a la espera de llevarlos a una estanteria");
        System.out.println("Ingrese nombre del producto");
        String name = reader.readLine();
        System.out.println("Ingrese descripcion del producto");
        String description = reader.readLine();
        System.out.println("Ingrese codigo de identificacion del producto");
        Long code = Long.valueOf(reader.readLine());
        System.out.println("Ingrese stock del producto");
        int stock = Integer.parseInt(reader.readLine());
        itemService.createItem(code, stock, name, description, "Nuevo No Asignado");
        List<StorableItem> itemsListLobby = repository.getItemsListLobby();
        itemsListLobby.forEach(storableItem -> System.out.println(storableItem.getStringItemD()));
    }

    public MenuCreator() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.opcion = 0;
        repository = GeneralRepository.getInstance();
        this.itemService = new StorableItemService(repository);
        this.storageStructureService = new StorageStructureService(repository);
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
