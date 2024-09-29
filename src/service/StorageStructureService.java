package src.service;

import src.errors.EmptyException;
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

    private GeneralRepository repository;
    private LobbyService lobbyService;

    public StorageStructureService() {
        GeneralRepository.getInstance();
    }

    public void listAllRacks() {
        List<Section> sections = this.repository.getWarehouse().getSectionList();
        sections.forEach(System.out::println);
    }

    public StorageStructure addStorableItemToRack(StorableItem item, Long rackId, StorageStructure rack) throws EmptyException {
        if (notNull(item, rackId, rack)) {
            var itemList = rack.getItemsList();
            Optional<Integer> index = getItemIndexFromListByCode(item, itemList);

            if (index.isEmpty()) {
                item.setAction(StorableItemAction.STORED);
                itemList.add(item);
            } else {
                var itemFromList = itemList.get(index.get());
                itemFromList.agregarItemStock(item.getStock());
                itemFromList.setAction(StorableItemAction.STORED);
                itemList.set(index.get(), itemFromList);
            }
            return rack;
        }

        throw new EmptyException(PRODUCTO_IDESTANTERIA_ESTANTERIA);
    }

    public StorageStructure withDrawItemFromRack(StorableItem item, Long rackId, StorageStructure rack, int amount) throws EmptyException {
        if(notNull(item, rackId, rack)) {
            var itemList = rack.getItemsList();
            Optional<Integer> index = getItemIndexFromListByCode(item, itemList);

            if(index.isEmpty()) {
                throw new EmptyException("No existe en la estanteria: " + rackId + " el producto" + item.getName());
            }

            var itemFromList = itemList.get(index.get());
            itemFromList.quitarItemStock(amount);

            if(itemFromList.getStock() == 0) {
              removeItemFromRack(index.get(), rack);
            }

            itemList.set(index.get(), itemFromList);

            lobbyService.addItemFromRackToLobby(item);

            return rack;
        }

        throw new EmptyException(PRODUCTO_IDESTANTERIA_ESTANTERIA);
    }

    public void removeItemFromRack(int index, StorageStructure rack) {
        List<StorableItem> itemList = rack.getItemsList();
        itemList.remove(index);
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

}
