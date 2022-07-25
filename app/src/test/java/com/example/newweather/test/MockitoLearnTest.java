package com.example.newweather.test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.verifyZeroInteractions;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Matches;

import java.util.LinkedList;
import java.util.List;

public class MockitoLearnTest {

    /**
     * 认证行为
     */
    @Test
    public void test1() {

        // 可以模拟一个接口
        List mockedList = mock(List.class);

        // 然后可以直接使用接口的方法
        mockedList.add("one");
        mockedList.clear();

        //verification  验证是否发生某种行为，发生了，啥都不做，未发生，报错
        // 下面这句会报错，因为 add的是one而不是two
//        verify(mockedList).add("two");
        verify(mockedList).clear();
    }

    /**
     * 模拟一个实体类
     */
    @Test
    public void test2() {

        // mock一个具体的类，产生mock后的对象
        LinkedList mockedList = mock(LinkedList.class);


        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException("我自己抛出的异常"));

        // following prints "first"
        System.out.println(mockedList.get(0));

        // following throws runtime exception
//         System.out.println(mockedList.get(1));

        // null 因为没有存过999
        System.out.println(mockedList.get(999));

        mockedList.add("11111");

        // 猜想：因为不是真实对象，所以size()始终为 0
        System.out.println(mockedList.size());

        // 验证调用了get(0)
        verify(mockedList).get(0);

    }

    /**
     * 参数匹配器
     */
    @Test
    public void test3() {

        // mock一个具体的类，产生mock后的对象
        LinkedList mockedList = mock(LinkedList.class);

        //stubbing using built-in anyInt() argument matcher
        when(mockedList.get(anyInt())).thenReturn("element");

        when(mockedList.contains(argThat(new Matches("ssss")))).thenReturn(true);
        // 可以使用正则表达式，没搞清楚怎么用的，下面返回true
        System.out.println(mockedList.contains("ssss"));


        //following prints "element"
        System.out.println(mockedList.get(999));

        //you can also verify using an argument matcher
        verify(mockedList).get(anyInt());
    }


    /**
     * 验证方法调用的次数
     */
    @Test
    public void test4() {

        // mock一个具体的类，产生mock后的对象
        LinkedList mockedList = mock(LinkedList.class);

        mockedList.add("once");
        mockedList.add("twice");
        mockedList.add("twice");

        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        //following two verifications work exactly the same - times(1) is used by default
        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");

        //exact number of invocations verification
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");

        //verification using never(). never() is an alias to times(0)
        verify(mockedList, never()).add("never happened");

        // 验证最少、最多调用的次数
        verify(mockedList, atLeastOnce()).add("three times");


        // verify(mockedList, atLeast(2)).add("five times");

        verify(mockedList, atMost(5)).add("three times");
    }

    /**
     * 模拟方法抛出异常
     */
    @Test
    public void test5() {

        // mock一个具体的类，产生mock后的对象
        LinkedList mockedList = mock(LinkedList.class);

        doThrow(new RuntimeException("我就抛出异常了")).when(mockedList).clear();

        //调用的时候就会抛出异常
        mockedList.clear();
    }

    /**
     * 验证方法调用的顺序
     */
    @Test
    public void test6() {

        // A. Single mock whose methods must be invoked in a particular order
        List singleMock = mock(List.class);

        //using a single mock
        singleMock.add("was added first");
        singleMock.add("was added second");

        //create an inOrder verifier for a single mock
        InOrder inOrder = inOrder(singleMock);

        // 验证方法调用的顺序， 如果下面列出的顺序和调用的顺序一样，则通过，否则抛出异常
        inOrder.verify(singleMock).add("was added first");
        inOrder.verify(singleMock).add("was added second");


        // B. Multiple mocks that must be used in a particular order
        List firstMock = mock(List.class);
        List secondMock = mock(List.class);

        //using mocks
        firstMock.add("was called first");
        secondMock.add("was called second");

        //create inOrder object passing any mocks that need to be verified in order
        InOrder inOrder2 = inOrder(firstMock, secondMock);

        //following will make sure that firstMock was called before secondMock
        inOrder2.verify(firstMock).add("was called first");
        inOrder2.verify(secondMock).add("was called second");
    }

    /**
     * 确保 mock 上不会发生交互
     */
    @Test
    public void test7() {

        List mockOne = Mockito.mock(List.class);
        List mockTwo = Mockito.mock(List.class);
        List mockThree = Mockito.mock(List.class);

        // using mocks - only mockOne is interacted
        mockOne.add("one");

        verify(mockOne).add("one");

        // 验证没有调用过某个行为
        verify(mockOne, never()).add("two");

        // 验证两个mock之间没有发生交互
        verifyZeroInteractions(mockTwo, mockThree);
    }

    /**
     *     mock vs spy
     */
    /**
     * Mock:
     *  (1) mock对象调用的所有方法都是空方法。非void方法都将返回默认值，比如返回值为int的方法将返回0，返回值为对象的方法将返回null等，而void方法将什么都不做;
     *  (2) 适用场景：类对外部依赖较多，只关心少数函数的具体实现。
     ************************************************************************************************************************************
     * Spy:
     * (1) 是正常对象的替身，跟正常对象的使用一样;
     * (2) 适用场景：类对外部依赖较少，关心大部分函数的具体实现
     */
    @Test
    public void testMultiplyExact() {
        Calculator calculatorMockObj = Mockito.mock(Calculator.class);
        Assert.assertEquals(0, calculatorMockObj.multiplyExact(6, 8));// Pass
        // Assert.assertEquals(48, calculatorMockObj.multiplyExact(6, 8));// Fail

        Calculator calculatorSpyObj = Mockito.spy(Calculator.class);
        Assert.assertEquals(48, calculatorSpyObj.multiplyExact(6, 8));// Pass
        // Assert.assertEquals(0, calculatorSpyObj.multiplyExact(6, 8));// Fail
    }

    /**
     * 如需互通使用 可以使用打桩方法doCallRealMethod()和doReturn()来实现
     */
    @Test
    public void testSubtractExact() {
        //
        Calculator calculatorMockObj = Mockito.mock(Calculator.class);
        Assert.assertEquals(0, calculatorMockObj.subtractExact(6, 8));// Pass
        // Assert.assertEquals(-2, calculatorMockObj.subtractExact(6, 8));// Fail

        doCallRealMethod().when(calculatorMockObj).subtractExact(anyInt(), anyInt());
        Assert.assertEquals(-2, calculatorMockObj.subtractExact(6, 8));// Pass
    }

    @Test
    public void testAddExact() {
        Calculator calculatorSpyObj = Mockito.spy(Calculator.class);
        Assert.assertEquals(16, calculatorSpyObj.addExact(8, 8));// Pass
        // Assert.assertEquals(0, calculatorSpyObj.addExact(8, 8));// Fail

        doReturn(0).when(calculatorSpyObj).addExact(anyInt(), anyInt());
        Assert.assertEquals(0, calculatorSpyObj.addExact(8, 8));// Pass
    }

}
