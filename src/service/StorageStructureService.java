package src.service;

import src.model.Section;
import src.model.StorableItem;
import src.model.StorageStructure;
import src.repository.GeneralRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class StorageStructureService {

    private GeneralRepository repository;

    public StorageStructureService(GeneralRepository repository) {
        this.repository = repository;
    }

    public StorageStructureService() {
        GeneralRepository.getInstance();
    }

    private void listAllRacks() {
      List<Section> sections = this.repository.getWarehouse().getSectionList();
      sections.forEach(section -> System.out.println(section.getSectionString()));
    }

    public void getAll() {
        listAllRacks();
    }

    public boolean addStorableItemToRack(StorableItem item, Long rackId, List<StorageStructure> racks) {
        if(item != null && !racks.isEmpty()) {
            for (StorageStructure rack : racks) {
                if(rackId != null && rackId == rack.getId()) {
                    rack.setStorableItemIntoRack(item);
                    System.out.println("Se ha agregado correctamente el producto");
                    System.out.println(item.getName());
                    System.out.println("al rack "+ rack.getId());
                    item.setRackId(rackId);
                    item.setStatus("Asignado");
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
       return repository.getWarehouse().getSectionList().stream()
                .flatMap(section -> section.getRackList().stream())
                .filter(estanteria -> Objects.equals(estanteria.getId(), id))
                .findFirst();
    }

    private static boolean isItemIdAndRacksNotNull(Long itemId, List<StorageStructure> racks) {
        return itemId != null && !racks.isEmpty();
    }

}
