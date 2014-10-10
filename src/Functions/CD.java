package Functions;

import java.io.File;

public class CD  extends Function{
    @Override
    public String make(String fileName, String command) {
        File file = new File(fileName);
        if (file.isDirectory()) {
            for (File file1 : file.listFiles()) {
                if (file1.getName().equals(command) ){
                    return file1.getAbsolutePath();
                }
            }
            if (command.equals("..")){
                return new File(fileName).getParent() == null ? "-1" : new File(fileName).getParent();
            }
            File resFile = new File(command);
            if (resFile.isDirectory()){
                return resFile.getAbsolutePath();
            } else {
                return "-1"; //wrong command
            }
        } else {
            System.err.println("File is not a directory in CD");
        }
        System.err.println("Reached end of CD");
        return null;
    }
}
