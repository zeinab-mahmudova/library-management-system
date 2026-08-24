package az.librarycrudapi.mapper;

import az.librarycrudapi.dto.OrderItemResponseDto;
import az.librarycrudapi.dto.OrderRequestDto;
import az.librarycrudapi.dto.OrderResponseDto;
import az.librarycrudapi.entity.Order;
import az.librarycrudapi.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    Order toEntity(OrderRequestDto dto);

    @Mapping(target = "memberId", source = "member.id")
    @Mapping(target = "memberName", source = "member.fullName")
    @Mapping(target = "items", source = "orderItems")
    OrderResponseDto toResponseDto(Order order);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    @Mapping(target = "price", source = "price")
    OrderItemResponseDto toItemResponseDto(OrderItem item);

    List<OrderItemResponseDto> toItemResponseDtoList(List<OrderItem> items);
}
