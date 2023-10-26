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
    public String pwd(){return " ";}
    public void cd(String[] args){}
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
            case "pwd":

                break;
            case "cd":

                break;

        }
    }
    public static void main(String[] args)
    {
        Terminal myterminal = new Terminal();
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        myterminal.chooseCommandAction(input);
    }
}