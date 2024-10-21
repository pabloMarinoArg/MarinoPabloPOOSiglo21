package src.model;

import src.utils.StorableItemAction;

import java.time.LocalDateTime;

public record Audits (String user, LocalDateTime createdDate, StorableItemAction action, Long itemCode, String obs) {
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
        if(!this.obs.isEmpty()) {
            sb.append("\n").append("Observacion: ").append(this.obs);
        }
        return sb.toString();
    }
}
