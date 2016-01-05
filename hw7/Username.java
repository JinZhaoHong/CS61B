public class Username {

    // Potentially useless note: (int) '0' == 48, (int) 'a' == 97

    // Instance Variables (remember, they should be private!)
    private String username;
    // YOUR CODE HERE

    public Username() {
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

            return;
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
      // YOUR CODE HERE
    }

    public Username(String reqName) {
        if (reqName == null) {
            throw new NullPointerException("Requested username is null!");
        }

        if (reqName.length() != 2 && reqName.length() != 3) {
            throw new IllegalArgumentException("length of the reqName is not 2 or 3");
        }
         
        for (int i = 0; i < reqName.length(); i++) {
            int index = (int) reqName.charAt(i);
            if (index < 0 || index > 25) {
                throw new IllegalArgumentException("Contains illegal characters in the input");
            }
        }
        
        username = reqName;

        // YOUR CODE HERE
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Username) {
            // YOUR CODE HERE
            Username other = (Username) o;
            return this.hashCode() == other.hashCode(); 
        }
        return false;
    }

    @Override
    public int hashCode() { 
        // YOUR CODE HERE
        int hash = 0;
        for (int i = 0; i < username.length(); i ++) {
            int index = (int) username.charAt(i);
            hash += index;
        }
        return hash;
    }

    public static void main(String[] args) {
        // You can put some simple testing here.
    }
}