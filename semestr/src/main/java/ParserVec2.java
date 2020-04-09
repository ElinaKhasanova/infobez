import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ParserVec2 {

    private ArrayList<int[]> ciphers;
    private ArrayList<int[]> keys;
    private int[] plainText;
    private ArrayList<ArrayList<int[]>> functions;
    private ArrayList<String> functionInfo;

    public static final String F = "Ffunc";
    public static final String FL = "FL";
    public static final String FL_INV = "FLinv";

    public ParserVec2(String filename) {
        ciphers = new ArrayList<>();
        keys = new ArrayList<>();
        functions = new ArrayList<>();
        functionInfo = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            functions.add(new ArrayList<>());
        }
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

    public ArrayList<String> getFunctionInfo() {
        return functionInfo;
    }

    private void stringToHexData(String line) {
        String[] text = line.split(" ");
        int data[];
        int k;
        if (text[0].equals("PT") || text[0].equals("KEY")
                || text[0].equals("CT")) {
            k = 2;
            int pairs = 2;
            if (text[0].equals("KEY")) {
                if (text.length == 5) pairs = 3;
                if (text.length == 6) pairs = 4;
            } else {
                if (text.length == 5) k++;
            }
            data = new int[pairs * 2];
            for (int i = 0; i < pairs; i++) {
                data[(i + 1) * 2 - 2] = (int) Long.parseLong(text[k].substring(0, 8), 16);
                data[(i + 1) * 2 - 1] = (int) Long.parseLong(text[k].substring(8, 16), 16);
                k++;
            }
            if (text[0].equals("PT")) {
                plainText = data;
            }
            if (text[0].equals("KEY")) {
                keys.add(data);
            }
            if (text[0].equals("CT")) {
                ciphers.add(data);
            }
        }
        if (text[0].equals("Ffunc") || text[0].equals("FL") || text[0].equals("FLinv")) {
            k = 1;
            if (text.length > 4) {
                k = 4;
            }
            for (int i = k; i < text.length; i++) {
                data = new int[2];
                String num = text[i].substring(2);
                data[0] = (int) Long.parseLong(num.substring(0, 8), 16);
                data[1] = (int) Long.parseLong(num.substring(8, 16), 16);
                functions.get(i - k).add(data);
            }
            functionInfo.add(text[0]);
        }
    }

    public ArrayList<int[]> getCiphers() {
        return ciphers;
    }

    public ArrayList<int[]> getKeys() {
        return keys;
    }

    public int[] getPlainText() {
        return plainText;
    }

    public ArrayList<ArrayList<int[]>> getFunctions() {
        return functions;
    }
}
