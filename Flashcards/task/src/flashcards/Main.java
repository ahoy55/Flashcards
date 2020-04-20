package flashcards;


import java.io.*;
import java.util.*;


public class Main {
    static List<String> cardList = new ArrayList<>();
    static List<String> defList = new ArrayList<>();
    static List<String> lines = new ArrayList<>();
    static HashMap<String, Integer> errors = new HashMap<>();
    static List<String> log = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        /*errors.put("France", 4);
        errors.put("Russia", 2);
        for(Map.Entry<String, Integer> s : errors.entrySet()) {
            System.out.println(s);
        }
        errors.put("France", errors.get("France") + 1);

        for(Map.Entry<String, Integer> s : errors.entrySet()) {
            System.out.println(s);
        }*/
        menu();
    }

    public static void reset() {
        errors.clear();
        log("Card statistics has been reset.");
        menu();
    }

    public static void log(String logLine, boolean isOut) {
        log.add(logLine);
        if(isOut) System.out.println(logLine);
    }

    public static void log(String logLine) {
        System.out.println(logLine);
        log.add(logLine);
    }

    public static void hard() {
        ArrayList<String> err = new ArrayList<>();
        if (errors.isEmpty()) {
            log("There are no cards with errors.");
        } else {
            int max = Collections.max(errors.values());
            for (Map.Entry<String, Integer> s : errors.entrySet()) {
                String str = s.getKey();
                int val = s.getValue();
                if (val == max) err.add(str);
            }
            if (err.size() == 1) {
                log("The hardest card is \"" + err.get(0) + "\". You have " + max + " errors answering it.");
            } else if (err.size() > 1) {
                StringBuilder str = new StringBuilder("The hardest cards are ");
                for (int i = 0; i < err.size(); i++) {
                    String s = err.get(i);
                    str.append("\"").append(s).append("\"");
                    if (i == err.size() - 1) break;
                    str.append(", ");
                }
                str.append(". You have ").append(max).append(" errors answering them.");
                log(str.toString());
            }
        }
        menu();
    }

    public static void menu() {
        log("\nInput the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
        Scanner scan = new Scanner(System.in);
        String choose = scan.nextLine();
        log(choose, false);
        switch (choose) {
            case "add":
                add();
                break;
            case "log":
                doLog();
                break;
            case "hardest card":
                hard();
                break;
            case "reset stats":
                reset();
                break;
            case "remove":
                remove();
                break;
            case "import":
                imp();
                break;
            case "export":
                export();
                break;
            case "ask":
                ask();
                break;
            case "exit":
                exit();
                break;
            case "test":
                test();
                break;
            default:
                menu();
        }
    }

    public static void ask() {
        Scanner scan = new Scanner(System.in);
        log("How many times to ask?");
        int num = scan.nextInt();
        log(String.format("%d", num), false);
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            int ran = random.nextInt(cardList.size());
            log("Print the definition of \"" + cardList.get(ran) + "\"");
            Scanner sc = new Scanner(System.in);
            String answer = sc.nextLine();
            log(answer, false);
            String str = defList.get(ran);
            String card = cardList.get(ran);
            if (str.equals(answer)) {
                log("Correct answer\n");
            } else {
                StringBuilder line;
                line = new StringBuilder(("Wrong answer. (The correct one is \"" + str + "\".)"));
                for (String s : defList) {
                    if (s.equals(answer))
                        line.append(", you've just written the definition of \"").append(cardList.get(defList.indexOf(s))).append("\" card.)");
                }
                log(line.toString());
                if(errors.containsKey(card)) errors.put(card, (errors.get(card) + 1));
                else errors.put(card, 1);
            }
        }
        log("\n");
        menu();
    }

    public static void export() {
        log("File name:");
        Scanner scan = new Scanner(System.in);
        String nameFile = scan.nextLine();
        log(nameFile, false);
        try {
            FileWriter expFile = new FileWriter(nameFile);
            for (int i = 0; i < cardList.size(); i++) {
                String card = cardList.get(i);
                String def = defList.get(i);
                expFile.write(card + ":" + def + System.getProperty("line.separator"));
            }
            expFile.close();
            log(cardList.size() + " cards have been saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        defList.clear();
        cardList.clear();
        menu();
        expErrors();
    }

    public static void doLog() {
        log("File name:");
        Scanner scan = new Scanner(System.in);
        String nameFile = scan.nextLine();
        log(nameFile, false);
        try {
            FileWriter expFile = new FileWriter(nameFile);
            for(String s : log) {
                expFile.write(s + System.getProperty("line.separator"));
            }
            expFile.close();
            log("The log has been saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        menu();
    }

    public static void remove() {
        log("The card:");
        Scanner sc = new Scanner(System.in);
        String cardName = sc.nextLine();
        log(cardName, false);
        if (cardList.contains(cardName)) {
            int i = cardList.indexOf(cardName);
            cardList.remove(i);
            defList.remove(i);
            log("The card has been removed.");
        } else log("Can't remove \"" + cardName + "\": there is no such card.");
        menu();
    }

    public static void add() {
        log("The card:");
        Scanner sc = new Scanner(System.in);
        String cardName = sc.nextLine();
        log(cardName, false);
        if (cardList.contains(cardName)) {
            log("The card \"" + cardName + "\" already exists");
            menu();
        } else {
            //System.out.println("*** card created");
            def(cardName);
        }
    }

    public static void def(String cardName) {
        log("The definition of the card:");
        Scanner sc = new Scanner(System.in);
        String defName = sc.nextLine();
        log(defName, false);
        if (defList.contains(defName)) {
            log("The definition \"" + defName + "\" already exists.");
        } else {
            defList.add(defName);
            cardList.add(cardName);
            log(String.format("The pair (\"%s\":" +
                    "\"%s\") has been added.", cardName, defName));
        }
        menu();
    }

    public static void test() {
        for (int i = 0; i < cardList.size(); i++) {
            log("[" + (i + 1) + "] " + cardList.get(i) + " : " + defList.get(i));
        }
        menu();
    }

    public static void exit() {
        log("Bye bye!");
        System.exit(0);
    }

    public static void expErrors() {
        try {
            FileWriter expFile = new FileWriter("errors.txt");
            for (Map.Entry<String, Integer> entry : errors.entrySet()) {
                String card = entry.getKey();
                int err = entry.getValue();
                expFile.write(card + ":" + err + System.getProperty("line.separator"));
            }
            expFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initErrors() {
        try {
            List<String> str = new ArrayList<>();
            File impFile = new File("errors.txt");
            if (impFile.exists()) {
                Scanner scan = new Scanner(impFile);
                while (scan.hasNextLine()) {
                    str.add(scan.nextLine());
                }
                String[][] errs = new String[str.size()][];
                for (int i = 0; i < str.size(); i++) {
                    String s = str.get(i);
                    errs[i] = s.split( ":");
                }
                for (String[] err : errs) {
                    errors.put(err[0], Integer.parseInt(err[1]));
                }
            }
            menu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLines() {
        int count = 0;
        String[][] cardDef = new String[lines.size()][lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            cardDef[i] = lines.get(i).split(":");
            if (cardList.contains(cardDef[i][0])) {
                defList.set(cardList.indexOf(cardDef[i][0]), cardDef[i][1]);
            } else {
                cardList.add(cardDef[i][0]);
                defList.add(cardDef[i][1]);
            }
        }
        log((lines.size() - count) + " cards have been loaded.");
        lines.clear();
    }

    public static void imp() {
        lines.clear();
        log("File name:");
        Scanner sc = new Scanner(System.in);
        String nameFile = sc.nextLine();
        log(nameFile, false);
        try {
            File impFile = new File(nameFile);
            if (impFile.exists()) {
                Scanner scan = new Scanner(impFile);
                while (scan.hasNextLine()) {
                    lines.add(scan.nextLine());
                }
                writeLines();
            } else {
                log("File not found.");
            }
            initErrors();
            menu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
