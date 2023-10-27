public class Parser {
    String commandName;
    String[] args;

    public boolean parse(String input) {
        String[] temp = input.split("\\s+");
        commandName = temp[0];
        args = new String[temp.length - 1];
        System.arraycopy(temp, 1, args, 0, args.length);
        return true;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }
}
