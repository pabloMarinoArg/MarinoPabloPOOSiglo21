package src.service;

import src.model.Audits;
import src.model.Invoice;
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
    // Se crean las variables de los servicios a utilizar
    // y los "placeholders" para input del usuario.
    private final BufferedReader reader;
    private String userName;
    private String password;
    private int opcion;
    private int repeatWithdraw;
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
        System.out.println();
        System.out.println("PARA PROFESORES: USUARIO: admin PASS: 123456");
        // Esta harcodeado en GeneralRepository
        // por el momento es -> user: admin pass: 123456
        userLogin();

        System.out.println("Ingrese una opcion a ejecutar");
        do {
            System.out.println();
            System.out.println("1  - Ingresar producto al deposito");
            System.out.println("2  - Ingresar producto en espera a estanterias");
            System.out.println("3  - Sacar producto de estanteria");
            System.out.println("4  - Modificar producto");
            System.out.println("5  - Ver estanterias");
            System.out.println("6  - Ver registros de auditoria");
            System.out.println("7  - Ver lobby recepcion");
            System.out.println("8  - Crear orden de retiro");
            System.out.println("9  - Ver ordenes de retiro creadas");
            System.out.println("99 - Salir");
            System.out.println();

            // Acá uso try catch para poder setear a CERO la variable opcion
            // Y no me rompa el programa, para que permita al usuario
            // ingresar correctamente el valor pedido en un nuevo intento.

            try {
                opcion = Integer.parseInt(reader.readLine().trim());
            } catch (Exception e) {
                System.out.println("Ingrese la opcion correcta");
                opcion = 0;
            }


            switch (opcion) {
                case 1:
                    // Agrego item al lobby, seria como ingresar un producto al deposito
                    // cuando viene de afuera.
                    addItemToLobby();
                    break;
                case 2:
                    // Se ingresa algun producto del "lobby" a la estanteria que indiquemos
                    takeItemToRacks();
                    break;
                case 3:
                    // Se saca algun producto de una estantería que le indiquemos
                    // debemos especificar id de estanteria, de producto y la cantidad a retirar
                    withdrawItemFromRack(Optional.empty());
                    break;
                case 4:
                    // Podemos editar algunos parametros de los productos ya ingresados.
                    editItem();
                    break;
                case 5:
                    // Muestra todas las estanterias con su informacion y sus
                    // productos almacenados, si tuviera.
                    System.out.println("Ver estanterias");
                    menuService.drawSectionList();
                    break;
                case 6:
                    // Nos muestra los registros de auditoria generados en la mayoria de las transacciones
                    drawAudits();
                    break;
                case 7:
                    // Nos muestra que productos tenemos en el lobby
                    menuService.drawLobbyItemsAvailable();
                    break;
                case 8:
                    // Nos permite crear una orden de retiro para un cliente
                    createInvoiceOrder();
                    break;
                case 9:
                    // Nos muestra todas las ordenes de retiro creadas
                    menuService.drawAllInvoices();
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
        // Se usa un while y un contador decremental
        // para poder controlar la cantidad de reintentos de login
        // Pasados los 3 reintentos apaga el sistema.
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
        // Se pide el nombre del producto
        System.out.println("Ingresar lote de producto a lobby");
        System.out.println("Recepcion de productos nuevos a la espera de llevarlos a una estanteria");
        System.out.println("Ingrese nombre del producto");
        String name = reader.readLine().trim();

        // Se pide la descripcion del producto
        System.out.println("Ingrese descripcion del producto");
        String description = reader.readLine().trim();

        // Se pide el codigo del producto
        System.out.println("Ingrese codigo de identificacion del producto");
        Long code = Long.valueOf(reader.readLine());

        // Se pide el stock del producto
        System.out.println("Ingrese stock del producto");
        int stock = Integer.parseInt(reader.readLine().trim());

        // Se ingresa el producto recien creado al deposito y lo envia a una lista que representa
        // al lobby. Luego nos muestra el producto recien ingresado.
        menuService.receiveProductIntoWarehouse(code, stock, name, description);
        menuService.drawLobbyItemsAvailable();

        // Se genera su correspondiente Audit.
        Audits audit = new Audits(userName, LocalDateTime.now(), StorableItemAction.PENDING_STORAGE, code, "");
        auditService.addAuditToList(audit);
        System.out.printf("Item %s ingresado al deposito correctamente%n", name);
    }

    private void takeItemToRacks() throws IOException {
        System.out.println("Asignar producto a estanteria");

        //Se traen los productos del lobby y se fija si hay items o no
        // si hay los muestra, sino te devuelve al menu.
        List<StorableItem> listItemsEnLobby = repository.getItemsListLobby();

        if (listItemsEnLobby.isEmpty()) {
            System.out.println("No hay productos en la recepcion");
            return;
        }

        menuService.drawItemsLobbyList(listItemsEnLobby);

        // Se pide el id del item a guardar
        System.out.println("Ingrese el codigo del item a guardar: ");
        var codeFromUser = Integer.parseInt(reader.readLine().trim());

        // Se chequea que el id del item ingresado corresponda a un item en el lobby
        Optional<StorableItem> item = itemService.getItemByCodeFromLobby(codeFromUser);
        if (item.isEmpty()) {
            System.out.println("El item no existe");
            return;
        }

        // Nos muestra las secciones donde podemos guardarlo
        // Estas secciones nos muestran informacion de sus estanterias disponibles
        // y los datos necesarios para continuar el flujo
        menuService.drawSectionList();

        // Se pide el id de la estanteria donde se guardara
        System.out.println("Ingrese el id de la estanteria donde lo guardara: ");
        var idRack = Long.parseLong(reader.readLine().trim());

        // Se busca la estanteria y se chequea si existe o no.
        Optional<StorageStructure> storageStructure = menuService.getStorageStructure(idRack);

        if (storageStructure.isEmpty()) {
            System.out.println("La estanteria no existe");
            return;
        }

        // Se guarda el item del lobby en la estanteria elegida
        storageStructureService.addStorableItemToRack(item.get(), idRack, storageStructure.get());

        // Se genera el audit correspondiente
        Audits audit = new Audits(userName, LocalDateTime.now(), StorableItemAction.STORED, item.get().getCode(), "");
        auditService.addAuditToList(audit);
    }

    private Optional<StorableItem> withdrawItemFromRack(Optional<Invoice> invoice) throws IOException {
        System.out.println("Sacar producto de estanteria");
        // Se muestran todas las estanterias
        storageStructureService.listAllRacks();

        // Se muestran las secciones y los items del lobby
        menuService.drawSectionList();
        menuService.drawLobbyItemsAvailable();

        // Se pide al usuario el codigo del item que se quiere retirar
        System.out.println("Ingrese el codigo del item a sacar: ");
        var itemCode = Long.parseLong(reader.readLine().trim());

        // Se pide la cantidad a sacar
        System.out.println("Ingrese la cantidad a sacar: ");
        var amountToWithdraw = Integer.parseInt(reader.readLine().trim());

        // Se pide que ingrese el id de la estanteria donde se ubica el producto
        System.out.println("Ingrese el id de la estanteria que contiene al producto: ");
        var rackId = Long.parseLong(reader.readLine().trim());

        // Se obtiene y se chequea si existe la estanteria
        Optional<StorageStructure> storageStructure = menuService.getStorageStructure(rackId);

        if (storageStructure.isEmpty()) {
            System.out.println("La estanteria no existe");
            return Optional.empty();
        }

        // Se procede a retirar el producto y restar el stock de la estanteria
        // EL producto retirado se va al lobby
        Optional<StorableItem> item = storageStructureService.withDrawItemFromRack(itemCode, storageStructure.get(), amountToWithdraw);
        if (item.isEmpty()) {
            return Optional.empty();
        }

        // Si al metodo se le pasó una invoice entonces busca el ultimo registro de Withdraw realizado en la linea
        // con código anterior y lo suma al detalle de la invoice
        if(invoice.isPresent()) {
            var listWithdraw = menuService.lobbyItemsAvailableByItemAction(StorableItemAction.WITHDRAW);
            var product = listWithdraw.get(listWithdraw.size()-1);
            invoice.get().getDetail().add(product.toString() + "\n");
        }

        // Se genera el audit correspondiente
        Audits audit = new Audits(userName, LocalDateTime.now(), StorableItemAction.WITHDRAW, itemCode, "");
        auditService.addAuditToList(audit);

        System.out.println("Producto correctamente sacado de la estanteria");
        return item;
    }

    private void drawAudits() {
        System.out.println("Auditorias");
        menuService.drawAudits();
    }

    private void editItem() throws IOException {
        System.out.println("Editar producto de estanteria");

        // Se muestran todas las estanterias
        storageStructureService.listAllRacks();

        // Se pide el id del producto a editar
        System.out.println("Ingrese el codigo del item a sacar: ");
        var ic = Long.parseLong(reader.readLine().trim());

        // Se pide el id de la estanteria donde se ubica el producto a editar
        System.out.println("Ingrese el id de la estanteria que contiene al producto: ");
        var ri = Long.parseLong(reader.readLine().trim());

        // Se obtiene el item, y se verifica que exista
        Optional<StorableItem> itemEditable = getItem(ri, ic);
        if(itemEditable.isEmpty()) {
            System.out.println("No existe el item");
            return;
        }

        // Se genera un sub-menu para editar
        // Solo se permite editar nombre, descripcion y stock
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

        // Se genera el audit correspondiente
        Audits audit = new Audits(userName, LocalDateTime.now(), StorableItemAction.MODIFY, itemEditable.get().getCode(), "");
        auditService.addAuditToList(audit);
    }

    private void createInvoiceOrder() throws IOException {
        System.out.println("Crear orden de envio/retiro");

        // Se chequea que existan productos en el deposito, si no se corta el proceso y se devuelve al menu ppal
        if (!menuService.isStorableItemExists()) {
            System.out.println("No hay ningun item ingresado al deposito que pueda ser retirado");
            return;
        }

        // Se muestran todos los clientes cargados
        System.out.println("Clientes");
        menuService.drawClientsList();

        // Se pide el dni del cliente al cual le vamos hacer la orden
        System.out.println("Ingrese el DNI del cliente para abrir orden");
        var dni = Long.parseLong(reader.readLine().trim());

        // Se obtiene al cliente y se verifica si existe con ese dni ingresado
        var client = menuService.getAllClients().stream()
                .filter(clientUser -> clientUser.getIdNumber()==dni)
                .findFirst();
        if (client.isEmpty()) {
            System.out.println("Revise el dni ingresado. No corresponde a un cliente registrado.");
            return;
        }

        // Se genera una INVOICE y se inicializa con algunos parametros
        var fullName = client.get().getFirstName() +" " + client.get().getLastName();
        var sendAddress = client.get().getDeliveryAddress();
        Invoice inv = new Invoice();
        inv.setClientFullName(fullName);
        inv.setSendAddress(sendAddress);

        // Se llama al metodo que saca productos de las estanterias con la invoice recien creada
        Optional<StorableItem> item = withdrawItemFromRack(Optional.of(inv));
        if(item.isEmpty()) {
            return;
        }
        // Se hace loop para permitir cargar cuantos productos sean necesarios.
        do {
            System.out.println("Agregar otro producto a retirar por cliente?");
            System.out.println("1 - Si");
            System.out.println("2 - No");

            repeatWithdraw = Integer.parseInt(reader.readLine().trim());

            switch (repeatWithdraw) {
                case 1:
                    withdrawItemFromRack(Optional.of(inv));
                    break;
                case 2:
                    break;
                default:
                    System.out.println("No existe esa opcion");
                    break;
            }
        } while (repeatWithdraw != 2);

        menuService.addInvoiceToInvoiceList(inv);
        System.out.printf("Se completo con exito la creacion de la orden %s%n", inv.getId());

        System.out.println(inv);

        // Se generan audits correspondientes
        Audits audit = new Audits(userName, LocalDateTime.now(), StorableItemAction.INVOICE, 0L, "inv id: "+inv.getId());
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
        // Se instancian las variables del MenuCreator
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
