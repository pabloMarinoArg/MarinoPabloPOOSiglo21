package src.model;

import java.util.List;
import java.util.Objects;

public class Warehouse {
    private List<Section> sectionList;
    private Long id;

    public Warehouse(List<Section> sectionList, Long id) {
        this.sectionList = sectionList;
        this.id = id;
    }

    public Warehouse() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return Objects.equals(sectionList, warehouse.sectionList) && Objects.equals(id, warehouse.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectionList, id);
    }

    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "sectionList=" + sectionList +
                ", id=" + id +
                '}';
    }
}
