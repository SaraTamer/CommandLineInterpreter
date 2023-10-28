import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.io.IOException;
import java.util.*;

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
        File myFile;
        for (int i = 0; i < parser.args.length; i++)
        {
            myFile = new File(parser.args[i]);
            if(!myFile.mkdir())
                exists.add(parser.args[i]);
        }
        return exists;
    }
    public void touch() throws IOException {
        File myFile;
        myFile = new File(parser.args[0]);
        myFile.createNewFile();
    }
    public Map<String, Integer> rmdir()
    {
        Map<String, Integer> exceptions = new HashMap<>();
        String myPath = System.getProperty("user.dir");
        File[] files = new File(myPath).listFiles();
        if(Objects.equals(parser.args[0], "*"))
        {
            for (File file : files)
            {
                if(file.isDirectory() && !file.delete())
                {
                    exceptions.put(file.getName(),0);           // '0' represents non-empty directory
                }
                else if(file.isFile())
                {
                    exceptions.put(file.getName(),1);          // '1' represents not directory
                }
            }
        }
        else {
            File file = new File(parser.args[0]);
            if(!file.isDirectory() && !file.isFile())
                exceptions.put(file.getName(),2);                // '2' represents no such file or directory
            else if(file.isDirectory() && !file.delete())
                exceptions.put(file.getName(),0);
            else if(file.isFile())
                exceptions.put(file.getName(),1);
        }

        return exceptions;
    }

    public String pwd(){return System.getProperty("user.dir");}
    public boolean cp(String[] arg) {
        String source = arg[0];
        String dest = arg[1];
        String check=".txt";
        boolean flag = source.contains(check);
        if(!flag){
            source+=".txt";
        }
        try (FileInputStream sourceFileInputStream = new FileInputStream(source);
             FileOutputStream destinationFileOutputStream = new FileOutputStream(dest)) {

            int bytesRead;
            while ((bytesRead = sourceFileInputStream.read()) != -1) {
                destinationFileOutputStream.write(bytesRead);
            }

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        return false;
    }
    public boolean wc(String[] arg) throws Exception {
        try {
            Path file = Paths.get(arg[0]);
            long count_l = 0;
            long count_c = 0;
            long count_w = 0;
            String line;
            BufferedReader reader = Files.newBufferedReader(file);
            while ((line = reader.readLine()) != null) {
                count_l++;
                count_c += line.length();
                String[] words = line.split("\\s+");
                count_w += words.length;
            }
            System.out.println("Total Lines: " + count_l);
            System.out.println("Total Words: " + count_w);
            System.out.println("Total Chars: " + count_c);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
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
public void chooseCommandAction(String input) throws IOException {
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
        case "rmdir":
            Map<String, Integer> exceptions = new HashMap<>();
            exceptions = rmdir();
            for(Map.Entry<String, Integer> exception: exceptions.entrySet())
            {
                if(exception.getValue() == 0)
                {
                    System.out.println("rmdir: '" + exception.getKey() + "': Directory not empty");
                }
                else if (exception.getValue() == 1)
                    System.out.println("rmdir: '" + exception.getKey() + "': Not a directory");
                else
                    System.out.println("rmdir: '" + exception.getKey() + "': No such file or directory");
            }
            break;
        case "touch":
            touch();
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
        case "wc":
            try {
                wc(parser.getArgs());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            break;
        case "pwd":
            System.out.println(pwd());
            break;
        case "cp":
            cp(parser.getArgs());
            break;
        case "exit":
            System.exit(0);
            break;
    }
}

    public static void main(String[] args) throws IOException {

        Terminal myterminal = new Terminal();
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String input = in.nextLine();
            myterminal.chooseCommandAction(input);
        }

    }
}