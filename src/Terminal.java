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