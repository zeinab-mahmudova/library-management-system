package az.librarycrudapi.service;

import az.librarycrudapi.dto.OrderRequestDto;
import az.librarycrudapi.dto.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto dto);
    Page<OrderResponseDto> getAllOrders(Pageable pageable);
    Page<OrderResponseDto> searchOrders(String status, Long memberId, LocalDateTime date, Pageable pageable);
}
