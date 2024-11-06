import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/*
 * Player
 */
public class Player {
    public List<Monster> monsters = new ArrayList<>();
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

    public Player() {
        monsters.add(new Monster("スライム", 10, 40));
        monsters.add(new Monster("サハギン", 20, 20));
        monsters.add(new Monster("ドラゴン", 30, 25));
        monsters.add(new Monster("デュラハン", 25, 15));
        monsters.add(new Monster("シーサーペント", 30, 20));
    }

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

        calculatePoint();
    }

    private void calculatePoint() {
        for (int i = 0; i < this.yaku.length; i++) {
            if (this.yaku[i] >= 1) {
                this.AttackPoint += monsters.get(i).ap * this.yaku[i];
                this.DefencePoint += monsters.get(i).dp * this.yaku[i];
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
                System.out.print(this.monsters.get(i).name + " ");
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
            int monsterIndex = this.deck[i];
            System.out.printf("%s ", this.monsters.get(monsterIndex).name);
        }
        System.out.println();
    }
}
