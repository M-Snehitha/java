import java.io.FileReader;
import java.math.BigInteger;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ShamirSecretSharing {

    public static void main(String[] args) {
        try {
     
            JSONObject testCase1 = (JSONObject) new JSONParser().parse(new FileReader("testcase1.json"));
            JSONObject testCase2 = (JSONObject) new JSONParser().parse(new FileReader("testcase2.json"));

          
            BigInteger secret1 = findSecret(testCase1);
            BigInteger secret2 = findSecret(testCase2);

          
            System.out.println("Secret for Test Case 1: " + secret1);
            System.out.println("Secret for Test Case 2: " + secret2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  
    private static BigInteger findSecret(JSONObject testCase) {
        JSONObject keys = (JSONObject) testCase.get("keys");
        int n = ((Long) keys.get("n")).intValue();
        int k = ((Long) keys.get("k")).intValue();

   
        List<Point> points = getPoints(testCase);


        return lagrangeInterpolation(points, k);
    }

    
    private static List<Point> getPoints(JSONObject data) {
        List<Point> points = new ArrayList<>();
        for (Object key : data.keySet()) {
            if (!"keys".equals(key)) {
                int x = Integer.parseInt((String) key);
                JSONObject pointData = (JSONObject) data.get(key);
                int base = Integer.parseInt((String) pointData.get("base"));
                BigInteger y = new BigInteger((String) pointData.get("value"), base);
                points.add(new Point(x, y));
            }
        }
        return points;
    }

    
    private static BigInteger lagrangeInterpolation(List<Point> points, int k) {
        BigInteger constantTerm = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger xi = BigInteger.valueOf(points.get(i).x);
            BigInteger yi = points.get(i).y;

            BigInteger term = yi;
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger xj = BigInteger.valueOf(points.get(j).x);

                    
                    term = term.multiply(xj.negate()).divide(xi.subtract(xj));
                }
            }
            constantTerm = constantTerm.add(term);
        }

        return constantTerm;
    }

    
    static class Point {
        int x;
        BigInteger y;

        Point(int x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }
}
