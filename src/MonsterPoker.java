import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * MonsterPoker
 */
public class MonsterPoker {
    Scanner scanner = new Scanner(System.in);

    private Player player;
    private Cpu cpu;
    private List<Monster> cards;

    public MonsterPoker() {
        this.player = new Player("Player", scanner);
        this.cpu = new Cpu("CPU", scanner);

        this.cards = new java.util.ArrayList<>();
        cards.add(new Monster("スライム", 10, 40));
        cards.add(new Monster("サハギン", 20, 20));
        cards.add(new Monster("ドラゴン", 30, 25));
        cards.add(new Monster("デュラハン", 25, 15));
        cards.add(new Monster("シーサーペント", 30, 20));
    }

    public void run() throws InterruptedException {
        while (true) {
            drawPhase();
            battlePhase();
            if (player.getHitPoint() <= 0 && cpu.getHitPoint() <= 0) {
                System.out.println("引き分け！");
            } else if (player.getHitPoint() <= 0) {
                System.out.println("CPU Win!");
            } else if (cpu.getHitPoint() <= 0) {
                System.out.println("Player Win!");
            } else {
                Thread.sleep(2000);
                continue;
            }
            break;
        }
        this.scanner.close();
    }

    /**
     * 5枚のモンスターカードをプレイヤー/CPUが順に引く
     *
     * @throws InterruptedException
     */
    public void drawPhase() throws InterruptedException {
        player.draw(cards);
        cpu.draw(cards);
    }

    public void battlePhase() throws InterruptedException {
        player.handCheck();
        cpu.handCheck();
        System.out.println("バトル！！");
        this.attack(player, cpu);
        this.attack(cpu, player);
        System.out.println("PlayerのHPは" + player.getHitPoint());
        System.out.println("CPUのHPは" + cpu.getHitPoint());
    }

    public void attack(Player attacker, Player blocker) throws InterruptedException {
        System.out.printf("%sのDrawした", attacker.getName());

        for (Map.Entry<Monster, Integer> entry : attacker.handMap.entrySet()) {
            System.out.print(entry.getKey().getName() + " ");
            Thread.sleep(500);
        }
        System.out.print("の攻撃！");
        Thread.sleep(1000);
        if (blocker.getDefensePoint() >= attacker.getAttackPoint()) {
            System.out.printf("%sはノーダメージ！\n", blocker.getName());
        } else {
            double damage = attacker.getAttackPoint() - blocker.getDefensePoint();
            System.out.printf("%sは%.0fのダメージを受けた！\n", blocker.getName(), damage);
            blocker.setHitPoint(blocker.getHitPoint() - damage);
        }
    }
}


enum HandRank {
    SPECIAL_FIVE("スペシャルファイブ！", 10, 10), FIVE_OF_KIND("ファイブ！", 5, 5), FOUR_OF_KIND("フォー！", 4,
            4), FULL_HOUSE("フルハウス！", 3, 3), THREE_OF_KIND("スリーカード！", 3,
                    2), TWO_PAIR("ツーペア！", 2, 2), ONE_PAIR("ワンペア！", 0.5, 0.5);

    String name;
    double attackMultiplier;
    double defenseMultiplier;

    HandRank(String name, double attackMultiplier, double defenseMultiplier) {
        this.name = name;
        this.attackMultiplier = attackMultiplier;
        this.defenseMultiplier = defenseMultiplier;
    }
}
