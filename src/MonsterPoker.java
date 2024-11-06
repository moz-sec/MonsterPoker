import java.util.Scanner;

/**
 * MonsterPoker
 */
public class MonsterPoker {
    Player player = new Player("Player");
    Cpu cpu = new Cpu("CPU");

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
        // PlayerのDraw
        player.draw(scanner);


        // CPUのDraw
        cpu.draw();
    }

    public void battlePhase() throws InterruptedException {
        player.judgeYaku();

        cpu.judgeYaku();

        // バトル
        System.out.println("バトル！！");
        player.attack(cpu);

        cpu.attack(player);

        System.out.println("PlayerのHPは" + player.hitPoint);
        System.out.println("CPUのHPは" + cpu.hitPoint);
    }
}
