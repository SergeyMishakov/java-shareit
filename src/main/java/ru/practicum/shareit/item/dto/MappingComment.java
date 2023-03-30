package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import java.util.ArrayList;
import java.util.List;

public class MappingComment {

    public static CommentDto mapToCommentDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setCreated(comment.getCreated());
        return dto;
    }

    public static List<CommentDto> mapToCommentDtoList(List<Comment> commentList) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentDtoList.add(mapToCommentDto(comment));
        }
        return commentDtoList;
    }
}
