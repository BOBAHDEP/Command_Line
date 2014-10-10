package Functions;

public class Pwd extends Function {
    @Override
    public String make(String fileName, String command) {
        if (command != null){
            System.err.println("In PWD command is not null!");
        } else {
            System.out.println(fileName);
        }
        return null;
    }
}
