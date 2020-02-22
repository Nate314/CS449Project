package com.nathangawith.umkc.financeapp;

import android.content.Context;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.function.Consumer;

import static org.junit.Assert.*;

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
