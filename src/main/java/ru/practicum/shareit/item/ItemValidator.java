package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

@Service
public class ItemValidator {
    public boolean validate(Item item) {
        return nameValidate(item.getName()) && descriptionValidate(item.getDescription()) &&
                availableValidate(item.getAvailable());
    }

    public boolean nameValidate(String name) {
        return name != null && !name.isBlank();
    }
    public boolean descriptionValidate(String descr) {
        return descr != null && !descr.isBlank();
    }
    public boolean availableValidate(Boolean available) {
        return available != null;
    }
}
