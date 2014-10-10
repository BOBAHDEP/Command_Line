import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import Functions.*;

public class Main {
    public static final String CLASSES = "files/Classes.xml";
    public static final int NUMBER_OF_FUNCTIONS = 3;

    private String momentFileName;

    public Main(String momentFileName) {
        this.momentFileName = momentFileName;
    }

    public void setMomentFileName(String momentFileName) {
        this.momentFileName = momentFileName;
    }

    private static Document getDocument() {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setValidating(false);
            DocumentBuilder builder = f.newDocumentBuilder();
            return builder.parse(new File(CLASSES));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private String[] parseCommand(String in){
        String[] res = new String[2];
        if (in.indexOf(' ') == -1){
            res[0] = in;
            res[1] = null;
        } else {
            res[0] = in.substring(0, in.indexOf(' '));
            res[1] = in.substring(in.indexOf(' ')+1, in.length() );
        }
        return res;
    }

    private boolean validate(String string){
        String[] names = parseCommand(string);
        String[][] example = getClassFunctions();
        for (int i = 0; i < NUMBER_OF_FUNCTIONS; i++){
            if (example[1][i].equals(names[0])){
                return true;
            }
        }
        return false;
    }

    private static String[][] getClassFunctions(){
        String[][] res = new String[2][NUMBER_OF_FUNCTIONS];
        NodeList methodNodes = getDocument().getElementsByTagName("command");
        if (methodNodes.getLength() == 0){
            System.err.println("Couldn't read XML");
            return null;
        }else {
            for (int i = 0; i < methodNodes.getLength(); i++) {
                Node node = methodNodes.item(i);
                NamedNodeMap attributes = node.getAttributes();
                Node nameAttrib = attributes.getNamedItem("name");
                String functionName = nameAttrib.getNodeValue();
                Node class_nameAttrib = attributes.getNamedItem("class_name");
                String class_name = class_nameAttrib.getNodeValue();
                res[0][i] = class_name;
                res[1][i] = functionName;
            }
        }
        return res;
    }

    private String getClassName(String commandSorted){
        String[][] examples = getClassFunctions();
        for (int i = 0; i < NUMBER_OF_FUNCTIONS; i++){
            if (examples[1][i].equals(commandSorted))
                return examples[0][i];
        }
        return null;
    }

    private void make(String command) throws Exception{
        if (validateIn(command) && validate(command)){
            Object object=Class.forName("Functions."+getClassName(parseCommand(command)[0])).newInstance();
            if (object.getClass().equals(Dir.class)) {
                Dir dir = (Dir)object;
                System.out.println("Files:");
                if (dir.make(momentFileName,parseCommand(command)[1]) != null){
                    throw new IOException();
                }
                System.out.println("---------");
            } else if (object.getClass().equals(CD.class)) {
                CD cd = (CD)object;
                String newName = cd.make(momentFileName,parseCommand(command)[1]);
                if (newName == null){
                    throw new IOException();
                }
                if (newName.equals("-1")){
                    System.out.println("Wrong file name");
                    return;
                }
                System.out.println("New direction:");
                System.out.println(newName);
                setMomentFileName(newName);
            } else if (object.getClass().equals(Pwd.class)) {
                System.out.println("Directory:");
                Pwd pwd = (Pwd)object;
                if (pwd.make(momentFileName,parseCommand(command)[1]) != null){
                    throw new IOException();
                }
                System.out.println("---------");
            }
        } else {
            System.out.println("Wrong command");
        }
    }

    private static void printHelp(){
        System.out.println("Possible functions:");
        for (String s: getClassFunctions()[1]){
            System.out.println(s);
        }

    }

    private boolean validateIn(String name){
        return name.length() >= 2;
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main("C:\\");
        System.out.println("Enter \"help\" for help.");
        System.out.println("C:\\");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String command = scanner.nextLine();
            if (command.equals("exit")){
                break;
            }
            if (command.equals("help")){
                printHelp();
                continue;
            }
            main.make(command);
        }
        scanner.close();
    }
}
