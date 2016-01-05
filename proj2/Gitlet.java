//
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
* @author Zhaohong Jin
*/


public class Gitlet {


    public static void main(String[] args) {
        Git git = tryLoadingGit();
        if (git == null) {
            git = new Git();
        }
        if (args.length == 0) {
            return;
        }
        File f = new File(".gitlet/");
        if (!args[0].equals("init") && !f.exists()) {
            System.out.println("you need to initialize gitlet before doing anything!");
            return;
        } 
        String key = args[0];
        String[] command = new String[args.length - 1];
        System.arraycopy(args, 1, command, 0, args.length - 1);
        checkConditions(key, git, command);      
        saveGit(git);
    }

    private static void checkConditions(String key, Git git, String[] command) {
        switch (key) {
            case "init":
                git.initialize();
                break;
            case "add":
                git.add(command[0]);
                break;
            case "commit":
                git.commit(command[0]);
                break;
            case "rm":
                git.remove(command[0]);
                break;
            case "log":
                git.log();
                break;
            case "global-log":
                git.globallog();
                break;
            case "find":
                git.find(command[0]);
                break;
            case "status":
                git.status();
                break;
            case "branch":
                git.branch(command[0]);
                break;
            case "checkout":
                if (warningConfirmed()) {
                    if (command.length == 1) {
                        HashMap<String, Node<HashSet<File>>> bMap = git.getBranchMap();
                        if (bMap.containsKey(command[0])) {
                            git.checkout(command[0]);
                        } else {
                            File file = new File(command[0]);
                            git.checkout(file);
                        }
                    } else {
                        git.checkout(Integer.parseInt(command[0]), command[1]);
                    }
                }
                break;
            case "rm-branch":
                git.removeBranch(command[0]);
                break;
            case "reset":
                if (warningConfirmed()) {
                    git.reset(Integer.parseInt(command[0]));
                }
                break;
            case "merge":
                if (warningConfirmed()) {
                    git.merge(command[0]);
                }
                break;
            case "rebase":
                if (warningConfirmed()) {
                    git.rebase(command[0]);
                }
                break;
            case "i-rebase":
                if (warningConfirmed()) {
                    git.interactiveRebase(command[0]);
                }
                break;
            default :
                System.out.println("please type in valid commands");
        }
    }

    //inspiration from Sarah's code
    private static Git tryLoadingGit() {
        Git git = null;
        File gitFile = new File(".gitlet/git.ser");
        if (gitFile.exists()) {
            try {
                FileInputStream fileIn = new FileInputStream(gitFile);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                git = (Git) objectIn.readObject();
                objectIn.close();

            } catch (IOException e) {
                String msg = "IOException while loading Git.";
                System.out.println(msg);
            } catch (ClassNotFoundException e) {
                String msg = "ClassNotFoundException while loading Git.";
                System.out.println(msg);
            }
        }
        return git;

    }

    private static void saveGit(Git git) {
        if (git == null) {
            return;
        }
        try {
            File gitFile = new File(".gitlet/git.ser");
            FileOutputStream fileout = new FileOutputStream(gitFile);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileout);
            objectOut.writeObject(git);
            objectOut.close();
        } catch (IOException e) {
            String msg = "IOException while saving git.";
            System.out.println(msg);
        }
    }

    //if it is a dangerous method, it needs to prompt out a warning message first
    private static boolean warningConfirmed() {
        String warningMessage = "Warning: The command you entered may alter the files in "; 
        warningMessage += "your working directory. Uncommitted changes may be lost. "; 
        warningMessage += "Are you sure you want to continue? (yes/no)";
        System.out.println(warningMessage);
        Scanner sc = new Scanner(System.in);
        String s = sc.next();
        if (s.equals("yes")) {
            return true;
        }
        return false;
    }
}
