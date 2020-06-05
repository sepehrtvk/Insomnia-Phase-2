import java.io.*;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {

        if (args.length == 0) {

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            args = input.split("\\s+");
        }
        if (args[0].toLowerCase().contains("list") || args[0].toLowerCase().contains("fire") || args[0].toLowerCase().equals("-h") || args[0].toLowerCase().equals("--help")) {

            if (args[0].toLowerCase().contains("list")) {

                if (args.length == 1) {
                    File folder = new File("Requests");
                    File[] listOfFiles = folder.listFiles();
                    if (listOfFiles != null) {
                        for (File file : listOfFiles) {
                            if (file.isFile()) {
                                System.out.println(file.getName());
                            }
                        }
                    } else System.out.println("No list found.");
                } else {
                    try {
                        Scanner sc = new Scanner(new File("Requests/" + args[1]));
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
            } else {
                for (String arg : args) {
                    if (arg.contains("fire")) continue;
                    try {
                        Scanner sc = new Scanner(new File("Requests/" + args[1]));
                        for (int i = 1; i < Integer.parseInt(args[2]); i++) {
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
            if (args[0].toLowerCase().equals("-h") || args[0].toLowerCase().equals("--help")) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader("help.txt"));

                    String line;
                    StringBuffer sb = new StringBuffer();

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                        sb.append("\n");
                    }
                    System.out.println("\u001B[34m" + sb.toString() + "\u001B[0m");
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

            if (input.contains("-create")) {
                File file;
                for (int i = 0; i < args.length; i++) {
                    if (args[i].equals("-create")) {
                        file = new File("Requests/" + args[i + 1]);
                        FileWriter fr;
                        try {
                            fr = new FileWriter(file);
                            fr.write("");
                            fr.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return;
            }

            if (input.contains("--save") || input.contains("-S")) {
                File file = new File("commands.sav");
                for (int i = 0; i < args.length; i++) {
                    if (args[i].equals("--save") || args[i].equals("-S")) {
                        file = new File("Requests/" + args[i + 1]);

                    }
                }
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

