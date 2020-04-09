import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ParserVec1 {

    private ArrayList<int[]> ciphers;
    private ArrayList<int[]> plainTexts;
    private ArrayList<int[]> keys;

    public ParserVec1(String filename) {
        ciphers = new ArrayList<>();
        plainTexts = new ArrayList<>();
        keys = new ArrayList<>();
        BufferedReader br = null;
        FileReader fr = null;

        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                stringToHexData(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<int[]> getCiphers() {
        return ciphers;
    }

    public ArrayList<int[]> getPlainTexts() {
        return plainTexts;
    }

    public ArrayList<int[]> getKeys() {
        return keys;
    }

    private void stringToHexData(String line) {
        String[] text = line.split(" ");
        if (text[0].equals("C") || text[0].equals("K") || text[0].equals("P")) {
            int n = 4;
            if (text.length == 19 + 8) n = 6;
            if (text.length == 19 + 16) n = 8;
            int[] data = new int[n];
            int i = 3;
            int k = 0;
            while (i < text.length) {
                String number = "";
                for (int j = 0; j < 4; j++) {
                    number += text[i++];
                }
                data[k] = (int) Long.parseLong(number, 16);
                k++;
            }
            if (text[0].equals("C")) {
                ciphers.add(data);
            }
            if (text[0].equals("K")) {
                keys.add(data);
            }
            if (text[0].equals("P") && plainTexts.size() < 128) {
                plainTexts.add(data);
            }
        }
    }
}
