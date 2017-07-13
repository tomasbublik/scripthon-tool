public class ProbeTest3 {

    //private String foo = "a";

    private int count = 0;

    public ProbeTest3() {
        count = 1;
    }
    /*private int count1 = 0;
    private int count2 = 0;
    private int count3 = 0;*/

    public void someMethod() {
        System.out.println("Inicializováno");
        count = 6;
        int a = 3;
        int v = 4;
        System.out.println("Inicializováno");
    }
}
