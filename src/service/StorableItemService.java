package src.service;

import src.model.StorableItem;
import src.repository.GeneralRepository;

import java.util.Optional;

public class StorableItemService {
    private GeneralRepository repository;

    public StorableItemService(GeneralRepository repository) {
        this.repository = repository;
    }

    public StorableItemService() {
        this.repository = GeneralRepository.getInstance();
    }

    public String createItem(Long code, int stock , String name, String description, String status) {
      StorableItem item = new StorableItem(code, name, description, stock, status);
      addItemsToLobby(item);
        return "Se agrego exitosamente el lote de items al lobby: "+item;
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
