import java.util.Random;
import java.util.Scanner;

class Player {
    String monsters[] = {"スライム", "サハギン", "ドラゴン", "デュラハン", "シーサーペント"};
    int monsterAp[] = {10, 20, 30, 25, 30};
    int monsterDp[] = {40, 20, 25, 15, 20};
    String changeCard = new String();
    boolean five = false;
    boolean four = false;
    boolean three = false;
    int pair = 0;
    int one = 0;

    Random card = new Random();

    double hitPoint = 1000;
    int yaku[] = new int[5];
    int deck[] = new int[5];

    double AttackPointRate = 1;
    double DefencePointRate = 1;
    double AttackPoint = 0;
    double DefencePoint = 0;

    public void draw(Scanner scanner) throws InterruptedException {
        System.out.println("PlayerのDraw！");
        for (int i = 0; i < this.deck.length; i++) {
            this.deck[i] = card.nextInt(5);
        }
        this.printCard("[Player]");

        // カードの交換
        System.out.println("カードを交換する場合は1から5の数字（左から数えた位置を表す）を続けて入力してください．交換しない場合は0と入力してください");
        String exchange = scanner.nextLine();
        if (exchange.charAt(0) != '0') {
            for (int i = 0; i < exchange.length(); i++) {
                this.deck[Character.getNumericValue(exchange.charAt(i)) - 1] = card.nextInt(5);
            }
            this.printCard("[Player]");

            System.out
                    .println("もう一度カードを交換する場合は1から5の数字（左から数えた位置を表す）を続けて入力してください．交換しない場合は0と入力してください");
            exchange = scanner.nextLine();
            if (exchange.charAt(0) != '0') {
                for (int i = 0; i < exchange.length(); i++) {
                    this.deck[Character.getNumericValue(exchange.charAt(i)) - 1] = card.nextInt(5);
                }
                this.printCard("[Player]");
            }
        }
    }

    public void judgeYaku() throws InterruptedException {
        // 役判定用配列の初期化
        for (int i = 0; i < this.yaku.length; i++) {
            this.yaku[i] = 0;
        }
        // モンスターカードが何が何枚あるかをcpuYaku配列に登録
        for (int i = 0; i < this.deck.length; i++) {
            this.yaku[this.deck[i]]++;
        }
        // 役判定
        // 5が1つある：ファイブ
        // 4が1つある：フォー
        // 3が1つあり，かつ，2が1つある：フルハウス
        // 2が2つある：ツーペア
        // 3が1つある：スリー
        // 2が1つある：ペア
        // 1が5つある：スペシャルファイブ
        // 初期化
        five = false;
        four = false;
        three = false;
        pair = 0; // pair数を保持する
        one = 0;// 1枚だけのカードの枚数
        // 手札ごとのcpuYaku配列の作成
        for (int i = 0; i < this.yaku.length; i++) {
            if (yaku[i] == 1) {
                one++;
            } else if (yaku[i] == 2) {
                pair++;
            } else if (yaku[i] == 3) {
                three = true;
            } else if (yaku[i] == 4) {
                four = true;
            } else if (yaku[i] == 5) {
                five = true;
            }
        }

        this.AttackPointRate = 1;// 初期化
        this.DefencePointRate = 1;
        if (one == 5) {
            System.out.println("スペシャルファイブ！AP/DPは両方10倍！");
            this.AttackPointRate = 10;
            this.DefencePointRate = 10;
        } else if (five == true) {
            System.out.println("ファイブ！AP/DPは両方5倍！");
            this.AttackPointRate = 5;
            this.DefencePointRate = 5;
        } else if (four == true) {
            System.out.println("フォー！AP/DPは両方4倍！");
            this.AttackPointRate = 3;
            this.DefencePointRate = 3;
        } else if (three == true && pair == 1) {
            System.out.println("フルハウス！AP/DPは両方3倍");
            this.AttackPointRate = 3;
            this.DefencePointRate = 3;
        } else if (three == true) {
            System.out.println("スリーカード！AP/DPはそれぞれ3倍と2倍");
            this.DefencePointRate = 2;
            this.AttackPointRate = 3;
        } else if (pair == 2) {
            System.out.println("ツーペア！AP/DPは両方2倍");
            this.AttackPointRate = 2;
            this.DefencePointRate = 2;
        } else if (pair == 1) {
            System.out.println("ワンペア！AP/DPは両方1/2倍");
            this.AttackPointRate = 0.5;
            this.DefencePointRate = 0.5;
        }
        Thread.sleep(1000);

        // APとDPの計算
        for (int i = 0; i < this.yaku.length; i++) {
            if (this.yaku[i] >= 1) {
                this.AttackPoint = this.AttackPoint + this.monsterAp[i] * this.yaku[i];
                this.DefencePoint = this.DefencePoint + this.monsterDp[i] * this.yaku[i];
            }
        }
        this.AttackPoint = this.AttackPoint * this.AttackPointRate;
        this.DefencePoint = this.DefencePoint * this.DefencePointRate;
    }

    public void attack(Cpu cpu) throws InterruptedException {
        // Playerの攻撃
        System.out.print("PlayerのDrawした");
        for (int i = 0; i < this.yaku.length; i++) {
            if (this.yaku[i] >= 1) {
                System.out.print(this.monsters[i] + " ");
                Thread.sleep(500);
            }
        }
        System.out.print("の攻撃！");
        Thread.sleep(1000);
        System.out.println("CPUのモンスターによるガード！");
        if (cpu.DefencePoint >= this.AttackPoint) {
            System.out.println("CPUはノーダメージ！");
        } else {
            double damage = this.AttackPoint - cpu.DefencePoint;
            System.out.printf("CPUは%.0fのダメージを受けた！\n", damage);
            cpu.hitPoint = cpu.hitPoint - damage;
        }
    }

    public void printCard(String name) {
        System.out.print(name);
        for (int i = 0; i < this.deck.length; i++) {
            System.out.printf("%s ", this.monsters[this.deck[i]]);
        }
        System.out.println();
    }

    public double getPlayerHp() {
        return this.hitPoint;
    }

    public double getplayerAttackPointRate() {
        return this.AttackPointRate;
    }

    public void setplayerAttackPointRate(double playerAttackPointRate) {
        this.AttackPointRate = playerAttackPointRate;
    }

    public double getplayerDefencePointRate() {
        return this.DefencePointRate;
    }

    public void setplayerDefencePointRate(double playerDefencePointRate) {
        this.DefencePointRate = playerDefencePointRate;
    }

    public double getplayerAttackPoint() {
        return this.AttackPoint;
    }

    public void setplayerAttackPoint(double playerAttackPoint) {
        this.AttackPoint = playerAttackPoint;
    }
}


class Cpu extends Player {
    public int exchangeCards[] = new int[5];

    public void draw() throws InterruptedException {
        System.out.println("CPUのDraw！");
        for (int i = 0; i < this.deck.length; i++) {
            this.deck[i] = card.nextInt(5);
        }
        this.printCard("[CPU]");

        // 交換するカードの決定
        this.decideExchangeCard();

        // 交換するカード番号の表示
        this.printExchangeCardNumber();

        // // カードの交換
        this.exchangeCard();

        // 交換するカードの決定
        this.decideExchangeCard();

        // 交換するカード番号の表示
        this.printExchangeCardNumber();

        // カードの交換
        this.exchangeCard();
    }

    private void printExchangeCardNumber() {
        this.changeCard = "";
        for (int i = 0; i < this.exchangeCards.length; i++) {
            if (this.exchangeCards[i] == 1) {
                this.changeCard = this.changeCard + (i + 1);
            }
        }
        if (this.changeCard.length() == 0) {
            this.changeCard = "0";
        }
        System.out.println(this.changeCard);
    }

    private void exchangeCard() {
        if (this.changeCard.charAt(0) != '0') {
            for (int i = 0; i < this.changeCard.length(); i++) {
                this.deck[Character.getNumericValue(this.changeCard.charAt(i)) - 1] =
                        this.card.nextInt(5);
            }
            this.printCard("[CPU]");
        }
    }

    private void decideExchangeCard() throws InterruptedException {
        System.out.println("CPUが交換するカードを考えています・・・・・・");
        Thread.sleep(2000);
        // cpuDeckを走査して，重複するカード以外のカードをランダムに交換する
        // 0,1,0,2,3 といったcpuDeckの場合，2枚目，4枚目，5枚目のカードをそれぞれ交換するかどうか決定し，例えば24といった形で決定する
        // 何番目のカードを交換するかを0,1で持つ配列の初期化
        // 例えばexchangeCards[]が{0,1,1,0,0}の場合は2,3枚目を交換の候補にする
        for (int i = 0; i < this.exchangeCards.length; i++) {
            this.exchangeCards[i] = -1;
        }
        for (int i = 0; i < this.deck.length; i++) {
            if (this.exchangeCards[i] == -1) {
                for (int j = i + 1; j < this.deck.length; j++) {
                    if (this.deck[i] == this.deck[j]) {
                        this.exchangeCards[i] = 0;
                        this.exchangeCards[j] = 0;
                    }
                }
                if (this.exchangeCards[i] != 0) {
                    this.exchangeCards[i] = this.card.nextInt(2);// 交換するかどうかをランダムに最終決定する
                    // this.exchangeCards[i] = 1;
                }
            }
        }
    }

    public void attack(Player player) throws InterruptedException {
        System.out.print("CPUのDrawした");
        for (int i = 0; i < this.yaku.length; i++) {
            if (this.yaku[i] >= 1) {
                System.out.print(this.monsters[i] + " ");
                Thread.sleep(500);
            }
        }
        System.out.print("の攻撃！");
        Thread.sleep(1000);
        System.out.println("Playerのモンスターによるガード！");
        if (player.DefencePoint >= this.AttackPoint) {
            System.out.println("Playerはノーダメージ！");
        } else {
            double damage = this.AttackPoint - player.DefencePoint;
            System.out.printf("Playerは%.0fのダメージを受けた！\n", damage);
            player.hitPoint = player.hitPoint - damage;
        }

        System.out.println("PlayerのHPは" + player.hitPoint);
        System.out.println("CPUのHPは" + this.hitPoint);
    }
}


/**
 * MonsterPoker
 */
public class MonsterPoker {
    Player player = new Player();
    Cpu cpu = new Cpu();

    public void run() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);// 標準入力
        while (true) {
            drawPhase(scanner);
            battlePhase();
            if (player.getPlayerHp() <= 0 && cpu.getPlayerHp() <= 0) {
                System.out.println("引き分け！");
            } else if (player.getPlayerHp() <= 0) {
                System.out.println("CPU Win!");
            } else if (cpu.getPlayerHp() <= 0) {
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
    }
}
