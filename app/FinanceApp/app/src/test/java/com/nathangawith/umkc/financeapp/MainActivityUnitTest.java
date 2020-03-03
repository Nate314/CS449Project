package com.nathangawith.umkc.financeapp;

import com.nathangawith.umkc.financeapp.activites.MainActivity;
import com.nathangawith.umkc.financeapp.activites.TokenResponseDto;
import com.nathangawith.umkc.financeapp.http.MyApi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.Consumer;

public class MainActivityUnitTest {

    private MainActivity mActivity;

    @Mock
    private MyApi api;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mActivity = new MainActivity();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public <T extends Object> void testLogin() {
        String username = "username";
        String password = "password";
        TokenResponseDto result = new TokenResponseDto();
        result.token = "jalkjshglkawejndkljwejfd";

        Consumer<Class> tokenResponseDtoConsumer = t -> System.out.println();
        Consumer<Throwable> throwableConsumer = t -> System.out.println();

//        Mockito.when(
//                MyApi.postLogin(
//                        Mockito.any(Context.class),
//                        Mockito.any(Class.class),
//                        username, password,
//                        Mockito.any(tokenResponseDtoConsumer.getClass()),
//                        Mockito.any(throwableConsumer.getClass())
//                )
//        ).thenReturn(result);
    }
}
