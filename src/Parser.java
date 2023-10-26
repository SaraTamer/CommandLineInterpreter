public class Parser {
    String commandName;
    String[] args;
    //This method will divide the input into commandName and args
    //where "input" is the string command entered by the user
    public boolean parse(String input)
    {
        String[] temp;
        temp = input.split("\s+");
        commandName = temp[0];
        input = input.substring(temp[0].length() + 1);
        args = input.split("\s+");
        return true;
    }
    public String getCommandName()
    {
        return commandName;
    }
    public String[] getArgs()
    {
        return args;
    }
}
