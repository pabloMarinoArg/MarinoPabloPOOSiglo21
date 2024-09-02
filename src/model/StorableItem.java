package src.model;

import java.util.Objects;

public class StorableItem {
    private Long code;
    private String name;
    private Long rackId;
    private String description;

    public StorableItem(Long code, String name, Long rackId, String description) {
        this.code = code;
        this.name = name;
        this.rackId = rackId;
        this.description = description;
    }

    public StorableItem() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorableItem that = (StorableItem) o;
        return Objects.equals(code, that.code) && Objects.equals(name, that.name) && Objects.equals(rackId, that.rackId) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, rackId, description);
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRackId() {
        return rackId;
    }

    public void setRackId(Long rackId) {
        this.rackId = rackId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
