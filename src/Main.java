import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0) {

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            args = input.split("\\s+");
        }


    }
}

