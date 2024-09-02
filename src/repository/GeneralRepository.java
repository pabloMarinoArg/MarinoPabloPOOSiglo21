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
    private List<StorageStructure> storageStructureRepository;
    private Warehouse warehouse;

    public GeneralRepository() {

        StorageStructure rack1 = new StorageStructure(1L, new ArrayList<>(), "Cosas generales 1");
        StorageStructure rack2 = new StorageStructure(2L, new ArrayList<>(), "Cosas generales 2");
        StorageStructure rack3 = new StorageStructure(3L, new ArrayList<>(), "Cosas generales 3");
        StorageStructure rack4 = new StorageStructure(4L, new ArrayList<>(), "Cosas generales 4");
        Section section1 = new Section(List.of(rack1,rack2), 2233L);
        Section section2 = new Section(List.of(rack3), 2233L);
        Section section3 = new Section(List.of(rack4), 2233L);
        List<Section> sectionList = new ArrayList<>();
        sectionList.add(section1);
        sectionList.add(section2);
        sectionList.add(section3);

        this.warehouse = new Warehouse(sectionList, 123L);
    }

    public boolean addStorableItemToRack(StorableItem item, Long rackId, List<StorageStructure> racks) {
        if(item != null && !racks.isEmpty()) {
              for (StorageStructure rack : racks) {
                if(rackId != null && rackId == rack.getId()) {
                    rack.setStorableItemIntoRack(item);
                    System.out.println("Se ha agregado correctamente el producto");
                    System.out.println(item.getName());
                    System.out.println("al rack "+ rack.getId());
                    return true;
                }
                  System.out.println("No existe el rack donde se quiere almacenar el producto o está vacío");
                  return false;
              }

        }
        return false;
    }

    public boolean deleteItemFromRack(Long itemId, Long rackId, List<StorageStructure> racks) {
        if (isItemIdAndRacksNotNull(itemId, racks)) {
            Optional<StorageStructure> structure = findStorageStructureById(rackId);
            if (structure.isEmpty()){
                System.out.println("No existe el rack o está vacío");
                return false;
            }
        }
        System.out.println("Item , , "+ itemId +" borrado con éxito");
        return true;
    }

    public Optional<StorageStructure> findStorageStructureById(Long id) {
        return Optional.empty();
    }

    private static boolean isItemIdAndRacksNotNull(Long itemId, List<StorageStructure> racks) {
        return itemId != null && !racks.isEmpty();
    }

    public static GeneralRepository getInstance() {
        if(instance != null) {
            instance = new GeneralRepository();
        }
        return instance;
    }
    public List<StorageStructure> getStorageStructureRepository() {
        return storageStructureRepository;
    }

    public void setStorageStructureRepository(List<StorageStructure> storageStructureRepository) {
        this.storageStructureRepository = storageStructureRepository;
    }
}
