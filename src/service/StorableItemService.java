package src.service;

import src.model.StorableItem;
import src.repository.GeneralRepository;
import src.utils.StorableItemAction;
import src.utils.Validator;

import java.util.Optional;

public class StorableItemService {

    public static final String EL_PRODUCTO_TIENE_DATOS_INCOMPLETOS = "El producto tiene datos incompletos: ";
    public static final String SE_CREO_EXITOSAMENTE_EL_PRODUCTO = "Se creo exitosamente el producto: ";
    public static final String FAILED_ITEM_TO_LOBBY = "No se pudo ingresar el producto al deposito";

    private GeneralRepository repository;
    private LobbyService lobbyService;


    public StorableItemService() {
        this.repository = GeneralRepository.getInstance();
        this.lobbyService = new LobbyService();
    }

    public void createItemAndSendItToLobby(Long code, int stock, String name, String description) {
           Optional<StorableItem> item = createItem(code, stock, name, description);
           item.ifPresentOrElse(lobbyService::receiveItemToLobby, ()-> System.out.println(FAILED_ITEM_TO_LOBBY));
    }

    public Optional<StorableItem> createItem(Long code, int stock, String name, String description) {
        try {
            StorableItem item = createNewItem(code, stock, name, description);
            Validator.validate(item);
            System.out.println(SE_CREO_EXITOSAMENTE_EL_PRODUCTO + item);
            return Optional.of(item);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            System.out.println(EL_PRODUCTO_TIENE_DATOS_INCOMPLETOS + e.getMessage());
        }
        return Optional.empty();
    }

    private static StorableItem createNewItem(Long code, int stock, String name, String description) {
        return new StorableItem(code, name, description, stock);
    }

    public Optional<StorableItem> getItemByCode(int code) {
        return repository.getItemsListLobby().stream()
                .filter(storableItem -> storableItem.getCode() == code)
                .findFirst();
    }

    private void addItemsToLobby(StorableItem item) {
        System.out.println("Ingreso lote de productos:");
        repository.addItemToLobbyList(item);

    }

    public boolean quitarStock(int stockAQuitar, int code) {
        Optional<StorableItem> item = getItemByCode(code);
        if (item.isEmpty()) {
            return false;
        }
        if (stockAQuitar == item.get().getStock()) {
            repository.deletItemFromLobbyList(item.get());
            return true;
        }
        repository.deletItemFromLobbyList(item.get());
        var currentStock = item.get().getStock();
        item.get().setStock(currentStock - stockAQuitar);
        repository.addItemToLobbyList(item.get());

        return true;
    }
}
