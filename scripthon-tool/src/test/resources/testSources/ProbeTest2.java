package testSources;

public class ProbeTest2 {

    private int count = 0;

    public ProbeTest2() {
        count = 1;
    }

    @Deprecated
    public void someMethod() {
        if (count == 1) {
            System.out.println("Inicializováno");
        }
    }
}
