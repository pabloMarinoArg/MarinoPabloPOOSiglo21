package src.model;

import java.util.List;
import java.util.Objects;

public class StorageStructure {
    private Long id;
    private List<StorableItem> itemsList;
    private String description;

    public StorageStructure(Long id, List<StorableItem> itemsList, String description) {
        this.id = id;
        this.itemsList = itemsList;
        this.description = description;
    }

    public StorageStructure() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorageStructure that = (StorageStructure) o;
        return Objects.equals(id, that.id) && Objects.equals(itemsList, that.itemsList) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemsList, description);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<StorableItem> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<StorableItem> itemsList) {
        this.itemsList = itemsList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStorableItemIntoRack(StorableItem s) {
        itemsList.add(s);
    }

    @Override
    public String toString() {
        return "StorageStructure{" +
                "id=" + id +
                ", itemsList=" + itemsList +
                ", description='" + description + '\'' +
                '}';
    }
}
