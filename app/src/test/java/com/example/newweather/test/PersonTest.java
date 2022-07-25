package com.example.newweather.test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class PersonTest {

    private Person mPerson;

    @Before
    public void setUp() throws Exception {
        mPerson = new Person("Chris");
    }

    @After
    public void tearDown() throws Exception {
        mPerson = null;
    }

    @Test
    public void modifyName() {
    }

    @Test
    public void getName() {
    }

    @Test
    public void addAddressList() {
    }

    @Test
    public void addInnerAddressList() {
    }

    @Test
    public void getAllAddress() {
    }

    @Test
    public void getSchoolNo() {
    }

    @Test
    public void getModifyInfoTimes() {
    }

    /**
     * 访问私有成员
     */
    @Test
    public void testGetPrivateMemberValue() {

        // 读取
        String name = Whitebox.getInternalState(mPerson, "mName");
        Assert.assertEquals("Chris", name);

        mPerson.addAddressList("Wuhan");
        mPerson.addAddressList("Shenzhen");
        List<String> list = Whitebox.getInternalState(mPerson, "mAddressList");
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("Shenzhen", list.get(list.size()-1));
        // 读取
        // 两种修改私有成员的方法
        Whitebox.setInternalState(mPerson, "mName", "FanFF");
        Assert.assertEquals("FanFF", mPerson.getName());

        try {
            MemberModifier.field(Person.class, "mName").set(mPerson, "FanFF_166");
        } catch (Exception e) {
        }
        Assert.assertEquals("FanFF_166", mPerson.getName());
        // 修改
    }

    /**
     * 访问私有成员方法
     */
    @Test
    public void testPrivateMethod() throws Exception {
        // Verify私有方法 //
        Person personMockObj = PowerMockito.mock(Person.class);
        personMockObj.getModifyInfoTimes("school");
        PowerMockito.verifyPrivate(personMockObj, times(0)).invoke("getInnerModifyInfoTimes", anyString());

        Person personSpyObj = PowerMockito.spy(new Person("Chris"));
        personSpyObj.getModifyInfoTimes("school");
        PowerMockito.verifyPrivate(personSpyObj, times(1)).invoke("getInnerModifyInfoTimes", anyString());

        // Invoke私有方法 /
        Whitebox.invokeMethod(mPerson, "addInnerAddressList", "inner_default_addr");
        Assert.assertEquals("inner_default_addr", mPerson.getAllAddress().get(0));

        // 对私有方法进行修改
        PowerMockito.replace(PowerMockito.method(Person.class, "modifyInnerName")).with(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Whitebox.setInternalState(proxy, "mName", "modify name, haha");
                return null;
            }
        });
        mPerson.modifyName("FanFF_00");
        String curName = mPerson.getName();
        System.out.println("Name = " + curName);
        Assert.assertEquals("modify name, haha", curName);
    }

    /**
     * 调用私有构造方法
     */
    @Test
    public void testPrivateConstructMethod() throws Exception {
        Person personPrivConstruct = Whitebox.invokeConstructor(Person.class, "testPrivate");
        personPrivConstruct.addAddressList("private_province");
        Assert.assertEquals("private_province", personPrivConstruct.getAllAddress().get(0));
    }

}