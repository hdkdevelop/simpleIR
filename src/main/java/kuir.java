public class kuir {
    public static void main_(String[] args) {
        String command = "", path = "";
        try {
            command = args[0];
            path = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid Arguments");
            System.exit(0);
        }
        switch (command) {
            case "-c" -> makeCollection.htmlToCollection(path);
            case "-k" -> makeKeyword.collectionToIndex(path);
            case "-i" -> indexer.indexToPost(path);
            case "-s" -> {
                try {
                    String searchCommand = args[2], query = args[3];
                    if(searchCommand.equals("-q")) searcher.search(path, query);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Invalid Arguments: searcher");
                    System.exit(0);
                }
            }
            default -> System.out.println("Invalid Arguments");
        }
    }
}

