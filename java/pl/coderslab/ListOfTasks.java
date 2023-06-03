package pl.coderslab;

import pl.coderslab.util.ConsoleColors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Scanner;

public class ListOfTasks {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean checkIfFileExists = false;
        PrintWriter printWriter = null;
        String scan;
        String fileName="";

        System.out.println(ConsoleColors.GREEN_BOLD + "Welcome to the listing your tasks program");
        System.out.println(ConsoleColors.GREEN_BOLD + "*NOTE* This program works properly only with .csv files, having three elements in a row");
        System.out.println(ConsoleColors.GREEN + "You will be given four options: adding new tasks to the list, displaying the list, removing elements from your tasks list and exiting from program while saving your changes. \n");
        System.out.println("Before we start, write down full name of your file containing list (at the end of the name of the file add extension - for example \".csv\". \n ");

        String [][] list;

        while (!checkIfFileExists) {
            fileName = scanner.nextLine();
            File file = new File(fileName);
            if (file.exists()) {
                checkIfFileExists=true;
            } else {
                System.out.println("There is no such file in your main directory, mind the full name of your file ");
            }
        }
        System.out.println("Do you want to make a copy of your file before starting? (\"yes\" or \"no\")");
        scan = scanner.nextLine();
        if (scan.equalsIgnoreCase("yes")) {
            copyFile(fileName);
        }
        list = tasks(fileName);
        do {
            System.out.println("\nChoose what do you want to do with your file: \n");
            System.out.println();
            System.out.println(ConsoleColors.GREEN + "add");
            System.out.println(ConsoleColors.RED + "remove");
            System.out.println(ConsoleColors.BLUE + "list");
            System.out.println(ConsoleColors.WHITE + "exit\n");

            scan = scanner.nextLine();

            System.out.println("\n" + ConsoleColors.PURPLE + scan);

            switch (scan) {
                case "add":
                    list = addNewTask(list, scanner);
                    break;
                case "remove":
                    list = removeTask(list, scanner);
                    break;
                case "list":
                    list(list);
                    break;
                case "exit":
                    break;
                default:
                    System.out.println("Choose one of the given options, please. ");
            }
        } while (!scan.equals("exit"));

        try {
            printWriter = new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < list[0].length; j++) {
                sb.append(list[i][j] + ",");
            }
            sb.setLength(sb.length() - 1);
            printWriter.println(sb.toString());
        }
        printWriter.close();
        System.out.println("Thank you for using this program, your changes have been saved");

    }

    public static String[][] tasks (String fileName) {

        String[] temp = new String[0];
        File file = new File (fileName);
        Scanner newScanner = null;
            try {
                newScanner = new Scanner(file);
                if (!file.exists()) {
                    System.out.println("There is no such file, mind the path to your file. ");
                    return null;
                }
                else {
                    while (newScanner.hasNextLine()) {
                        String scan2 = newScanner.nextLine();
                        temp = addNewElement(temp, scan2);
                    }
                }
            } catch(FileNotFoundException e) {
                System.out.println("File not found, mind the path to your file: ");
            }
        String[][] task = new String[temp.length][3];
            for (int i=0; i<temp.length; i++) {
                    String[] temp2 = temp[i].split(",");
                    for (int j=0; j<temp2.length; j++) {
                        task[i][j] = temp2[j];
                    }
                }
        return task;
    }

    public static String[] addNewElement(String[] tab, String element) {
        tab = Arrays.copyOf(tab, tab.length + 1);
        tab[tab.length - 1] = element;
        return tab;
    }

    public static void list(String [][] list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.length; i++) {
            sb.append("["+(i+1)+"]");
            for (int j = 0; j < list[i].length; j++) {
                sb.append(list[i][j] + ", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append("\n");
        }
        sb.setLength(sb.length() - 1);
        System.out.println(sb.toString());
    }

    public static String[][] addNewTask (String[][] list, Scanner scanner) {

        String[] temp = new String[0];
        String str;
        boolean check = false;
        try {
            while (!check) {
                StringBuilder sb = new StringBuilder();
                System.out.println("Write down your new task: ");
                str = scanner.nextLine();
                while (!scanner.hasNextLine()) {
                    str = scanner.nextLine();
                }
                sb.append(str);
                temp = addNewElement(temp, str);
                System.out.println("Write down a deadline date (YYYY-MM-DD): ");
                str = scanner.nextLine();
                while (!scanner.hasNextLine()) {
                    str = scanner.nextLine();
                }
                str = checkDate(str, scanner);
                sb.append(", " + str);
                temp = addNewElement(temp, str);
                System.out.println("Write down \"true\" if it's an important task or \"false\" if it is not: ");
                boolean importance=false;
                while (!importance) {
                    str = scanner.nextLine();
                    switch (str) {
                        case "true":
                            temp = addNewElement(temp, "True");
                            sb.append(", True");
                            importance=true;
                            break;
                        case "false":
                            temp = addNewElement(temp, "False");
                            sb.append(", False");
                            importance = true;
                            break;
                        default:
                            System.out.println("Write the correct input: \"true\" or \"false\"");
                    }
                }
                System.out.println("Is it the right input you wanted to add to the list of tasks?\n" + sb.toString());
                System.out.println("Write \"true\" if it's right or \"false\" if it isn't and you want to try one more time: ");

                boolean correct=false;
                while (!correct) {
                    str = scanner.nextLine();
                    if (str.equals("true")) {
                        correct = true;
                        check = true;
                    } else if (str.equals("false")) {
                        correct=true;
                    } else {
                        System.out.println("Write the correct input: \"true\" or \"false\"");
                    }
                }
            }
        } catch (IllegalArgumentException e) {
        System.out.println("Write down your input in a correct format:");
        scanner.nextLine();
        }

        String[][] newList = new String[list.length+1][temp.length];
        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < list[i].length; j++) {
                newList[i][j]=list[i][j];
            }
        }
        for (int i= list.length; i< newList.length; i++) {
            for (int j = 0; j < temp.length; j++) {
                newList[i][j] = temp[j];
            }
        }
        return newList;
    }


    public static String checkDate (String date, Scanner scan) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate inputDate = null;
        boolean datePast = false;
        String correct = "";
        do {
            while (!datePast) {
                try {
                    inputDate = LocalDate.parse(date, formatter);
                    datePast = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter a date in correct format (YYYY-MM-DD): ");
                    date = scan.nextLine();
                }
            }
            LocalDate currentDate = LocalDate.now();

            if (currentDate.isAfter(inputDate)) {
                System.out.println("The date you have given has already expired, do you want to add it to the list either way? \n\"yes\" or \"no\"");
                correct=scan.nextLine();
            } else {
                correct="yes";
            }
        } while(!correct.equalsIgnoreCase("yes"));
        return date;
    }

    public static String[][] removeTask (String[][] list, Scanner scanner) {

        int rows = list.length;
        int columns= list[0].length;
        boolean check=false;
        int rowRemove=0;

        System.out.println("Current state of tasks: ");
        list(list);
        System.out.print("Enter the number of the row to remove (1-" + rows + "): ");

        while(!check) {
            try {
                rowRemove = scanner.nextInt() - 1;
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Write down a number between given range");
            }
            if (rowRemove+1 <= rows) {
                check = true;
            } else {
                System.out.println("Write down a number between given range");
            }
        }
        String[][] newList = new String[rows - 1][columns];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            if (i != rowRemove) {
                for (int j = 0; j < columns; j++) {
                    newList[index][j] = list[i][j];
                }
                index++;
            }
        }
        scanner.nextLine();
        System.out.println("List with a removed row:");
        list(newList);

        return newList;
    }

    public static void copyFile(String fileName) {

        String secondFileNameWith;
        Path path1 = Paths.get(fileName);

        System.out.println("Making a copy...");
        String fileNameWithoutExt = "";
        if (fileName.contains(".")) {
            fileNameWithoutExt = fileName.substring(0, fileName.lastIndexOf("."));
            secondFileNameWith = fileNameWithoutExt + "(copy).csv";}
        else {
            secondFileNameWith = fileName + "(copy).csv";
        }
        Path path2 = Paths.get(secondFileNameWith);

        try {
            Files.copy(path1, path2);
        } catch (IOException e) {
            System.out.println("Something went wrong...");
        }
    }
}
