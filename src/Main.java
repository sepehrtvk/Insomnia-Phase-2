import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0) {

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            args = input.split("\\s+");
        }
        if (args[0].toLowerCase().equals("-list") || args[0].toLowerCase().equals("-fire")) {

            if (args[0].toLowerCase().equals("-list")) {

                try {

                    Scanner sc = new Scanner(new File("commands.sav"));
                    int i = 1;
                    while (sc.hasNext()) {

                        System.out.println(i + ": " + sc.nextLine());
                        i++;
                        sc.nextLine();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (args[0].toLowerCase().equals("-fire")) {

                int n = Integer.parseInt(args[1]);

                try {

                    Scanner sc = new Scanner(new File("commands.sav"));
                    for (int i = 1; i < n; i++) {

                        sc.nextLine();
                        sc.nextLine();
                    }
                    sc.nextLine();
                    Request request = new Request(sc.nextLine().split("\\s+"));
                    request.send();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

