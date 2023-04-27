package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(Long userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> changeItem(Long userId, int itemId, ItemDto itemDto) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> findItemDtoById(Long userId, int id) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> findItemsByUser(Long userId, Integer from, Integer size) {
        if (from != null && size != null) {
            Map<String, Object> parameters = Map.of(
                    "from", from,
                    "size", size
            );
            return get("?from={from}&size={size}", userId, parameters);
        }
        return get("", userId);
    }

    public ResponseEntity<Object> searchItem(String text, Integer from, Integer size) {
        if (from != null && size != null) {
            Map<String, Object> parameters = Map.of(
                    "text", text,
                    "from", from,
                    "size", size
            );
            return get("/search?text={text}&from={from}&size={size}", null, parameters);
        }
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", null, parameters);
    }

    public ResponseEntity<Object> addComment(Long userId, long itemId, Comment comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }
}
