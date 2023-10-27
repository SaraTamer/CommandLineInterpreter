import java.io.*;
import java.nio.file.*;
import java.nio.file.Paths;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Terminal {
    Parser parser;
    Terminal()
    {
        parser = new Parser();
    }
    public String echo()
    {
        String output = "";
        for(int i = 0 ; i < parser.args.length; i++)
        {
            output += parser.args[i];
            if(i != parser.args.length - 1)
                output += ' ';
        }
        return output;
    }
    public ArrayList<String> mkdir()
    {
        ArrayList<String> exists = new ArrayList<>();
        String myPath = System.getProperty("user.dir");
        File myFile;
        for (int i = 0; i < parser.args.length; i++)
        {
            if(parser.args[i].contains("/") || parser.args[i].contains("\\") )
            {
                myFile = new File(parser.args[i]);
            }
            else {
                myFile = new File(myPath + '/' + parser.args[i]);
            }
            if(!myFile.mkdir())
                exists.add(parser.args[i]);
        }
        return exists;
    }
    public String pwd(){return System.getProperty("user.dir");}
    public void cd(String[] args){}
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


    public void chooseCommandAction(String input)
    {
        parser.parse(input);
        switch (this.parser.commandName){
            case "echo":
                System.out.println(this.echo());
                break;
            case "mkdir":
                ArrayList<String> exists = new ArrayList<>();
                exists = this.mkdir();
                for (String exist : exists)
                    System.out.println("mkdir: can't create directory '" + exist + "': File exists");
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
            case "cd":

                break;

        }
    }
    public static void main(String[] args) {
        Terminal myterminal = new Terminal();
        Scanner in = new Scanner(System.in);
        while (true)
        {
            System.out.print("> ");
            String input = in.nextLine();
            myterminal.chooseCommandAction(input);
        }

    }
}
