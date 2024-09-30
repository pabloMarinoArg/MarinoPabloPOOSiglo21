package src.repository;

import src.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class GeneralRepository {
    private static GeneralRepository instance;
    private Warehouse warehouse;
    private List<StorableItem> itemsListLobby;
    private final ConcurrentHashMap<String, String> user;
    private List<Audits> auditEvents;
    private List<StorableItem> allItems;

    public GeneralRepository() {
        User admin = new User("admin", "123456");
        StorageStructure rack1 = new StorageStructure(1L, new ArrayList<>(), "Cables");
        StorageStructure rack2 = new StorageStructure(2L, new ArrayList<>(), "Electronico");
        StorageStructure rack3 = new StorageStructure(3L, new ArrayList<>(), "Plasticos");
        StorageStructure rack4 = new StorageStructure(4L, new ArrayList<>(), "Vidrio");
        Section section1 = new Section(List.of(rack1, rack2), 113L);
        Section section2 = new Section(List.of(rack3), 234L);
        Section section3 = new Section(List.of(rack4), 453L);
        List<Section> sectionList = new ArrayList<>();
        sectionList.add(section1);
        sectionList.add(section2);
        sectionList.add(section3);
        List<StorableItem> listStorableItem = new ArrayList<>();
        ConcurrentHashMap<String, String> credentials = new ConcurrentHashMap<>();
        credentials.put(admin.getUserName(), admin.getPassword());
        this.itemsListLobby = listStorableItem;
        this.warehouse = new Warehouse(sectionList, 123L);
        this.user = credentials;
        this.auditEvents = new ArrayList<>();
        this.allItems = new ArrayList<>();
    }

    public static GeneralRepository getInstance() {
        if (instance == null) {
            instance = new GeneralRepository();
        }
        return instance;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public List<StorableItem> getItemsListLobby() {
        return itemsListLobby;
    }

    public void addItemToLobbyList(StorableItem item) {
        this.itemsListLobby.add(item);
    }

    public void deletItemFromLobbyList(StorableItem item) {
        this.itemsListLobby.remove(item);
    }

    public ConcurrentHashMap<String, String> getUser() {
        return user;
    }

    public List<Audits> getAuditEvents() {
        return auditEvents;
    }

    public List<StorableItem> getAllItems() {
        return allItems;
    }
}
