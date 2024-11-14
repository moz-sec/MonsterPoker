import java.util.Map;

public class CalcPoints {
    double attackPoint;
    double defensePoint;

    CalcPoints(double attackPoint, double defensePoint) {
        this.attackPoint = attackPoint;
        this.defensePoint = defensePoint;
    }

    public void calculatePoint(HandRank handRank, Map<Monster, Integer> handMap) {
        if (handMap == null) {
            System.out.println("ぬるぽ");
        }
        for (Map.Entry<Monster, Integer> entry : handMap.entrySet()) {
            Monster monster = entry.getKey();
            int count = entry.getValue();

            this.attackPoint += monster.ap * count;
            this.defensePoint += monster.dp * count;
        }
        this.attackPoint *= handRank.attackMultiplier;
        this.defensePoint *= handRank.defenseMultiplier;
        // System.out.println("====================");
        // System.out.println(this.attackPoint);
        // System.out.println(this.defensePoint);
        // System.out.println("====================");
    }
}
