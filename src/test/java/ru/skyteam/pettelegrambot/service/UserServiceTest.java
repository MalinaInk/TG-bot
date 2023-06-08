package ru.skyteam.pettelegrambot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skyteam.pettelegrambot.entity.LastAction;
import ru.skyteam.pettelegrambot.entity.PetType;
import ru.skyteam.pettelegrambot.entity.User;
import ru.skyteam.pettelegrambot.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    public void createTest() {
        User user1 = new User(12L,156L,PetType.DOG, "12345678");
        when(userRepository.save(user1)).thenReturn(user1);
        assertThat(user1).isEqualTo(userServiceImpl.create(user1));
        assertThat(userServiceImpl.create(user1)).isNotNull();
    }

    @Test
    public void readTest() {
        User user1 = new User(12L,156L,PetType.DOG, "12345678");
        when(userRepository.getUserById(1L)).thenReturn(user1);
        assertThat(user1).isEqualTo(userServiceImpl.read(1L));
        assertThat(userServiceImpl.read(1L)).isNotNull();
    }

    @Test
    public void updateTest() {
        User user2 = new User(52L,186L,PetType.CAT, LastAction.START_CONTACT);
        when(userRepository.save(user2)).thenReturn(user2);
        assertThat(user2).isEqualTo(userServiceImpl.update(user2));
        assertThat(userServiceImpl.update(user2)).isNotNull();
    }

    @Test
    public void deleteTest() {
        userServiceImpl.delete(28L);
        verify(userRepository).deleteById(28L);
    }

    @Test
    public void readAllTest() {
        List<User> arr = new ArrayList<User>(3);
        User user1 = new User(12L,156L,PetType.DOG, "12345678");
        User user2 = new User(52L,186L,PetType.CAT, LastAction.START_CONTACT);
        User user3 = new User(22L,86L,PetType.CAT, LastAction.WAITING_USER_FULL_NAME);
        arr.add(user1);
        arr.add(user2);
        arr.add(user3);
        when(userRepository.findAll()).thenReturn(arr);
        assertThat(arr).isEqualTo(userServiceImpl.readAll());
        assertThat(userServiceImpl.readAll()).isNotNull();
    }

    @Test
    public void testFindUserByChatId() {
        Long chatId = 12349L;
        User user = new User();
        user.setId(1L);
        user.setChatId(chatId);
        when(userRepository.getUserByChatId(chatId)).thenReturn(user);
        User result = userServiceImpl.findUserByChatId(chatId);
        assertEquals(user, result);
    }

    @Test
    public void testSave() {
        User user = new User();
        user.setId(12L);
        user.setChatId(12546L);
        user.setPetType(PetType.CAT);
        user.setLastAction(null);
        when(userRepository.save(user)).thenReturn(user);
        User savedUser = userServiceImpl.save(user);
        verify(userRepository, times(1)).save(user);
        assertEquals(user, savedUser);
    }
}
