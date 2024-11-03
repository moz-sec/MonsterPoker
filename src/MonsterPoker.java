import java.util.Random;
import java.util.Scanner;

/**
 * MonsterPoker
 */
public class MonsterPoker {

    Random card = new Random();

    double playerHitPoint = 1000;
    double cpuHitPoint = 1000;
    int playerDeck[] = new int[5];
    int cpuDeck[] = new int[5];
    String monsters[] = {"スライム", "サハギン", "ドラゴン", "デュラハン", "シーサーペント"};
    int monsterAp[] = {10, 20, 30, 25, 30};
    int monsterDp[] = {40, 20, 25, 15, 20};
    int cpuExchangeCards[] = new int[5];
    String changeCard = new String();
    int playerYaku[] = new int[5];
    int cpuYaku[] = new int[5];
    double playerAttackPointRate = 1;
    double playerDefencePointRate = 1;
    double playerAttackPoint = 0;
    double playerDefencePoint = 0;
    double cpuAttackPointRate = 1;
    double cpuDefencePointRate = 1;
    double cpuAttackPoint = 0;
    double cpuDefencePoint = 0;
    boolean five = false;
    boolean four = false;
    boolean three = false;
    int pair = 0;
    int one = 0;

    /**
     * 5枚のモンスターカードをプレイヤー/CPUが順に引く
     *
     * @throws InterruptedException
     */
    public void drawPhase(Scanner scanner) throws InterruptedException {
        // 初期Draw
        System.out.println("PlayerのDraw！");
        for (int i = 0; i < playerDeck.length; i++) {
            this.playerDeck[i] = card.nextInt(5);
        }
        printPlayerCard();

        // カードの交換
        System.out.println("カードを交換する場合は1から5の数字（左から数えた位置を表す）を続けて入力してください．交換しない場合は0と入力してください");
        String exchange = scanner.nextLine();
        if (exchange.charAt(0) != '0') {
            for (int i = 0; i < exchange.length(); i++) {
                this.playerDeck[Character.getNumericValue(exchange.charAt(i)) - 1] =
                        card.nextInt(5);
            }
            printPlayerCard();

            System.out
                    .println("もう一度カードを交換する場合は1から5の数字（左から数えた位置を表す）を続けて入力してください．交換しない場合は0と入力してください");
            exchange = scanner.nextLine();
            if (exchange.charAt(0) != '0') {
                for (int i = 0; i < exchange.length(); i++) {
                    this.playerDeck[Character.getNumericValue(exchange.charAt(i)) - 1] =
                            card.nextInt(5);
                }
                printPlayerCard();
            }
        }

        System.out.println("CPUのDraw！");
        for (int i = 0; i < cpuDeck.length; i++) {
            this.cpuDeck[i] = card.nextInt(5);
        }
        printCpuCard();


        // 交換するカードの決定
        System.out.println("CPUが交換するカードを考えています・・・・・・");
        Thread.sleep(2000);
        // cpuDeckを走査して，重複するカード以外のカードをランダムに交換する
        // 0,1,0,2,3 といったcpuDeckの場合，2枚目，4枚目，5枚目のカードをそれぞれ交換するかどうか決定し，例えば24といった形で決定する
        // 何番目のカードを交換するかを0,1で持つ配列の初期化
        // 例えばcpuExchangeCards[]が{0,1,1,0,0}の場合は2,3枚目を交換の候補にする
        for (int i = 0; i < this.cpuExchangeCards.length; i++) {
            this.cpuExchangeCards[i] = -1;
        }
        for (int i = 0; i < this.cpuDeck.length; i++) {
            if (this.cpuExchangeCards[i] == -1) {
                for (int j = i + 1; j < this.cpuDeck.length; j++) {
                    if (this.cpuDeck[i] == this.cpuDeck[j]) {
                        this.cpuExchangeCards[i] = 0;
                        this.cpuExchangeCards[j] = 0;
                    }
                }
                if (this.cpuExchangeCards[i] != 0) {
                    this.cpuExchangeCards[i] = this.card.nextInt(2);// 交換するかどうかをランダムに最終決定する
                    // this.cpuExchangeCards[i] = 1;
                }
            }
        }

        // 交換するカード番号の表示
        this.changeCard = "";
        for (int i = 0; i < cpuExchangeCards.length; i++) {
            if (this.cpuExchangeCards[i] == 1) {
                this.changeCard = this.changeCard + (i + 1);
            }
        }
        if (this.changeCard.length() == 0) {
            this.changeCard = "0";
        }
        System.out.println(this.changeCard);

        // カードの交換
        if (changeCard.charAt(0) != '0') {
            for (int i = 0; i < changeCard.length(); i++) {
                this.cpuDeck[Character.getNumericValue(changeCard.charAt(i)) - 1] = card.nextInt(5);
            }
            printCpuCard();
        }

        // 交換するカードの決定
        System.out.println("CPUが交換するカードを考えています・・・・・・");
        Thread.sleep(2000);
        // cpuDeckを走査して，重複するカード以外のカードをランダムに交換する
        // 0,1,0,2,3 といったcpuDeckの場合，2枚目，4枚目，5枚目のカードをそれぞれ交換するかどうか決定し，例えば24といった形で決定する
        // 何番目のカードを交換するかを0,1で持つ配列の初期化
        // 例えばcpuExchangeCards[]が{0,1,1,0,0}の場合は2,3枚目を交換の候補にする
        for (int i = 0; i < this.cpuExchangeCards.length; i++) {
            this.cpuExchangeCards[i] = -1;
        }
        for (int i = 0; i < this.cpuDeck.length; i++) {
            if (this.cpuExchangeCards[i] == -1) {
                for (int j = i + 1; j < this.cpuDeck.length; j++) {
                    if (this.cpuDeck[i] == this.cpuDeck[j]) {
                        this.cpuExchangeCards[i] = 0;
                        this.cpuExchangeCards[j] = 0;
                    }
                }
                if (this.cpuExchangeCards[i] != 0) {
                    this.cpuExchangeCards[i] = this.card.nextInt(2);// 交換するかどうかをランダムに最終決定する
                    // this.cpuExchangeCards[i] = 1;
                }
            }
        }

        // 交換するカード番号の表示
        this.changeCard = "";
        for (int i = 0; i < cpuExchangeCards.length; i++) {
            if (this.cpuExchangeCards[i] == 1) {
                this.changeCard = this.changeCard + (i + 1);
            }
        }
        if (this.changeCard.length() == 0) {
            this.changeCard = "0";
        }
        System.out.println(this.changeCard);

        // カードの交換
        if (changeCard.charAt(0) != '0') {
            for (int i = 0; i < changeCard.length(); i++) {
                this.cpuDeck[Character.getNumericValue(changeCard.charAt(i)) - 1] = card.nextInt(5);
            }
            printCpuCard();
        }
    }

    public void battlePhase() throws InterruptedException {
        // Playerの役の判定
        // 役判定用配列の初期化
        for (int i = 0; i < playerYaku.length; i++) {
            this.playerYaku[i] = 0;
        }
        // モンスターカードが何が何枚あるかをplayerYaku配列に登録
        for (int i = 0; i < playerDeck.length; i++) {
            this.playerYaku[this.playerDeck[i]]++;
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
        // 手札ごとのplayerYaku配列の作成
        for (int i = 0; i < playerYaku.length; i++) {
            if (playerYaku[i] == 1) {
                one++;
            } else if (playerYaku[i] == 2) {
                pair++;
            } else if (playerYaku[i] == 3) {
                three = true;
            } else if (playerYaku[i] == 4) {
                four = true;
            } else if (playerYaku[i] == 5) {
                five = true;
            }
        }

        // 役の判定
        System.out.println("Playerの役は・・");
        this.playerAttackPointRate = 1;// 初期化
        this.playerDefencePointRate = 1;
        if (one == 5) {
            System.out.println("スペシャルファイブ！AP/DPは両方10倍！");
            this.playerAttackPointRate = 10;
            this.playerDefencePointRate = 10;
        } else if (five == true) {
            System.out.println("ファイブ！AP/DPは両方5倍！");
            this.playerAttackPointRate = 5;
            this.playerDefencePointRate = 5;
        } else if (four == true) {
            System.out.println("フォー！AP/DPは両方4倍！");
            this.playerAttackPointRate = 3;
            this.playerDefencePointRate = 3;
        } else if (three == true && pair == 1) {
            System.out.println("フルハウス！AP/DPは両方3倍");
            this.playerAttackPointRate = 3;
            this.playerDefencePointRate = 3;
        } else if (three == true) {
            System.out.println("スリーカード！AP/DPはそれぞれ3倍と2倍");
            this.playerAttackPointRate = 3;
            this.playerDefencePointRate = 2;
        } else if (pair == 2) {
            System.out.println("ツーペア！AP/DPは両方2倍");
            this.playerAttackPointRate = 2;
            this.playerDefencePointRate = 2;
        } else if (pair == 1) {
            System.out.println("ワンペア！AP/DPは両方1/2倍");
            this.playerAttackPointRate = 0.5;
            this.playerDefencePointRate = 0.5;
        }
        Thread.sleep(1000);

        // APとDPの計算
        for (int i = 0; i < playerYaku.length; i++) {
            if (playerYaku[i] >= 1) {
                this.playerAttackPoint = this.playerAttackPoint + this.monsterAp[i] * playerYaku[i];
                this.playerDefencePoint =
                        this.playerDefencePoint + this.monsterDp[i] * playerYaku[i];
            }
        }
        this.playerAttackPoint = this.playerAttackPoint * this.playerAttackPointRate;
        this.playerDefencePoint = this.playerDefencePoint * this.playerDefencePointRate;

        // CPUの役の判定
        // 役判定用配列の初期化
        for (int i = 0; i < cpuYaku.length; i++) {
            this.cpuYaku[i] = 0;
        }
        // モンスターカードが何が何枚あるかをcpuYaku配列に登録
        for (int i = 0; i < cpuDeck.length; i++) {
            this.cpuYaku[this.cpuDeck[i]]++;
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
        for (int i = 0; i < cpuYaku.length; i++) {
            if (cpuYaku[i] == 1) {
                one++;
            } else if (cpuYaku[i] == 2) {
                pair++;
            } else if (cpuYaku[i] == 3) {
                three = true;
            } else if (cpuYaku[i] == 4) {
                four = true;
            } else if (cpuYaku[i] == 5) {
                five = true;
            }
        }

        // 役の判定
        System.out.println("CPUの役は・・");
        this.cpuAttackPointRate = 1;// 初期化
        this.cpuDefencePointRate = 1;
        if (one == 5) {
            System.out.println("スペシャルファイブ！AP/DPは両方10倍！");
            this.cpuAttackPointRate = 10;
            this.cpuDefencePointRate = 10;
        } else if (five == true) {
            System.out.println("ファイブ！AP/DPは両方5倍！");
            this.cpuAttackPointRate = 5;
            this.cpuDefencePointRate = 5;
        } else if (four == true) {
            System.out.println("フォー！AP/DPは両方4倍！");
            this.cpuAttackPointRate = 3;
            this.cpuDefencePointRate = 3;
        } else if (three == true && pair == 1) {
            System.out.println("フルハウス！AP/DPは両方3倍");
            this.cpuAttackPointRate = 3;
            this.cpuDefencePointRate = 3;
        } else if (three == true) {
            System.out.println("スリーカード！AP/DPはそれぞれ3倍と2倍");
            this.cpuAttackPointRate = 3;
            this.cpuDefencePointRate = 2;
        } else if (pair == 2) {
            System.out.println("ツーペア！AP/DPは両方2倍");
            this.cpuAttackPointRate = 2;
            this.cpuDefencePointRate = 2;
        } else if (pair == 1) {
            System.out.println("ワンペア！AP/DPは両方1/2倍");
            this.cpuAttackPointRate = 0.5;
            this.cpuDefencePointRate = 0.5;
        }
        Thread.sleep(1000);

        // APとDPの計算
        for (int i = 0; i < cpuYaku.length; i++) {
            if (cpuYaku[i] >= 1) {
                this.cpuAttackPoint = this.cpuAttackPoint + this.monsterAp[i] * cpuYaku[i];
                this.cpuDefencePoint = this.cpuDefencePoint + this.monsterDp[i] * cpuYaku[i];
            }
        }
        this.cpuAttackPoint = this.cpuAttackPoint * this.cpuAttackPointRate;
        this.cpuDefencePoint = this.cpuDefencePoint * this.cpuDefencePointRate;

        // バトル
        System.out.println("バトル！！");
        // Playerの攻撃
        System.out.print("PlayerのDrawした");
        for (int i = 0; i < playerYaku.length; i++) {
            if (playerYaku[i] >= 1) {
                System.out.print(this.monsters[i] + " ");
                Thread.sleep(500);
            }
        }
        System.out.print("の攻撃！");
        Thread.sleep(1000);
        System.out.println("CPUのモンスターによるガード！");
        if (this.cpuDefencePoint >= this.playerAttackPoint) {
            System.out.println("CPUはノーダメージ！");
        } else {
            double damage = this.playerAttackPoint - this.cpuDefencePoint;
            System.out.printf("CPUは%.0fのダメージを受けた！\n", damage);
            this.cpuHitPoint = this.cpuHitPoint - damage;
        }

        // CPUの攻撃
        System.out.print("CPUのDrawした");
        for (int i = 0; i < cpuYaku.length; i++) {
            if (cpuYaku[i] >= 1) {
                System.out.print(this.monsters[i] + " ");
                Thread.sleep(500);
            }
        }
        System.out.print("の攻撃！");
        Thread.sleep(1000);
        System.out.println("Playerのモンスターによるガード！");
        if (this.playerDefencePoint >= this.cpuAttackPoint) {
            System.out.println("Playerはノーダメージ！");
        } else {
            double damage = this.cpuAttackPoint - this.playerDefencePoint;
            System.out.printf("Playerは%.0fのダメージを受けた！\n", damage);
            this.playerHitPoint = this.playerHitPoint - damage;
        }

        System.out.println("PlayerのHPは" + this.playerHitPoint);
        System.out.println("CPUのHPは" + this.cpuHitPoint);

    }

    public void printCpuCard() {
        System.out.print("[CPU]");

        for (int i = 0; i < cpuDeck.length; i++) {
            System.out.printf("%s ", this.monsters[cpuDeck[i]]);
        }
        System.out.println();
    }

    public void printPlayerCard() {
        System.out.print("[Player]");
        for (int i = 0; i < playerDeck.length; i++) {
            System.out.printf("%s ", this.monsters[playerDeck[i]]);
        }
        System.out.println();
    }

    public double getPlayerHp() {
        return this.playerHitPoint;
    }

    public double getCpuHp() {
        return this.cpuHitPoint;
    }

    public double getplayerAttackPointRate() {
        return this.playerAttackPointRate;
    }

    public void setplayerAttackPointRate(double playerAttackPointRate) {
        this.playerAttackPointRate = playerAttackPointRate;
    }

    public double getplayerDefencePointRate() {
        return this.playerDefencePointRate;
    }

    public void setplayerDefencePointRate(double playerDefencePointRate) {
        this.playerDefencePointRate = playerDefencePointRate;
    }

    public double getplayerAttackPoint() {
        return this.playerAttackPoint;
    }

    public void setplayerAttackPoint(double playerAttackPoint) {
        this.playerAttackPoint = playerAttackPoint;
    }

    public double getcpuAttackPointRate() {
        return this.cpuAttackPointRate;
    }

    public void setcpuAttackPointRate(double cpuAttackPointRate) {
        this.cpuAttackPointRate = cpuAttackPointRate;
    }

    public double getcpuDefencePointRate() {
        return this.cpuDefencePointRate;
    }

    public void setcpuDefencePointRate(double cpuDefencePointRate) {
        this.cpuDefencePointRate = cpuDefencePointRate;
    }

}
