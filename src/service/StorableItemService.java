package src.service;

import src.model.StorableItem;
import src.repository.GeneralRepository;

public class StorableItemService {
    private GeneralRepository repository;

    public StorableItemService(GeneralRepository repository) {
        this.repository = repository;
    }

    public StorableItemService() {
        this.repository = GeneralRepository.getInstance();
    }

    public String createItem(Long code, int stock , String name, String description) {
      StorableItem item = new StorableItem(code, name, description, stock);
      addItemsToLobby(item);
        return "Se agrego exitosamente el lote de items al lobby: "+item;
    }

    private void addItemsToLobby(StorableItem item) {
        System.out.println("Ingreso lote de productos:");
        repository.addItemToLobbyList(item);

    }
}
