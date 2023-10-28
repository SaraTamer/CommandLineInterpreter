import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class Terminal {
    Parser parser;

    Terminal() {
        parser = new Parser();
    }

    public String echo() {
        String output = "";
        for (int i = 0; i < parser.args.length; i++) {
            output += parser.args[i];
            if (i != parser.args.length - 1)
                output += ' ';
        }
        return output;
    }

    public ArrayList<String> mkdir() {
        ArrayList<String> exists = new ArrayList<>();
        String myPath = System.getProperty("user.dir");
        File myFile;
        for (int i = 0; i < parser.args.length; i++) {
            if (parser.args[i].contains("/") || parser.args[i].contains("\\")) {
                myFile = new File(parser.args[i]);
            } else {
                myFile = new File(myPath + '/' + parser.args[i]);
            }
            if (!myFile.mkdir())
                exists.add(parser.args[i]);
        }
        return exists;
    }

    public String pwd() {
        return " ";
    }



    public void chooseCommandAction(String input) {
        parser.parse(input);
        switch (this.parser.commandName) {
            case "echo":
                System.out.println(this.echo());
                break;
            case "mkdir":
                ArrayList<String> exists = new ArrayList<>();
                exists = this.mkdir();
                for (String exist : exists)
                    System.out.println("mkdir: can't create directory '" + exist + "': File exists");
                break;
            case "exit":
                System.exit(0);
                break;
            case "cd":
                break;
            case "ls":
                this.ls();
                break;
            case "ls_r":
                this.ls_r();
                break;
            case "rm":
                this.rm();
                break;
            case "cat":
                this.cat();
                break;

        }
    }

    /*________________________Hadeer____________________________*/
    public void ls() {
        //Get the current directory
        Path currentDirectory = Paths.get(System.getProperty("user.dir"));
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(currentDirectory)) {
            //Iterate over the files and directories in the current directory
            for (Path path : directoryStream) {
                System.out.println(path.getFileName());
            }
        } catch (IOException e) {
            e.printStackTrace(); // from library
        }
    }

    public void ls_r() {
        //Get the current directory
        Path current = Paths.get(System.getProperty("user.dir"));
        List<Path> reversed = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(current)) {
            for (Path path : directoryStream) {
                reversed.add(path);
            }
            Collections.reverse(reversed);

            // Print the reversed paths
            for (Path path : reversed) {
                System.out.println(path.getFileName());
            }
        } catch (IOException e) {
            e.printStackTrace(); // from library
        }
    }

    public void rm() {
        String[] temp = parser.getArgs(); // Specify the name of the file to remove
        String fileName = temp[0];
        File currentDirectory = new File(System.getProperty("user.dir"));
        File fileToRemove = new File(currentDirectory, fileName);
        if (fileToRemove.exists()) {
            if (fileToRemove.delete()) {
                System.out.println("File removed successfully: " + fileToRemove.getAbsolutePath());
            } else {
                System.out.println("Failed to remove the file: " + fileToRemove.getAbsolutePath());
            }
        } else {
            System.out.println("File not found: " + fileToRemove.getAbsolutePath());
        }

    }
    public void cat() {

        String fileName = null;
        if(parser.args.length == 1) {
            String[] temp = parser.getArgs(); // Specify the name of the file to remove
             fileName = temp[0];
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);

                }
                System.out.println();
            } catch (IOException e) {
                System.err.println("Error reading the file: " + e.getMessage());
            }
        }
        else if(parser.args.length == 2){
            String[] temp = parser.getArgs();
            String f1 = temp[0];
            String f2 = temp[1];
            try (BufferedReader reader = new BufferedReader(new FileReader(f1))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.print(line);
                }

            } catch (IOException e) {
                System.err.println("Error reading the file: " + e.getMessage());
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(f2))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                System.out.println();
            } catch (IOException e) {
                System.err.println("Error reading the file: " + e.getMessage());
            }

        }

    }
    public void cd() {
        Path currentDirectory = Paths.get(System.getProperty("user.dir"));
        //Path newPath = currentDirectory.resolve();



    }





/*--------------------------------------------------------------*/

    public static void main(String[] args) {

        Terminal myterminal = new Terminal();
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String input = in.nextLine();
            myterminal.chooseCommandAction(input);
        }

    }
}