package com.jyotibank.service;

import com.jyotibank.dao.UserDao;
import com.jyotibank.exception.AuthenticationException;
import com.jyotibank.model.User;
import com.jyotibank.model.enums.UserRole;
import com.jyotibank.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserDao userDao;
    @Mock private AuditService auditService;

    private AuthService service;

    private User activeUser(String password) {
        User user = new User("jyoti", PasswordUtil.hashPassword(password), UserRole.CUSTOMER);
        user.setUserId(9L);
        user.setCustomerId(3L);
        user.setActive(true);
        return user;
    }

    @BeforeEach
    void setUp() {
        service = new AuthService(userDao, auditService);
    }

    @Test
    @DisplayName("valid login updates last-login and records LOGIN_SUCCESS")
    void successfulLogin() {
        when(userDao.findByUsername("jyoti")).thenReturn(Optional.of(activeUser("s3cret")));

        User user = service.login("jyoti", "s3cret");

        assertEquals(9L, user.getUserId());
        verify(userDao).updateLastLogin(eq(9L), any(LocalDateTime.class));
        verify(auditService).record(eq(9L), eq("LOGIN_SUCCESS"), eq("USER"), eq(9L), any());
    }

    @Test
    @DisplayName("wrong password throws AuthenticationException and audits the failure")
    void wrongPasswordRejected() {
        when(userDao.findByUsername("jyoti")).thenReturn(Optional.of(activeUser("s3cret")));

        assertThrows(AuthenticationException.class, () -> service.login("jyoti", "wrong"));

        verify(userDao, never()).updateLastLogin(anyLong(), any());
        verify(auditService).record(anyLong(), eq("LOGIN_FAILED"), eq("USER"), any(), any());
    }

    @Test
    @DisplayName("inactive users cannot log in even with correct credentials")
    void inactiveUserBlocked() {
        User user = activeUser("s3cret");
        user.setActive(false);
        when(userDao.findByUsername("jyoti")).thenReturn(Optional.of(user));

        assertThrows(AuthenticationException.class, () -> service.login("jyoti", "s3cret"));

        verify(userDao, never()).updateLastLogin(anyLong(), any());
    }

    @Test
    @DisplayName("duplicate username registration is rejected")
    void duplicateUsernameRejected() {
        when(userDao.existsByUsername("jyoti")).thenReturn(true);

        assertThrows(AuthenticationException.class,
                () -> service.registerCustomerUser(1L, "jyoti", "pw", 3L));

        verify(userDao, never()).create(any());
    }
}
