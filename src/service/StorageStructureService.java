package src.service;

import src.model.Section;
import src.model.StorableItem;
import src.model.StorageStructure;
import src.repository.GeneralRepository;
import src.utils.StorableItemAction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class StorageStructureService {

    public static final String PRODUCTO_IDESTANTERIA_ESTANTERIA = "producto, id de estanteria o estanteria";

    private final GeneralRepository repository;
    private final LobbyService lobbyService;

    public StorageStructureService() {
       this.repository = GeneralRepository.getInstance();
       this.lobbyService = new LobbyService();
    }

    public void listAllRacks() {
        List<Section> sections = repository.getWarehouse().getSectionList();
        sections.forEach(System.out::println);
    }

    public void addStorableItemToRack(StorableItem item, Long rackId, StorageStructure rack) {
        if (notNull(item, rackId, rack)) {
            var itemList = rack.getItemsList();
            Optional<Integer> index = getItemIndexFromListByCode(item, itemList);

            if (index.isEmpty()) {
                lobbyService.removeItemFromLobyToRack(item);
                item.setAction(StorableItemAction.STORED);
                item.setRackId(rackId);
                itemList.add(item);

            } else {
                lobbyService.removeItemFromLobyToRack(item);
                var itemFromList = itemList.get(index.get());
                itemFromList.agregarItemStock(item.getStock());
                itemFromList.setRackId(rackId);
                itemFromList.setAction(StorableItemAction.STORED);
                itemList.set(index.get(), itemFromList);
            }

        }

        return;
    }

    public void withDrawItemFromRack(Long code, StorageStructure rack, int amount) {
        if(notNull(code, rack)) {
            var itemList = rack.getItemsList();
            Optional<Integer> index = getItemIndexFromListByCode(code, itemList);

            if(index.isEmpty()) {
                System.out.println("El producto a retirar, no existe");
                return;
            }

            StorableItem itemFromList = itemList.get(index.get());
            itemFromList.quitarItemStock(amount);

            if(itemFromList.getStock() == 0) {
              removeItemFromRack(index.get(), rack);
            }

            itemList.set(index.get(), itemFromList);

            StorableItem itemToLoby = new StorableItem(itemFromList);
            itemToLoby.setStock(amount);
            lobbyService.addItemFromRackToLobby(itemToLoby);
        }
    }

    public void removeItemFromRack(int index, StorageStructure rack) {
        List<StorableItem> itemList = rack.getItemsList();
        itemList.remove(index);
    }

    private static Optional<Integer> getItemIndexFromListByCode(Long code, List<StorableItem> itemList) {
        return itemList.stream()
                .filter(i -> Objects.equals(i.getCode(), code))
                .findFirst()
                .map(itemList::indexOf);
    }

    private static Optional<Integer> getItemIndexFromListByCode(StorableItem item, List<StorableItem> itemList) {
        return itemList.stream()
                .filter(i -> Objects.equals(i.getCode(), item.getCode()))
                .findFirst()
                .map(itemList::indexOf);
    }

    private static boolean notNull(StorableItem item, Long rackId, StorageStructure rack) {
        return Objects.nonNull(item) && Objects.nonNull(rackId) && Objects.nonNull(rack);
    }

    private static boolean notNull(Long id, StorageStructure rack) {
        return Objects.nonNull(id) && Objects.nonNull(rack);
    }

}
