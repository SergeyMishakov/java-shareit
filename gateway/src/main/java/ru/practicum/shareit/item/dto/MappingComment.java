package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;

public class MappingComment {

    public static CommentDto mapToCommentDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setCreated(comment.getCreated());
        return dto;
    }
}
