// Adapted from Sierra and Bates, Head First Java

// You need not use all of the lines below, and you may use duplicates.
class LeapYear{
	static int year = 2000;
	public static void main (String[] args){
	if ((year % 100 != 0 && year % 4 == 0) || year % 400 == 0) {
    System.out.println (year + " is a leap year.");
}
    System.out.println (year + " is not a leap year.");
}
}