import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0) {

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            args = input.split("\\s+");
        }
        if (args[0].toLowerCase().equals("-list") || args[0].toLowerCase().equals("-fire") || args[0].toLowerCase().equals("-h") || args[0].toLowerCase().equals("--help")) {

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
                    System.out.println("No request saved.");
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
            if (args[0].toLowerCase().equals("-h") || args[0].toLowerCase().equals("--help")) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader("help.txt"));

                    String line;
                    StringBuffer sb = new StringBuffer();

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                        sb.append("\n");
                    }
                    System.out.println("\u001B[34m"+sb.toString()+"\u001B[0m");
                    bufferedReader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("help.txt file is not exist.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {

            Request request = new Request(args);

            StringBuilder temp = new StringBuilder();

            for (String arg : args) {
                temp.append(arg).append(" ");
            }

            temp.deleteCharAt(temp.length() - 1);
            String input = temp.toString();

            if (input.contains("--save") || input.contains("-S")) {
                File file = new File("commands.sav");

                try {
                    FileWriter fr = new FileWriter(file, true);
                    input = input.replace(" --save", "");
                    input = input.replace(" -S", "");
                    fr.write(request.toString() + "\n");
                    fr.write(input + "\n");
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            request.send();
        }
    }
}

