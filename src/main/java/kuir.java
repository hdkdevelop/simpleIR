public class kuir {
    public static void main(String[] args) {
        String command = "", path = "";
        try {
            command = args[0];
            path = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid Arguments");
            System.exit(0);
        }
        switch (command) {
            case "-c":
                makeCollection.htmlToCollection(path);
                break;
            case "-k":
                break;
            default:
                break;
        }
    }
}

