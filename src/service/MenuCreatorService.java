package src.service;

import src.model.*;
import src.repository.GeneralRepository;
import src.utils.StorableItemAction;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MenuCreatorService {

    GeneralRepository repository;

    StorableItemService itemService;

    StorageStructureService storageStructureService;

    SectionService sectionService;

    public MenuCreatorService() {
        this.repository = GeneralRepository.getInstance();
        this.itemService = new StorableItemService();
        this.storageStructureService = new StorageStructureService();
        this.sectionService = new SectionService();
    }

    public void drawSectionList() {
        List<Section> sections = repository.getWarehouse().getSectionList();
        sections.forEach(System.out::println);
    }

    public void drawLobbyItemsAvailable() {
        List<StorableItem> itemsListLobby = repository.getItemsListLobby();
        System.out.println();
        System.out.println("Productos lobby");

        if (itemsListLobby.isEmpty()) {
            System.out.println("No hay productos en lobby");
        } else {
            itemsListLobby.forEach(System.out::println);
        }
    }

    public List<StorableItem> lobbyItemsAvailableByItemAction(StorableItemAction action) {
        List<StorableItem> itemsListLobby = repository.getItemsListLobby();

        return itemsListLobby.stream()
                .filter(item -> item.getAction().equals(action))
                .collect(Collectors.toList());
    }

    public void drawItemsLobbyList(List<StorableItem> itemsListLobby) {
        System.out.println();
        System.out.println("Productos en espera de ser guardados");

        itemsListLobby.forEach(System.out::println);
    }

    public void receiveProductIntoWarehouse(Long code, int stock, String name, String description) {
        itemService.createItemAndSendItToLobby(code, stock, name, description);
    }

    public Optional<StorageStructure> getStorageStructure(Long rackId) {
        List<Section> sectionList = repository.getWarehouse().getSectionList();

        OptionalInt index = IntStream.range(0, sectionList.size())
                .filter(s -> sectionList.get(s).getRackList().stream()
                        .anyMatch(storageStructure -> storageStructure.getId().equals(rackId)))

                .findFirst();
        if (index.isEmpty()) {
            return Optional.empty();
        }

        Section section = repository.getWarehouse().getSectionList().get(index.getAsInt());

        return sectionService.getStorageStructureById(section, rackId);
    }

    public void drawAudits() {
        repository.getAuditEvents().forEach(System.out::println);
    }

    public void drawClientsList() {
       var clients = getAllClients();
       clients.forEach(System.out::println);
    }

    public List<ClientUser> getAllClients() {return repository.getClientsList();}

    public void addInvoiceToInvoiceList(Invoice invoice) {
        repository.getInvoiceList().add(invoice);
    }

    public void drawAllInvoices() {
        List<Invoice> invoiceList = repository.getInvoiceList();
        if(invoiceList.isEmpty()) {
            System.out.println("No hay ordenes de retiro creadas");
            return;
        }
        repository.getInvoiceList().forEach(System.out::println);
    }

    public boolean isStorableItemExists() {
        boolean itemsStoredAvailable = repository.getWarehouse().getSectionList().stream()
                .flatMap(section -> section.getRackList().stream())
                .flatMap(racks -> racks.getItemsList().stream())
                .anyMatch(item -> true);
        return !repository.getItemsListLobby().isEmpty()
                || itemsStoredAvailable;
    }
}
