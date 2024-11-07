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
        this.player = new Player("Player");
        this.cpu = new Cpu("CPU");

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
            drawPhase(scanner);
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
    public void drawPhase(Scanner scanner) throws InterruptedException {
        player.draw(scanner, cards);
        cpu.draw(cards);
    }

    public void battlePhase() throws InterruptedException {
        player.judgeCardHand();

        cpu.judgeCardHand();

        System.out.println("バトル！！");
        player.attack(cpu);
        cpu.attack(player);

        System.out.println("PlayerのHPは" + player.hitPoint);
        System.out.println("CPUのHPは" + cpu.hitPoint);
    }
}
