package homework;

public class DemoTest {

    @Before
    public void beforeMethod1() {
        System.out.println("beforeMethod1");
    }

    @Before
    public void beforeMethod2() {
        System.out.println("beforeMethod2");
    }

    @Test
    public void testMethod1() {
        System.out.println("\ntestMethod1\n");
        throw new IllegalArgumentException();
    }

    @Test
    public void testMethod2() {
        System.out.println("\ntestMethod2\n");
    }

    @After
    public void afterMethod1() {
        System.out.println("afterMethod1");
    }

    @After
    public void afterMethod2() {
        System.out.println("afterMethod2");
    }

}
