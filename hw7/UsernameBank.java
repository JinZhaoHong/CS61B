import java.util.Map;
import java.util.HashMap;

public class UsernameBank {

    // Instance variables (remember, they should be private!)
    // YOUR CODE HERE
    private HashMap<String, String> nameAndEmail;
    private HashMap<String, String> emailAndName;
    private HashMap<String, Integer> badUsernames;
    private HashMap<String, Integer> badEmails;

    public UsernameBank() {
        // YOUR CODE HERE
        nameAndEmail = new HashMap<String, String>();
        emailAndName = new HashMap<String, String>();

    }


    public void generateUsername(String username, String email) {
        // YOUR CODE HERE
        if (username == null || email == null) {
            throw new NullPointerException("either username or email is null");
        }

        if (username.length() != 2 && username.length() != 3) {
            if (badUsernames.containsKey(username)) {
                int times = badUsernames.get(username);
                times += 1;
                badUsernames.put(username, times);
            } else {
                badUsernames.put(username, 1);
            } 
            throw new IllegalArgumentException("length of the reqName is not 2 or 3");
        }
         
        for (int i = 0; i < username.length(); i++) {
            int index = (int) username.charAt(i);
            if (index < 0 || index > 25) {
                if (badUsernames.containsKey(username)) {
                    int times = badUsernames.get(username);
                    times += 1;
                    badUsernames.put(username, times);
                } else {
                    badUsernames.put(username, 1);
                } 
                throw new IllegalArgumentException("Contains illegal characters in the input");
            }
        }
        
        //Adapted from tutorialspoint
        //http://www.tutorialspoint.com/javaexamples/regular_email.htm
        String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (!email.matches(EMAIL_REGEX)) {
            if (badEmails.containsKey(email)) {
                int times = badEmails.get(email);
                times += 1;
                badEmails.put(email, times);
            } else {
                badEmails.put(email, 1);
            }
            throw new IllegalArgumentException("Invalid email address");
        }

        if (nameAndEmail.containsKey(username)) {
            throw new IllegalArgumentException("Username already exist");
        }

        nameAndEmail.put(username, email);
        emailAndName.put(email, username);
    }

    public String getEmail(String username) {
        // YOUR CODE HERE
        return nameAndEmail.get(username);
    }

    public String getUsername(String userEmail)  {
        // YOUR CODE HERE
        return emailAndName.get(userEmail);
    }

    public Map<String, Integer> getBadEmails() {
        return badEmails;
    }

    public Map<String, Integer> getBadUsernames() {
        return badUsernames;
    }

    public String suggestUsername() {
        String username = new String();
        double choseTwoOrThree = Math.random();

        //generate two alphanumeric characters
        if (choseTwoOrThree < 0.5) {
            double randomAgain = Math.random();
            //generate the first as number
            if (randomAgain < 0.5) {
                int first = (int) (9 * Math.random());
                username = username + first;
            } else {
                char first = (char) ((int) Math.random());
                username = username + first;
            }

            // the second
            double randomAgainAgain = Math.random();
            //generate the second as number
            if (randomAgainAgain < 0.5) {
                int second = (int) (9 * Math.random());
                username = username + second;
            } else {
                char second = (char) ((int) Math.random());
                username = username + second;
            }

            return username;
        }

        //generate three alphanumeric characters
        if (choseTwoOrThree >= 0.5) {
            double random1 = Math.random();
            //generate the first as number
            if (random1 < 0.5) {
                int first = (int) (9 * Math.random());
                username = username + first;
            } else {
                char first = (char) ((int) Math.random());
                username = username + first;
            }

            // the second
            double random2 = Math.random();
            //generate the second as number
            if (random2 < 0.5) {
                int second = (int) (9 * Math.random());
                username = username + second;
            } else {
                char second = (char) ((int) Math.random());
                username = username + second;
            }

            // the third
            double random3 = Math.random();
            //generate the second as number
            if (random3 < 0.5) {
                int third = (int) (9 * Math.random());
                username = username + third; 
            } else {
                char third = (char) ((int) Math.random());
                username = username + third;
            }

        }
        return username;
    }

    // The answer is somewhere in between 3 and 1000.
    public static final int followUp() {
        // YOUR CODE HERE
        return 32;
    }

    // Optional, suggested method. Use or delete as you prefer.
    private void recordBadUsername(String username) {
        // YOUR CODE HERE
    }

    // Optional, suggested method. Use or delete as you prefer.
    private void recordBadEmail(String email) {
        // YOUR CODE HERE
    }
}
