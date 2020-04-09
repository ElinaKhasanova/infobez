import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;

public class TestVec2 {

    private static ArrayList<int[]> ciphers;
    private static ArrayList<int[]> keys;
    private static int[] plainText;
    private static ArrayList<ArrayList<int[]>> functions;
    private static ArrayList<String> functionInfo;

    @BeforeClass
    public static void init() {
        ParserVec2 parser = new ParserVec2("D:\\УНИВЕР УЧЕБА\\3\\инфобез\\semestr\\src\\main\\java\\9.2.3.Camellia.vectors2.txt");
        ciphers = parser.getCiphers();
        plainText = parser.getPlainText();
        keys = parser.getKeys();
        functions = parser.getFunctions();
        functionInfo = parser.getFunctionInfo();
    }

    @Test
    public void testCipher() {
        boolean allCorrect = true;
        for (int i = 0; i < keys.size() && allCorrect; i++) {
            Camelia.keySchedule(keys.get(i));
            if (!Arrays.equals(ciphers.get(i), Camelia.encode(plainText))){
                allCorrect = false;
                System.out.println("Key: " + i);
            }
        }
        Assert.assertTrue(allCorrect);
    }

    @Test
    public void testF() {
        boolean allCorrect = true;
        for (int i = 0; i < functions.get(0).size() && allCorrect; i++) {
            int[] calc;
            if (functionInfo.get(i).equals(ParserVec2.F)) {
                calc = Camelia.F(functions.get(0).get(i), functions.get(1).get(i), 0);
            } else {
                if (functionInfo.get(i).equals(ParserVec2.FL)) {
                    calc = Camelia.FL(functions.get(0).get(i), functions.get(1).get(i), 0);
                }
                else calc = Camelia.FLinv(functions.get(0).get(i), functions.get(1).get(i), 0);
            }

            if (!Arrays.equals(calc, functions.get(2).get(i))) {
                allCorrect = false;
                System.out.println("Line: " + i);
            }
        }
        Assert.assertTrue(allCorrect);
    }
}
