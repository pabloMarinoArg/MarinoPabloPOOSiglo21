package src.service;

import src.model.StorableItem;
import src.model.StorageStructure;
import src.repository.GeneralRepository;
import src.utils.StorableItemAction;

public class LobbyService {

    private final GeneralRepository repository;
    private StorageStructureService storageStructureService;

    public LobbyService() {
        this.repository = GeneralRepository.getInstance();
        this.storageStructureService = new StorageStructureService();
    }

    public void addItemFromRackToLobby(StorableItem item) {
        item.setAction(StorableItemAction.WITHDRAW);
        repository.addItemToLobbyList(item);
    }

    public void receiveItemToLobby(StorableItem item) {
        item.setAction(StorableItemAction.PENDING_STORAGE);
        repository.addItemToLobbyList(item);
    }

    public void addItemFromLobyToRack(StorableItem item, StorageStructure rack) {
        item.setAction(StorableItemAction.STORED);
        rack.getItemsList().add(item);
    }



}
