package src.service;

import src.model.Audits;
import src.model.StorableItem;
import src.model.StorageStructure;
import src.repository.GeneralRepository;
import src.utils.StorableItemAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MenuCreator {
    private final BufferedReader reader;
    private String userName;
    private String password;
    private int opcion;
    private final GeneralRepository repository;
    private final StorableItemService itemService;
    private final StorageStructureService storageStructureService;
    private final MenuCreatorService menuService;
    private AuditService auditService;

    public void getMenu() throws IOException {

        System.out.println("**********************");
        System.out.println("**********************");
        System.out.println("****Inventory Sys*****");
        System.out.println("**********V1**********");
        System.out.println("**********************");

        userLogin();

        System.out.println("Ingrese una opción a ejecutar");
        do {
            System.out.println();
            System.out.println("1  - Ingresar producto al deposito");
            System.out.println("2  - Ingresar producto en espera a estanterias");
            System.out.println("3  - Sacar producto de estanteria");
            System.out.println("4  - Modificar producto");
            System.out.println("5  - Ver estanterias");
            System.out.println("6  - Ver registros de auditoria");
            System.out.println("7  - Ver lobby recepcion");
            System.out.println("99 - Salir");
            System.out.println();

            try {
                opcion = Integer.parseInt(reader.readLine().trim());
            } catch (Exception e) {
                System.out.println("Ingrese la opcion correcta");
                opcion = 0;
            }


            switch (opcion) {
                case 1:
                    addItemToLobby();
                    break;
                case 2:
                    takeItemToRacks();
                    break;
                case 3:
                    withdrawItemFromRack();
                    menuService.drawSectionList();
                    menuService.drawLobbyItemsAvailable();
                    break;
                case 4:
                    editItem();
                    break;
                case 5:
                    System.out.println("Ver estanterias");
                    menuService.drawSectionList();
                    menuService.drawLobbyItemsAvailable();
                    break;
                case 6:
                    drawAudits();
                    break;
                case 7:
                    menuService.drawLobbyItemsAvailable();
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

    private void userLogin() throws IOException {
        boolean flagNotPassed = true;
        int counter = 3;
        while (flagNotPassed) {
            System.out.println("Ingrese nombre de usuario: ");
            userName = reader.readLine().trim();

            System.out.println("Ingrese la contrasena: ");
            password = reader.readLine().trim();

            String passToCompare = repository.getUser().get(userName);

            if (passToCompare != null && !passToCompare.isBlank() && !passToCompare.isEmpty() && passToCompare.equals(password)) {
                flagNotPassed = false;
            } else {
                System.out.println("Usuario o contrasena incorrecta");
                System.out.println("tiene intentos limitados, de superarlos se cerrara el programa");
                counter--;
                System.out.println("Intentos restantes: " + counter);
                if (counter == 0) {
                    System.out.println("Se desconecta del sistema...");
                    System.exit(0);
                }
            }

        }
    }

    private void addItemToLobby() throws IOException {
        System.out.println("Ingresar lote de producto a lobby");
        System.out.println("Recepcion de productos nuevos a la espera de llevarlos a una estanteria");
        System.out.println("Ingrese nombre del producto");
        String name = reader.readLine().trim();
        System.out.println("Ingrese descripcion del producto");
        String description = reader.readLine().trim();
        System.out.println("Ingrese codigo de identificacion del producto");
        Long code = Long.valueOf(reader.readLine());
        System.out.println("Ingrese stock del producto");
        int stock = Integer.parseInt(reader.readLine().trim());
        menuService.receiveProductIntoWarehouse(code, stock, name, description);
        menuService.drawLobbyItemsAvailable();
        Audits audit = new Audits(userName, LocalDateTime.now(), StorableItemAction.PENDING_STORAGE, code);
        auditService.addAuditToList(audit);
    }

    private void takeItemToRacks() throws IOException {
        System.out.println("Asignar producto a estanteria");

        List<StorableItem> listItemsEnLobby = repository.getItemsListLobby();

        if (listItemsEnLobby.isEmpty()) {
            System.out.println("No hay productos en la recepcion");
            return;
        }

        menuService.drawItemsLobbyList(listItemsEnLobby);

        System.out.println("Ingrese el codigo del item a guardar: ");
        var codeFromUser = Integer.parseInt(reader.readLine().trim());

        Optional<StorableItem> item = itemService.getItemByCodeFromLobby(codeFromUser);
        if (item.isEmpty()) {
            System.out.println("El item no existe");
            return;
        }

        menuService.drawSectionList();

        System.out.println("Ingrese el id de la estanteria donde lo guardara: ");
        var idRack = Long.parseLong(reader.readLine().trim());

        Optional<StorageStructure> storageStructure = menuService.getStorageStructure(idRack);

        if (storageStructure.isEmpty()) {
            System.out.println("La estanteria no existe");
            return;
        }

        storageStructureService.addStorableItemToRack(item.get(), idRack, storageStructure.get());
        Audits audit = new Audits(userName, LocalDateTime.now(), StorableItemAction.STORED, item.get().getCode());
        auditService.addAuditToList(audit);
    }

    private void withdrawItemFromRack() throws IOException {
        System.out.println("Sacar producto de estanteria");
        storageStructureService.listAllRacks();

        System.out.println("Ingrese el codigo del item a sacar: ");
        var itemCode = Long.parseLong(reader.readLine().trim());

        System.out.println("Ingrese la cantidad a sacar: ");
        var amountToWithdraw = Integer.parseInt(reader.readLine().trim());

        System.out.println("Ingrese el id de la estanteria que contiene al producto: ");
        var rackId = Long.parseLong(reader.readLine().trim());

        Optional<StorageStructure> storageStructure = menuService.getStorageStructure(rackId);

        if (storageStructure.isEmpty()) {
            System.out.println("La estanteria no existe");
            return;
        }

        storageStructureService.withDrawItemFromRack(itemCode, storageStructure.get(), amountToWithdraw);

        Audits audit = new Audits(userName, LocalDateTime.now(), StorableItemAction.WITHDRAW, itemCode);
        auditService.addAuditToList(audit);
    }

    private void drawAudits() {
        System.out.println("Auditorias");
        menuService.drawAudits();
    }

    private void editItem() throws IOException {
        System.out.println("Editar producto de estanteria");
        storageStructureService.listAllRacks();

        System.out.println("Ingrese el codigo del item a sacar: ");
        var ic = Long.parseLong(reader.readLine().trim());

        System.out.println("Ingrese el id de la estanteria que contiene al producto: ");
        var ri = Long.parseLong(reader.readLine().trim());

        Optional<StorableItem> itemEditable = getItem(ri, ic);

        if(itemEditable.isEmpty()) {
            System.out.println("No existe el item");
            return;
        }

        System.out.println("Que quiere editar?");
        System.out.println("1 - Nombre");
        System.out.println("2 - Descripcion");
        System.out.println("3 - Stock");
        var editOption = Integer.parseInt(reader.readLine().trim());

        switch (editOption) {
            case 1:
                System.out.println("Ingrese nombre del producto");
                String editedName = reader.readLine().trim();
                itemEditable.get().setName(editedName);
                break;
            case 2:
                System.out.println("Ingrese la descripcion del producto");
                String descriptionEdited = reader.readLine().trim();
                itemEditable.get().setName(descriptionEdited);
                break;
            case 3:
                System.out.println("Ingrese el stock del producto");
                String stockEdited = reader.readLine().trim();
                itemEditable.get().setName(stockEdited);
                break;
        }
        Audits audit = new Audits(userName, LocalDateTime.now(), StorableItemAction.MODIFY, itemEditable.get().getCode());
        auditService.addAuditToList(audit);
    }

    private Optional<StorableItem> getItem(long ri, long ic) {
        Optional<StorageStructure> storageStructure = menuService.getStorageStructure(ri);
        if (storageStructure.isEmpty()) {
            System.out.println("La estanteria no existe");
            return Optional.empty();
        }
        var itemToEdit = storageStructureService.getItemIndexFromListByCode(ic, storageStructure.get().getItemsList());
        if(itemToEdit.isEmpty()) {
            System.out.println("La estanteria no existe");
            return Optional.empty();
        }

        return Optional.of(storageStructure.get().getItemsList().get(itemToEdit.get()));
    }

    public MenuCreator() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.opcion = 0;
        repository = GeneralRepository.getInstance();
        this.itemService = new StorableItemService();
        this.storageStructureService = new StorageStructureService();
        this.menuService = new MenuCreatorService();
        this.auditService = new AuditService();
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
