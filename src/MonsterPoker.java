import java.util.List;
import java.util.Scanner;

/**
 * MonsterPoker
 */
public class MonsterPoker {
    Player player;
    Cpu cpu;
    List<Monster> cards;

    public MonsterPoker() {
        Scanner scanner = new Scanner(System.in);
        this.player = new Player(0, 0, "Player", scanner);
        this.cpu = new Cpu(0, 0, "CPU", scanner);

        this.cards = new java.util.ArrayList<>();
        cards.add(new Monster("スライム", 10, 40));
        cards.add(new Monster("サハギン", 20, 20));
        cards.add(new Monster("ドラゴン", 30, 25));
        cards.add(new Monster("デュラハン", 25, 15));
        cards.add(new Monster("シーサーペント", 30, 20));
    }

    public void run() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);// 標準入力
        while (true) {
            drawPhase();
            battlePhase();
            if (player.hitPoint <= 0 && cpu.hitPoint <= 0) {
                System.out.println("引き分け！");
            } else if (player.hitPoint <= 0) {
                System.out.println("CPU Win!");
            } else if (cpu.hitPoint <= 0) {
                System.out.println("Player Win!");
            } else {
                Thread.sleep(2000);
                continue;
            }
            break;
        }
        scanner.close();
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
        player.attack(cpu);
        cpu.attack(player);
        System.out.println("PlayerのHPは" + player.hitPoint);
        System.out.println("CPUのHPは" + cpu.hitPoint);
    }
}

enum HandRank {
    SPECIAL_FIVE("スペシャルファイブ！", 10, 10),
    FIVE_OF_KIND("ファイブ！", 5, 5),
    FOUR_OF_KIND("フォー！", 4, 4),
    FULL_HOUSE("フルハウス！", 3, 3),
    THREE_OF_KIND("スリーカード！", 3, 2),
    TWO_PAIR("ツーペア！", 2, 2),
    ONE_PAIR("ワンペア！", 0.5, 0.5);

    String name;
    double attackMultiplier;
    double defenseMultiplier;

    HandRank(String name, double attackMultiplier, double defenseMultiplier) {
        this.name = name;
        this.attackMultiplier = attackMultiplier;
        this.defenseMultiplier = defenseMultiplier;
    }
}
