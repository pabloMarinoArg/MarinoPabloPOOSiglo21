package src.service;

import src.model.Section;
import src.model.StorableItem;
import src.repository.GeneralRepository;

import java.util.List;

public class MenuCreatorService {

    GeneralRepository repository;

    StorableItemService itemService;

    StorageStructureService storageStructureService;

    public MenuCreatorService() {
        this.repository = GeneralRepository.getInstance();
        this.itemService = new StorableItemService();
        this.storageStructureService = new StorageStructureService();
    }

    public void drawSectionList() {
        List<Section> sections = repository.getWarehouse().getSectionList();
        sections.forEach(System.out::println);
    }

    public void drawLobbyItemsAvailable() {
        List<StorableItem> itemsListLobby = repository.getItemsListLobby();
        System.out.println();
        System.out.println("Productos en espera de ser guardados");

        if (itemsListLobby.isEmpty()) {
            System.out.println("No hay productos en espera");
        }else {
            itemsListLobby.forEach(System.out::println);
        }
    }







}
