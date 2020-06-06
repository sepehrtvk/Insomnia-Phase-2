import java.io.*;
import java.util.Scanner;

/**
 * The Main class with acts like a HTTP Client and it can be used to send HTTP requests
 * with the given Method and get the response from the server.
 *
 * @author sepehr tavakoli
 * @version 1.0
 * @since 2020-05-01
 */
public class Main {


    public static void main(String[] args) {

        //get input form args.
        if (args.length == 0) {

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            args = input.split("\\s+");
        }
        //list , fire and help commands.
        if (args[0].toLowerCase().contains("list") || args[0].toLowerCase().contains("fire") || args[0].toLowerCase().equals("-h") || args[0].toLowerCase().equals("--help")) {

            //list command to show request groups.
            if (args[0].toLowerCase().contains("list")) {

                if (args.length == 1) {
                    File folder = new File("Requests");
                    File[] listOfFiles = folder.listFiles();
                    if (listOfFiles != null) {
                        for (File file : listOfFiles) {
                            if (file.isFile()&&!file.getName().equals(".DS_Store")) {
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
                //fire command to send requests from each group.
                int counter = 0;
                for (String arg : args) {
                    if (arg.contains("fire")) continue;
                    try {

                        Scanner sc = new Scanner(new File("Requests/" + args[1]));
                        for (int i = 1; i < Integer.parseInt(args[2 + counter]); i++) {
                            sc.nextLine();
                            sc.nextLine();
                        }
                        sc.nextLine();
                        Request request = new Request(sc.nextLine().split("\\s+"));
                        request.send();
                        counter++;
                        if (counter + 2 >= args.length) break;
                        System.out.println();
                        System.out.println();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            //-h or --help commands to see the help manual.
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

            //make a new request.
            Request request = new Request(args);

            StringBuilder temp = new StringBuilder();

            for (String arg : args) {
                temp.append(arg).append(" ");
            }

            temp.deleteCharAt(temp.length() - 1);
            String input = temp.toString();

            //create a new group of requests.
            if (input.contains("-create")) {
                File file;
                for (int i = 0; i < args.length; i++) {
                    if (args[i].equals("-create")) {
                        file = new File("Requests/" + args[i + 1]);
                        FileWriter fr;
                        System.out.println("List " + args[i + 1] + " created.");
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

            //-S or --save commands to save a request on a file.
            if (input.contains("--save") || input.contains("-S")) {
                File file = new File("commands.sav");
                for (int i = 0; i < args.length; i++) {
                    if (args[i].equals("--save") || args[i].equals("-S")) {
                        file = new File("Requests/" + args[i + 1]);
                    }
                }

                try {
                    //write on the file.
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
            //send a new request.
            request.send();
        }
    }
}

