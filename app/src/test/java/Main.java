
import java.util.Scanner;

/**
 * @author Relish Wang
 * @since 2018/01/07
 */
public class Main {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        StringBuilder str = new StringBuilder(cin.nextLine());
        System.out.println(str.reverse());
    }

//    @Test
//    public void test() {
////        main("I am a student".split(" "));
//        StringBuilder str = new StringBuilder("I am a student");
//        System.out.println(str.reverse());
//    }
}
