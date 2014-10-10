package Functions;

import java.io.File;

public class Dir extends Function{

    @Override
    public String make(String fileName, String command) {
        if (command != null){
            System.err.println("Command is not null in Dir");
        }else {
            File file = new File(fileName);
            if (file.isDirectory()) {
                for (File file1 : file.listFiles()) {
                    System.out.println(file1.getName());
                }
                System.out.println("..");
            } else {
                System.out.println("Not in a folder");
            }
        }
        return null;
    }

//    public static void main(String[] args) {
//        Dir dir = new Dir();
//        Scanner scanner = new Scanner(System.in);
//        dir.make(scanner.next(), null);
//    }
}
