package az.librarycrudapi.service.impl;

import az.librarycrudapi.dto.OrderRequestDto;
import az.librarycrudapi.dto.OrderResponseDto;
import az.librarycrudapi.entity.Book;
import az.librarycrudapi.entity.Member;
import az.librarycrudapi.entity.Order;
import az.librarycrudapi.entity.OrderItem;
import az.librarycrudapi.exception.ResourceNotFoundException;
import az.librarycrudapi.mapper.OrderMapper;
import az.librarycrudapi.repository.BookRepository;
import az.librarycrudapi.repository.MemberRepository;
import az.librarycrudapi.repository.OrderRepository;
import az.librarycrudapi.service.NotificationService;
import az.librarycrudapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final NotificationService notificationService;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto dto) {
        log.info("Yeni sifaris yaradilir. Uzv ID: {}", dto.getMemberId());

        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Uzv tapilmadi " + dto.getMemberId()));

        Order order = orderMapper.toEntity(dto);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setMember(member);
        order.setOrderItems(new java.util.HashSet<>());

        if (dto.getItems() != null) {
            dto.getItems().forEach(itemDto -> {
                Book book = bookRepository.findById(itemDto.getBookId())
                        .orElseThrow(() -> new ResourceNotFoundException("Kitab tapilmadi " + itemDto.getBookId()));

                OrderItem orderItem = new OrderItem();
                orderItem.setQuantity(itemDto.getQuantity());
                orderItem.setPrice(BigDecimal.valueOf(itemDto.getPrice()));
                orderItem.setBook(book);
                orderItem.setOrder(order);

                order.getOrderItems().add(orderItem);
            });
        }

        Order savedOrder = orderRepository.save(order);
        log.info("Sifaris ugurla bazaya yazildi. ID: {}", savedOrder.getId());

        notificationService.sendOrderNotification(member.getFullName(), savedOrder.getId());
        return orderMapper.toResponseDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        log.info("Butun sifarislerin siyahisi teleb olunur");
        return orderRepository.findAll(pageable).map(orderMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> searchOrders(String status, Long memberId, LocalDateTime date, Pageable pageable) {
        log.info("Dinamik sifaris axtarisi ise dusdu");
        var spec = az.librarycrudapi.repository.OrderSpecification.hasStatus(status)
                .and(az.librarycrudapi.repository.OrderSpecification.hasMemberId(memberId))
                .and(az.librarycrudapi.repository.OrderSpecification.createdAfter(date));
        return orderRepository.findAll(spec, pageable).map(orderMapper::toResponseDto);
    }
}
