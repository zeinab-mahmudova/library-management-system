package az.librarycrudapi;

import az.librarycrudapi.repository.BookRepository;
import az.librarycrudapi.repository.MemberRepository;
import az.librarycrudapi.repository.OrderRepository;
import az.librarycrudapi.service.NotificationService;
import az.librarycrudapi.mapper.OrderMapper;
import az.librarycrudapi.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testGetAllOrders() {
        Pageable pageable = PageRequest.of(0, 10);
        when(orderRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.emptyList()));

        Page<?> result = orderService.getAllOrders(pageable);

        assertNotNull(result);
        verify(orderRepository, times(1)).findAll(pageable);
    }
}
