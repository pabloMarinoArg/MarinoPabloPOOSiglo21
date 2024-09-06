package src.model;

import java.util.Objects;

public class StorableItem {
    private Long code;
    private String name;
    private Long rackId;
    private String description;
    private int stock;

    public StorableItem(Long code, String name, Long rackId, String description, int stock) {
        this.code = code;
        this.name = name;
        this.rackId = rackId;
        this.description = description;
        this.stock = stock;
    }

    public StorableItem(Long code, String name, String description, int stock) {
        this.code = code;
        this.name = name;
        this.rackId = null;
        this.description = description;
        this.stock = stock;
    }

    public StorableItem() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorableItem that = (StorableItem) o;
        return stock == that.stock && Objects.equals(code, that.code) && Objects.equals(name, that.name) && Objects.equals(rackId, that.rackId) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, rackId, description, stock);
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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

    // TODO agregar metodo para setear un campo reservado y la cantidad reservada no puede ser mayor al stock.


    @Override
    public String toString() {
        return "StorableItem{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", rackId=" + rackId +
                ", description='" + description + '\'' +
                ", stock=" + stock +
                '}';
    }
}
