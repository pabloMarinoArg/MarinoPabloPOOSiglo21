package src.model;

import src.utils.StorableItemAction;

import java.time.LocalDateTime;

public record Audits (String user, LocalDateTime createdDate, StorableItemAction action, Long itemCode) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n")
                .append("-------------")
                .append("\n")
                .append("Fecha y hora: ").append(this.createdDate)
                .append("\n")
                .append("Audit: ").append(this.user).append(" - Item Codigo: ").append(this.itemCode)
                .append("\n")
                .append("Accion: ").append(this.action);
        return sb.toString();
    }
}
