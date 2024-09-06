package src.repository;

import src.model.Section;
import src.model.StorableItem;
import src.model.StorageStructure;
import src.model.Warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GeneralRepository {
    private static GeneralRepository instance;
    private Warehouse warehouse;
    private List<StorableItem> itemsListLobby;

    public GeneralRepository() {

        StorageStructure rack1 = new StorageStructure(1L, new ArrayList<>(), "Cosas generales 1");
        StorageStructure rack2 = new StorageStructure(2L, new ArrayList<>(), "Cosas generales 2");
        StorageStructure rack3 = new StorageStructure(3L, new ArrayList<>(), "Cosas generales 3");
        StorageStructure rack4 = new StorageStructure(4L, new ArrayList<>(), "Cosas generales 4");
        Section section1 = new Section(List.of(rack1,rack2), 22233L);
        Section section2 = new Section(List.of(rack3), 22433L);
        Section section3 = new Section(List.of(rack4), 22533L);
        List<Section> sectionList = new ArrayList<>();
        sectionList.add(section1);
        sectionList.add(section2);
        sectionList.add(section3);
        List<StorableItem> listStorableItem = new ArrayList<>();
        this.itemsListLobby = listStorableItem;
        this.warehouse = new Warehouse(sectionList, 123L);
    }

    public static GeneralRepository getInstance() {
        if(instance == null) {
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
}
