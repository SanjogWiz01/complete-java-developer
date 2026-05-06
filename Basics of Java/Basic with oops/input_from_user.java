import java.util.Scanner;
public class input_from_user {
    public static void main(String[] args) {
        System.out.println("Enter your name:");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        System.out.println("Hello " + name + "!");
        sc.close();
    }
}
