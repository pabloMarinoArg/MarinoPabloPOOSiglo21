package src.model;

import java.util.List;
import java.util.Objects;

public class Section {
    private List<StorageStructure> rackList;
    private Long id;

    public Section(List<StorageStructure> rackList, Long id) {
        this.rackList = rackList;
        this.id = id;
    }

    public Section() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(rackList, section.rackList) && Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rackList, id);
    }

    public List<StorageStructure> getRackList() {
        return rackList;
    }

    public void setRackList(List<StorageStructure> rackList) {
        this.rackList = rackList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public StorageStructure getRackUnit(Long id) {
        if (this.rackList.isEmpty()) {
            return null;
        }

        for (StorageStructure rack : this.rackList) {
            if (rack.getId() == id) {
                return rack;
            }
        }
        return null;
    }
}
