package ru.practicum.shareit.request;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByRequestorId(Long requestorId, Sort sort);

    List<ItemRequest> findByRequestorIdNot(Long requestorId, Sort sort);

    @Query(value = "select * from requests b " +
            "where requestor_id <> ?1 " +
            "order by created desc " +
            "limit ?2 offset ?3",
            nativeQuery = true)
    List<ItemRequest> findByRequestorIdNot(Long requestorId, Integer size, Integer from);
}
