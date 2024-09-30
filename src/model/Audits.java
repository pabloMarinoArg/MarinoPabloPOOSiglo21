package src.model;

import src.utils.StorableItemAction;

import java.time.LocalDateTime;

public record Audits (String user, LocalDateTime createdDate, StorableItemAction action, Long itemCode, String itemName) {
}
