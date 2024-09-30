package src.service;

import src.model.StorableItem;
import src.model.StorageStructure;
import src.repository.GeneralRepository;
import src.utils.StorableItemAction;

public class LobbyService {

    private final GeneralRepository repository;


    public LobbyService() {
        this.repository = GeneralRepository.getInstance();
    }

    public void addItemFromRackToLobby(StorableItem item) {
        item.setAction(StorableItemAction.WITHDRAW);
        repository.addItemToLobbyList(item);
    }

    public void receiveItemToLobby(StorableItem item) {
        item.setAction(StorableItemAction.PENDING_STORAGE);
        repository.addItemToLobbyList(item);
    }

    public void removeItemFromLobyToRack(StorableItem item) {
       repository.getItemsListLobby().remove(item);
    }



}
