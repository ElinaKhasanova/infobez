import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class TestVec1 {
    private static ArrayList<int[]> ciphers;
    private static ArrayList<int[]> plainTexts;
    private static ArrayList<int[]> keys;

    @BeforeClass
    public static void init() {
        ParserVec1 parser = new ParserVec1("D:\\УНИВЕР УЧЕБА\\3\\инфобез\\semestr\\src\\main\\java\\9.2.3.Camellia.vectors1.txt");
        ciphers = parser.getCiphers();
        plainTexts = parser.getPlainTexts();
        keys = parser.getKeys();
    }

    @Test
    public void test() {
        int k = 0;
        boolean allCorrect = true;
        for (int i = 0; i < keys.size() && allCorrect; i++) {
            Camelia.keySchedule(keys.get(i));
            for (int j = 0; j < plainTexts.size() & allCorrect; j++) {
                if (!Arrays.equals(ciphers.get(k), Camelia.encode(plainTexts.get(j)))){
                    allCorrect = false;
                    System.out.println("Key: " + i + " " + "Text: " + j);
                }
                k++;
            }
        }
        Assert.assertTrue(allCorrect);
    }
}
