import com.example.BodyWeightCalculator.entity.ResultEntity;
import com.example.BodyWeightCalculator.entity.User;
import com.example.BodyWeightCalculator.jpa.ResultJPARepository;
import com.example.BodyWeightCalculator.jpa.UserRepository;
import com.example.BodyWeightCalculator.model.ResponseIndex;
import com.example.BodyWeightCalculator.service.BodyWeightService;
import com.example.BodyWeightCalculator.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BodyWeightServiceTest {
    @Mock
    private ResultJPARepository resultJPARepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private BodyWeightService bodyWeightService;

    private User testUser;
    private ResultEntity testEntity;

    @BeforeEach
    void setUp(){
        testUser = new User("Ivanov Ivan", "ivanov@yandex.ru");
        testEntity = new ResultEntity(70, 1.75, 22.86,"нормальный вес", LocalDate.now(), testUser);
        testEntity.setId(1L);
    }

    @Test
    void calculateIndex_shouldSaveAndReturn(){

        when(userService.getUserById(1L)).thenReturn(testUser);
        when(resultJPARepository.save(any(ResultEntity.class))).thenReturn(testEntity);
        ResponseIndex result = bodyWeightService.calculateIndex(70,1.75,1L);
        assertThat(result.getIndex()).isCloseTo(22.86,within(0.01));
        assertThat(result.getCategory()).isEqualTo("нормальный вес");
        verify(resultJPARepository,times(1)).save(any(ResultEntity.class));
    }

    @Test
    void getDataById_whenExists_shouldReturnResponse(){
        when(resultJPARepository.findById(1L)).thenReturn(Optional.of(testEntity));
        ResponseIndex result = bodyWeightService.getDataById(1L);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getWeight()).isEqualTo(70);

    }
}
