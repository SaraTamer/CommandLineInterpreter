import java.util.Scanner;

public class Terminal {
    Parser parser;
    //Implement each command in a method, for example:
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
    public String pwd(){return " ";}
    public void cd(String[] args){}
    // ...
    //This method will choose the suitable command method to be called
    public void chooseCommandAction(String input)
    {
        parser.parse(input);
        switch (this.parser.commandName){
            case "echo":
                System.out.println(this.echo());
                break;
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