
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.Date;
import java.util.Arrays;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.FileChannel;

/**
* @author Zhaohong Jin
*/



public class Git implements Serializable {
    // a linked list to store the chain of commits in git.
    private Node<HashSet<File>> commitChain;   
    // a pointer to track the linked list we are currently at.
    private Node<HashSet<File>> headPointer;
    /*
     * store the pointer with its corresponding commit eg: fisrt commit, key =
     * 1, return the pointer pointing to the right place of the list [1] --->
     * [2] ---> [3] ----> [4] ---^ head of the first commit
     */
    private HashMap<Integer, Node<HashSet<File>>> pointerMap;
    private HashMap<Node<HashSet<File>>, Integer> reversedPointerMap;
    // the list contains added files (git add [file]);
    // clean the entire addMark after each commit.
    private HashSet<File> addMark = new HashSet<File>();
    // the list contains files on the remove list(git remove [file]);
    //clean the entire removeMark after each commit.
    private HashSet<File> removeMark = new HashSet<File>();
    // the list contains files that has been committed(files in the .gitlet directory);
    private HashSet<File> commitMark = new HashSet<File>();
    //keep tracking of the ith number of commit.
    private int commitCount;
    //everything ever committed...Handy for globalLog and find.
    private ArrayList<Node<HashSet<File>>> nodes = new ArrayList<Node<HashSet<File>>>();
    //should be master when initialized
    private String currentBranch;
    // all the branches ever created, with its headPointer
    private HashMap<String, Node<HashSet<File>>> branchMap
        = new HashMap<String, Node<HashSet<File>>>();
    
    private static final int BITS = 80;

    /*
     * Usage: java Gitlet init Description: Creates a new gitlet version control
     * system in the current directory. This system will automatically start
     * with one commit: a commit that contains no files and has the commit
     * message initial commit. Runtime: Should be constant with relative to any
     * significant measure. Failure cases: If there is already a gitlet version
     * control system in the current directory, it should abort. It should NOT
     * overwrite the existing system with a new one. Should print the error
     * message A gitlet version control system already exists in the current
     * directory. Dangerous?: No
     */

    public void initialize() {
        File f = new File(".gitlet/");
        //check if f has already existed
        if (f.exists()) {
            System.out.println(
                "A gitlet version control system already exists in the current directory.");
            return;
        }
        //create a master branch upon initialization.
        currentBranch = "master";  
        //this is the first commit 
        commitCount = 0;
        //pyhsically create the file ".gitlet/"
        f.mkdirs();
        //update the commit date;
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd ' ' hh:mm:ss");
        String time = ft.format(date);
        //put the file into the linked list(Node). No file for initialize.
        HashSet<File> files = new HashSet<File>();
        commitChain = new Node<HashSet<File>>(
            files, "initial commit", time, commitCount, currentBranch, null);
        //point the head pointer to the entire list
        //create a unique pointer for this commit only.
        headPointer = commitChain;
        Node<HashSet<File>> localPointer = commitChain;
        //store the number of commit and the LOCALpointer;
        pointerMap = new HashMap<Integer, Node<HashSet<File>>>();
        reversedPointerMap = new HashMap<Node<HashSet<File>>, Integer>();
        pointerMap.put(commitCount, localPointer);
        reversedPointerMap.put(localPointer, commitCount);
        //only handy for globallog.. and find
        nodes.add(localPointer);
    }

    //check whether two files have the same content.
    //code inspired from 
    //http://javaonlineguide.net/2014/10/compare-two-files-in-java-example-code.html
    private boolean checkEquals(File f1, File f2) throws IOException {
        FileInputStream fis1 = new FileInputStream(f1);
        FileInputStream fis2 = new FileInputStream(f2);
        if (f1.length() == f2.length()) {
            int n = 0;
            byte[] b1;
            byte[] b2;
            while ((n = fis1.available()) > 0) {
                if (n > BITS) {
                    n = BITS;
                }
                b1 = new byte[n];
                b2 = new byte[n];
                int res1 = fis1.read(b1);
                int res2 = fis2.read(b2);
                if (!Arrays.equals(b1, b2)) {
                    fis1.close();
                    fis2.close();
                    return false;
                }
            }
        } else {
            fis1.close();
            fis2.close();
            return false; // length is not matched.
        }
        fis1.close();
        fis2.close(); 
        return true;
    }


    private File translateTo(File f) {
       if (f.getParent() == null) {
           return new File(".gitlet/" + commitCount + "#" + f.getName());
       }
       String returnString = ".gitlet/" + f.getParent() + "/" + commitCount + "#" + f.getName();
       File fileTo = new File(returnString);
       return fileTo;
    }

    //find the working directory by translate a .gitlet directory
    //into a working directory
    private File translateBack(File f) {
        if (f.getParent().equals(".gitlet")) {
            return new File(f.getName().split("#")[1]);
        }
        String returnString = f.getParent().substring(8, f.getParent().length());
        returnString = returnString + "/" + f.getName().split("#")[1];
        File fileBack = new File(returnString);
        return fileBack;
    }


    /*
     * Usage: java Gitlet add [file name] Description:
     */
    public void add(String addFile) {
        File f = new File(addFile);
        if (!f.exists()) {
            System.err.println("File does not exist.");
            return;
        }
        //If the file had been marked for removal, instead just unmark it.
        if (removeMark.contains(f)) {
            removeMark.remove(f);
            return;
        }
        //check whether a file has been modified or not form the last commit
        for (File f1 : commitMark) {
            try {
                File nameChecker = translateTo(f);
                if (checkEquals(f1, f) 
                    && f1.getPath().equals(nameChecker.getPath())) {
                    System.err.println("File has not been modified since the last commit.");
                    return;
                }
            } catch (IOException e) {
                System.out.println("Unable to checkEquals.");
            }
        }
        addMark.add(f);

    }

    //copy the hashset<File> not by pointer but by the entire list;
    //this is essential in storing each set of files in each commit;
    public HashSet<File> copyHashSet(HashSet<File> origin) {
        HashSet<File> copy = new HashSet<File>();
        for (File f : origin) {
            //File newFile = new File(
                //".gitlet/" + f.getName().split("#")[0] + "#" + f.getName().split("#")[1]);
            copy.add(f);
        }
        return copy;
    }

    /*
     * Usage: java Gitlet commit [message]
     */
    
    public void commit(String message) {
        //super buggy right now. didn't even handle remove mark and any changes
        //made to the file that may affect previous commit in the linked list.
        if (addMark.isEmpty() && removeMark.isEmpty()) {
            System.err.println("No changes added to the commit.");
            return;
        }
        //increment commitCount by 1;
        commitCount += 1;
        //this part copies the files in the working directory into the .gitlet
        commitFiles();
        //update the commit date;
        String time = recordTime();     
        //append the newest committed files to the head and 
        // update the headpointer to point to the head of the Node.
        //take a snapshot of the current commitMark and store it into the node.
        HashSet<File> commitMarkCopy = copyHashSet(commitMark);
        commitChain = new Node<HashSet<File>>(
            commitMarkCopy, message, time, commitCount, currentBranch, commitChain);
        headPointer = commitChain;
        //create a local pointer and its id number to capture this commit.
        //update the pointer map
        Node<HashSet<File>> localPointer = commitChain;
        pointerMap.put(commitCount, localPointer);
        reversedPointerMap.put(localPointer, commitCount);
        //only handy for globallog..and find
        nodes.add(localPointer);
        //clean up the add cache and remove cache
        addMark = new HashSet<File>();
        removeMark = new HashSet<File>();
        //update the branchMap
        //branchMap.put(currentBranch, headPointer);
    }


    //this part copies the files in the working directory into the .gitlet
    public void commitFiles() {
        //add the files committed into the commit chain
        // into commitMark
        // also, copy the files into the .gitlet
        for (File f : addMark) {
            File destFile = translateTo(f);
            try {
                copyFile(f, destFile);
            } catch (IOException e) {
                System.err.println("unable to copy files.");
            }
        }
        //update commit, also checks for duplicate
        //also check for duplicates;
        HashSet<File> commitMarkToBeRemoved = new HashSet<File>();
        HashSet<File> commitMarkToBeAdded = new HashSet<File>();
        for (File f : commitMark) {
            File modified = translateBack(f);
            if (addMark.contains(modified)) {
                commitMarkToBeRemoved.add(f);
                addMark.remove(modified);
                commitMarkToBeAdded.add(translateTo(modified));
            } 
        }
        for (File f : commitMarkToBeRemoved) {
            commitMark.remove(f);
        }
        for (File f : commitMarkToBeAdded) {
            commitMark.add(f);
        }

        for (File f : addMark) {
            File destFile = translateTo(f);
            commitMark.add(destFile);
        }

        //delete the files pointer that shows up in the removeMark
        HashSet<File> toBeRemoved = new HashSet<File>();
        for (File f : commitMark) {
            File modified = translateBack(f);
            if (removeMark.contains(modified)) {
                toBeRemoved.add(modified);
            } 
        }
        for (File f : toBeRemoved) {
            deepRemove(f);
        }
    }

    private void deepRemove(File f) {
        for (int i = 1; i <= commitCount; i++) {
            if (f.getParent() == null) {
                File ff = new File(".gitlet/" + i + "#" + f.getName());
                if (commitMark.contains(ff)) {
                    commitMark.remove(ff);
                    return;
                }
            }
            String returnString = ".gitlet/" + f.getParent() + "/" + i + "#" + f.getName();
            File fileTo = new File(returnString);
            if (commitMark.contains(fileTo)) {
                commitMark.remove(fileTo);
            }
        }
    }


    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (destFile.getParent() != null) {
            File folder = new File(destFile.getParent());
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
    //record the current time..
    public String recordTime() {
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd ' ' hh:mm:ss");
        String time = ft.format(date);  
        return time;   
    }

    /*
     * remove
     * 
     * Usage: java Gitlet rm [file name] 
     */
    public void remove(String removeFile) {
        File f = new File(removeFile);
        File newF = translateTo(f);
        if (!f.exists()) {
            System.err.println("File does not exist.");
            return;
        }
        if (!addMark.contains(f) && !commitMark.contains(newF)) {
            System.err.println("No reason to remove the file.");
            return;
        }
        //If the file had been staged, instead unstage it.
        if (addMark.contains(f)) {
            addMark.remove(f);
            return;
        }
        removeMark.add(f);
    }



    public void log() {
        //stateoftheBranchMap();
        Node<HashSet<File>> localPointer = headPointer;
        while (localPointer != null) {
            System.out.println("====");
            System.out.println("Commit " + reversedPointerMap.get(localPointer));
            System.out.println(localPointer.time);
            System.out.println(localPointer.message);
            System.out.println("");
            localPointer = localPointer.tail;
        }
    }
    

    public void globallog() {
        for (Node<HashSet<File>> n : nodes) {
            System.out.println("====");
            System.out.println("Commit " + reversedPointerMap.get(n));
            System.out.println(n.time);
            System.out.println(n.message);
            System.out.println("");
        }
    }


    public void find(String m) {
        ArrayList<Integer> cache = new ArrayList<Integer>();
        for (Node<HashSet<File>> n : nodes) {
            if (n.message.equals(m)) {
                cache.add(reversedPointerMap.get(n));
                System.out.println(reversedPointerMap.get(n));
            }
        }
        if (cache.isEmpty()) {
            System.err.println("Found no commit with that message.");
        }

    }


    public void status() {
        System.out.println("=== Branches ===");
        System.out.println("*" + currentBranch);
        Set<String> branches = branchMap.keySet();
        for (String s : branches) {
            if (!s.equals(currentBranch)) {
                System.out.println(s);
            }
        }
        System.out.println(" ");
        System.out.println("=== Staged Files ===");
        for (File f : addMark) {
            System.out.println(f);
        }
        System.out.println(" ");
        System.out.println("=== Files Marked for Removal ===");
        for (File f : removeMark) {
            System.out.println(f);
        }
        System.out.println(" ");
    }


    //rewrite the file in the working directory based on the record in the .gitlet
    //note, each filename in each snapshot of the commit is unique
    //if we overwrite a file, we renew the filename and remove the old filename
    public void checkout(File file) {
        HashSet<File> fileSet = headPointer.head;
        // change the file name to match the file contained in the .gitlet
        for (File f : fileSet) {
            File modifiedFile = translateBack(f);
            if (modifiedFile.equals(file)) {
                try {
                    copyFile(f, file);
                } catch (IOException e) {
                    System.err.println("unable to copy files.");
                }
                return;
            }
        }
        System.err.println(
            "File does not exist in the most recent commit, or no such branch exists.");
    }


    //rewrite the file in the working directory based on the record in the .gitlet
    public void checkout(int id, String filePath) {
        if (!pointerMap.containsKey(id)) {
            System.err.println("No commit with that id exists.");
            return;
        }
        File file = new File(filePath);
        HashSet<File> fileSet = pointerMap.get(id).head;
        for (File f : fileSet) {
            File modifiedFile = translateBack(f);
            if (modifiedFile.equals(file)) {
                try {
                    copyFile(f, file);
                } catch (IOException e) {
                    System.err.println("unable to copy files.");
                }
                return;
            }
        }
        System.err.println("File does not exist in that commit.");
    }

    //debug method
    public void stateoftheBranchMap() {
        System.out.println("");
        System.out.println(">>>>>>>>>>>>>>>>>>state of the branch Map");
        Set<String> keys = branchMap.keySet();
        for (String k : keys) {
            System.out.println(" ");
            System.out.println("Branch : " + k);
            HashSet<File> h = branchMap.get(k).head;
            for (File f : h) {
                System.out.println(f);
            }
        }
    }

    //this will automatically copy the entire set of file in the new branch.
    public void checkout(String branchName) {
        if (!branchMap.containsKey(branchName)) {
            System.err.println(
                "File does not exist in the most recent commit, or no such branch exists.");
            return;
        }
        if (currentBranch.equals(branchName)) {
            System.err.println("No need to checkout the current branch.");
            return;
        }
        //store the information of the original and update the 
        //headpointer of this branch to the new branch
        Node<HashSet<File>> oldHeadPointer = headPointer;
        branchMap.put(currentBranch, oldHeadPointer);
        currentBranch = branchName;
        //update commitMark and 
        headPointer = branchMap.get(branchName);
        commitMark = copyHashSet(headPointer.head);
        //update commitChain
        commitChain = headPointer;
        //update all the files in the working directory
        for (File f : commitMark) {
            File newPath = translateBack(f);
            checkout(newPath); 
        }
    }

    public void branch(String branchName) {
        if (branchMap.containsKey(branchName)) {
            System.err.println("A branch with that name already exists.");
            return;
        }
        Node<HashSet<File>> newBranchHead = commitChain;
        branchMap.put(branchName, newBranchHead);
    }

    //delete the headpointer of the branch from the branchMap
    public void removeBranch(String branchName) {
        if (!branchMap.containsKey(branchName)) {
            System.err.println("A branch with that name does not exist.");
            return;
        }
        if (currentBranch.equals(branchName)) {
            System.err.println("Cannot remove the current branch.");
            return;
        }
        branchMap.remove(branchName);
    }


    public void reset(int id) {
        if (!pointerMap.containsKey(id)) {
            System.err.println("No commit with that id exists.");
            return;
        }
        headPointer = pointerMap.get(id);
        commitMark = copyHashSet(headPointer.head);
        currentBranch = headPointer.branch;
        //update commitChain
        commitChain = headPointer;
        for (File f : commitMark) {
            File newPath = translateBack(f);
            checkout(newPath);
        }
    }

    //note, merge only changes the files in the working directory
    //if you want to save it, you still have to go through regular add and commit.
    public void merge(String branchName) {
        if (!branchMap.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (branchName.equals(currentBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        Node<HashSet<File>> pointer1 = headPointer;
        Node<HashSet<File>> pointer2 = branchMap.get(branchName);
        Node<HashSet<File>> splitNode = findSplittingNode(pointer1, pointer2);
        // there is nothing to merge if pointer1 and pointer2 is pointing
        // to the same node
        if ((pointer1.id == pointer2.id)) {
            return;
        }
        // files that pointer1 committed after split
        HashSet<File> pointer1Changed = checkChanged(splitNode.head, pointer1.head);
        // files that pointer2 committed after split
        HashSet<File> pointer2Changed = checkChanged(splitNode.head, pointer2.head);
        // files that both pointer1 and pointer2 committed (and both changes);  
        HashSet<File> conflict = conflictVersion(pointer1Changed, pointer2Changed);
        // modified pointer1Changed and pointer2Changed so that 
        // they contain changed files that only belong to their own node
        for (File f : conflict) {
            if (pointer1Changed.contains(f)) {
                pointer1Changed.remove(f);
            }
            if (pointer2Changed.contains(f)) {
                pointer2Changed.remove(f);
            }
        }
        finishUpMerge(branchName, pointer2Changed, conflict); 
    }


    //continue merging (break up the method for style concerns);
    private void finishUpMerge(String branchName, 
        HashSet<File> pointer2Changed, HashSet<File> conflict) {
        //this part handles updating not modified files in the current
        //branch to the version in the given branch
        for (File f2 : pointer2Changed) {
            //add(WORKINGDIRECTORY + f2Name);
            // could be buggy because of the route of the file;
            try {
                copyFile(f2, translateBack(f2));
            } catch (IOException e) {
                System.err.println("unable to copy.");
            }
        }

        //this part handles conflict files
        for (File f : conflict) {
            if (!headPointer.head.contains(f)) {
                String returnString = f.getParent().substring(8, f.getParent().length());
                returnString = returnString + "/" + f.getName().split("#")[1] + ".conflicted";
                try {
                    copyFile(f, new File(returnString));
                } catch (IOException e) {
                    System.err.println("unable to copy.");
                }   
            }
        }
        //commit("merge " + branchName);
    }

    //if there is not splitting node, return null;
    private Node<HashSet<File>> findSplittingNode(Node<HashSet<File>> node1, 
        Node<HashSet<File>> node2) {
        while (node1.id != node2.id) {
            node1 = node1.tail;
            node2 = node2.tail;
            if (node1 == null || node2 == null) {
                return null;
            }
        }
        return node1;
    }


    // return the modified files after the splitting node;
    // if no such modified files, return null;
    private HashSet<File> checkChanged(HashSet<File> splitNodeFiles, HashSet<File> files) {
        HashSet<File> changed = new HashSet<File>();
        for (File f : files) {
            if (!splitNodeFiles.contains(f)) {
                changed.add(f);
            }
        }
        return changed;
    }

 
    // return the conflict versions bwtween two files. 
    private HashSet<File> conflictVersion(HashSet<File> files1, HashSet<File> files2) {
        HashSet<File> conflictSet = new HashSet<File>();
        for (File f1 : files1) {
            for (File f2 : files2) {
                //if two files have the same original name but have different
                //content, then we say that they are conflict files.
                try {
                    if (translateBack(f1).getPath().equals(translateBack(f2).getPath()) && !checkEquals(f1, f2)) {
                        conflictSet.add(f1);
                        conflictSet.add(f2);
                    }
                } catch (IOException e) {
                    System.err.println("unable to checkEquals.");
                }
            }
        }
        return conflictSet;
    }




    public void rebase(String branchName) {
        //check conditions
        if (!branchMap.containsKey(branchName)) {
            System.err.println("A branch with that name does not exist.");
            return;
        }
        if (branchName.equals(currentBranch)) {
            System.err.println("Cannot rebase a branch onto itself.");
            return;
        }
        Node<HashSet<File>> header = headPointer;
        Node<HashSet<File>> other = branchMap.get(branchName);
        // if other branch is the history of this branch
        Node<HashSet<File>> headCopy = header;
        while (headCopy != null) {
            if (headCopy.id == other.id) {
                System.err.println("Already up-to-date.");
                return;
            }
            headCopy = headCopy.tail;
        }
        // if this branch is the history of the other branch
        //update the headpointer and also update the files
        Node<HashSet<File>> headCopy2 = header;
        Node<HashSet<File>> otherCopy = other;
        while (otherCopy != null) {
            if (headCopy2.id == otherCopy.id) {
                headPointer = other;
                commitMark = copyHashSet(other.head);
                for (File f : commitMark) {
                    File newPath = translateBack(f);
                    checkout(newPath);
                }
                return; 
            }
            otherCopy = otherCopy.tail;
        }

        Node<HashSet<File>> splitNode = findSplittingNode(header, other);
        
        //the update the headpointer
        Node<HashSet<File>> snappedOff = getSnappedOffBranch(splitNode, header, branchName);
        //now start to append things on the other node
        String oldBranch = currentBranch;
        checkout(branchName);
        //notice that all the replayed commit should belong to the original branch,
        //so we arbuiturarily change the branchname to be the original branch;
        currentBranch = oldBranch;

        while (snappedOff != null) {
            replay(snappedOff, splitNode, snappedOff.message);
            snappedOff = snappedOff.tail;
        }
        
        
    }


    //replay the newNode and put it on the tail of the the current branch
    private void replay(
        Node<HashSet<File>> newNode, Node<HashSet<File>> splitNode, String message) {
        //this changes the current branch to be the branch of the newNode
        HashSet<File> headCopy = newNode.head;
        // this part is files that has been changed in the current branch
        //after split. This is supposed to just stay as they are in the current
        //branch even if there are conflicts in the branch tobe attached.
        HashSet<File> headerChanged = checkChanged(splitNode.head, headCopy);

        for (File f : headerChanged) {
            try {
                copyFile(f, translateBack(f));
            } catch (IOException e) {
                System.err.println("unable to copy.");
            }
            add(translateBack(f).getPath());
        }
        commit(newNode.message);
    }



    // get the chopped-off node. Also update the branch of the node.
    // note that the node is in a reversed order
    private Node<HashSet<File>> getSnappedOffBranch(Node<HashSet<File>> splitNode, 
        Node<HashSet<File>> ori, String otherBranch) {
        Node<HashSet<File>> snapped = null;
        while (splitNode.id != ori.id) {
            snapped = new Node<HashSet<File>>(ori.head, 
                ori.message, ori.time, ori.id, otherBranch, snapped);
            ori = ori.tail;
        }
        return snapped;
    }




    public void interactiveRebase(String branchName) {
        if (!branchMap.containsKey(branchName)) {
            System.err.println("A branch with that name does not exist.");
            return;
        }
        if (branchName.equals(currentBranch)) {
            System.err.println("Cannot rebase a branch onto itself.");
            return;
        }
        Node<HashSet<File>> header = headPointer;
        Node<HashSet<File>> other = branchMap.get(branchName);
        Node<HashSet<File>> headCopy = header;
        while (headCopy != null) {
            if (headCopy.id == other.id) {
                System.err.println("Already up-to-date.");
                return;
            }
            headCopy = headCopy.tail;
        }
        Node<HashSet<File>> headCopy2 = header;
        Node<HashSet<File>> otherCopy = other;
        while (otherCopy != null) {
            if (headCopy2.id == otherCopy.id) {
                headPointer = other;
                commitMark = copyHashSet(other.head);
                for (File f : commitMark) {
                    File newPath = translateBack(f);
                    checkout(newPath);
                }
                return; 
            }
            otherCopy = otherCopy.tail;
        }
        Node<HashSet<File>> splitNode = findSplittingNode(header, other);    
        Node<HashSet<File>> snappedOff = getSnappedOffBranch(splitNode, header, branchName);
        String oldBranch = currentBranch;
        checkout(branchName);
        currentBranch = oldBranch;
        boolean firstNode = true;
        while (snappedOff != null) {
            System.out.println("Currently replaying: ");
            displayInformation(snappedOff);
            String m = "Would you like to (c)ontinue, ";
            m = m + "(s)kip this commit, or change this commit's (m)essage?";
            System.out.println(m);
            // this part handles the user input;
            Scanner sc = new Scanner(System.in);
            char input = sc.next().charAt(0);
            if (input == 'c') {
                replay(snappedOff, splitNode, snappedOff.message);
                snappedOff = snappedOff.tail;
                firstNode = false;
            }
            if (input == 's') {
                if (firstNode || snappedOff.tail != null) {
                    System.out.println("you can not skip the initial & final commit");
                } else {
                    snappedOff = snappedOff.tail;
                    firstNode = false; 
                }
            }
            if (input == 'm') {
                System.out.println("Please enter a new message for this commit.");
                Scanner scc = new Scanner(System.in);
                String newMessage = scc.nextLine();
                replay(snappedOff, splitNode, newMessage);
                snappedOff = snappedOff.tail;
                firstNode = false;
            }
        }
    }

    
    // output the useful info about this log.
    private void displayInformation(Node<HashSet<File>> snappedOff) {
        System.out.println("======");
        System.out.println("Commit ID: " + snappedOff.id);
        System.out.println("Commit Message: " + snappedOff.message);
        System.out.println("Commit Time: " + snappedOff.time);
        System.out.println(" ");
    }

    // this is to check whether checkout is refering to a branch
    //or refering to a file
    public HashMap<String, Node<HashSet<File>>> getBranchMap() {
        return branchMap;
    }
}
/* ==========================================
 * the end of the regular part of the project
 * ==========================================
 */

