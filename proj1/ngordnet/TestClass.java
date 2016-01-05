package ngordnet;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestClass {

	@Test
	public void testSynsets(){
		WordNet wn = new WordNet("./wordnet/synsets11.txt", "./wordnet/hyponyms11.txt");
		System.out.println(wn.isNoun("jump"));
        System.out.println(wn.isNoun("leap"));
        System.out.println(wn.isNoun("nasal_decongestant"));

        assertEquals(true, wn.isNoun("jump"));
        assertEquals(true, wn.isNoun("leap"));
        assertEquals(true, wn.isNoun("nasal_decongestant"));


        System.out.println("All nouns:");
        for (String noun : wn.nouns()) {
            System.out.println(noun);
        }


        System.out.println("Hypnoyms of increase:");
        for (String noun : wn.hyponyms("increase")) {
            System.out.println(noun);
        }


        System.out.println("Hypnoyms of jump:");
        for (String noun : wn.hyponyms("jump")) {
            System.out.println(noun);
        }  



        System.out.println("Hypnoyms of change:");

        WordNet wn2 = new WordNet("./wordnet/synsets14.txt", "./wordnet/hyponyms14.txt");
        for (String noun : wn2.hyponyms("change")) {
            System.out.println(noun);
	}
}



	public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestClass.class);
    }
}
    
