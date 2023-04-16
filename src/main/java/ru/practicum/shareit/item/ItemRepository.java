package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(" select i from Item i " +
            "where (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))) and i.available = true")
    List<Item> search(String text);

    @Query(value = "select * from items i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            "or upper(i.description) like upper(concat('%', ?1, '%')) " +
            "order by id " +
            "limit ?2 offset ?3",
            nativeQuery = true)
    List<Item> search(String text, Integer size, Integer from);

    @Query(" select i from Item i where i.owner = ?1")
    List<Item> findItemsByUser(long userId);

    @Query(value = "select * from items i " +
            "where owner = ?1 " +
            "order by id " +
            "limit ?2 offset ?3",
            nativeQuery = true)
    List<Item> findItemsByUser(long userId, Integer size, Integer from);

    List<Item> findByRequestId(long requestId);
}
