import java.io.*;
import java.nio.file.*;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.util.*;

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
        File myFile;
        for (int i = 0; i < parser.args.length; i++)
        {
            myFile = new File(parser.args[i]);
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

    public void cp_r(File source, File target) throws IOException {
        if (source.isDirectory()) {
            if (!target.exists()) {
                target.mkdir();
            }

            String[] files = source.list();
            if (files != null) {
                for (String file : files) {
                    File srcFile = new File(source, file);
                    File destFile = new File(target, file);
                    cp_r(srcFile, destFile); // Recursively copy the subdirectory or file
                }
            }
        } else {
            Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
    public void chooseCommandAction(String input)
    {
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
                exceptions.put(file.getName(),2);
            else if(file.isDirectory() && !file.delete())
                exceptions.put(file.getName(),0);
            else if(file.isFile())
                exceptions.put(file.getName(),1);
        }

        return exceptions;
    }
    public void touch() throws IOException {
        File myFile;
        myFile = new File(parser.args[0]);
        myFile.createNewFile();
    }
    public String pwd(){return " ";}
    public void cd(String[] args){}
    public void chooseCommandAction(String input) throws IOException {
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
            case "exit":
                System.exit(0);
                break;
            case "cd":

                break;
            case "cp_r":
                String []arg= parser.getArgs();
                File sourceDirectory = new File(arg[0]);
                File targetDirectory = new File(arg[1]);

                try {
                    cp_r(sourceDirectory, targetDirectory);
                    System.out.println("Directory copied successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    public static void main(String[] args) throws IOException {
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
