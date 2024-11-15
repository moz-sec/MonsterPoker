import java.util.Map;

public class CalculatePoints {
    double attackPoint;
    double defensePoint;

    CalculatePoints(double attackPoint, double defensePoint) {
        this.attackPoint = attackPoint;
        this.defensePoint = defensePoint;
    }

    public void calculatePoint(HandRank handRank, Map<Monster, Integer> handMap) throws InterruptedException {
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

    public void battle(Player attacker, Player blocker) {
        if (blocker.defensePoint >= attacker.attackPoint) {
            System.out.printf("%sはノーダメージ！\n", blocker.name);
        } else {
            double damage = attacker.attackPoint - blocker.defensePoint;
            System.out.printf("%sは%.0fのダメージを受けた！\n", blocker.name, damage);
            blocker.hitPoint = blocker.hitPoint - damage;
        }
    }
}
