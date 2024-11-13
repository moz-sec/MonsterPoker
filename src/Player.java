import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/*
 * Player
 */
public class Player {
    String name;
    Monster[] deck = new Monster[5];
    Random random;
    Scanner scanner;

    // 手札のカードとそれぞれの枚数
    // 例) スライム:2, サハギン:1, ドラゴン:2
    Map<Monster, Integer> handMap = new HashMap<>();

    double hitPoint;
    double attackPoint;
    double defensePoint;

    public Player(String name, Scanner scanner) {
        this.name = name;
        this.hitPoint = 1000;
        this.random = new Random();
        this.scanner = scanner;
    }

    public void draw(List<Monster> cards) throws InterruptedException {
        System.out.println("PlayerのDraw！");
        IntStream.range(0, this.deck.length)
                .forEach(i -> {
                    deck[i] = cards.get(this.random.nextInt(cards.size()));
                    // テストコード
                    // deck[i] = cards.get(2);
                });
        this.printCard();

        this.promptCardExchange(cards);
    }

    public void promptCardExchange(List<Monster> cards) {
        for (int i = 0; i < 2; i++) {
            System.out.println(
                    i == 0 ? "カードを交換する場合は1から5の数字（左から数えた位置を表す）を続けて入力してください．交換しない場合は0と入力してください"
                            : "もう一度カードを交換する場合は1から5の数字（左から数えた位置を表す）を続けて入力してください．交換しない場合は0と入力してください");
            String exchangePositions = this.scanner.nextLine();

            if (exchangePositions.charAt(0) == '0') {
                break;
            }

            this.exchangeCards(exchangePositions, cards);
        }
    }

    public void exchangeCards(String exchangePositions, List<Monster> cards) {
        IntStream.range(0, exchangePositions.length()).forEach(
                i -> this.deck[Character.getNumericValue(exchangePositions.charAt(i)) - 1] = cards
                        .get(this.random.nextInt(cards.size())));

        this.printCard();
    }

    public void judgeCardHand() throws InterruptedException {
        this.handMap.clear();

        for (Monster card : deck) {
            handMap.merge(card, 1, Integer::sum);
        }

        // System.out.printf("-------------------- %s\n", this.name);
        // for (Map.Entry<Monster, Integer> entry : handMap.entrySet()) {
        // System.out.println(entry.getKey().name + " : " + entry.getValue() + "枚");
        // }
        // System.out.println("--------------------");

        // 役判定
        // 5が1つある：ファイブ
        // 4が1つある：フォー
        // 3が1つあり，かつ，2が1つある：フルハウス
        // 2が2つある：ツーペア
        // 3が1つある：スリー
        // 2が1つある：ペア
        // 1が5つある：スペシャルファイブ
        // 初期化

        boolean fiveOfKind = false;
        boolean fourOfKind = false;
        boolean threeOfKind = false;
        int pairs = 0; // pair数を保持

        for (int count : handMap.values()) {
            switch (count) {
                case 2 -> pairs++;
                case 3 -> threeOfKind = true;
                case 4 -> fourOfKind = true;
                case 5 -> fiveOfKind = true;
            }
        }

        HandRank handRank = null;
        if (handMap.size() == 5) {
            System.out.println("スペシャルファイブ！AP/DPは両方10倍！");
            handRank = HandRank.SPECIAL_FIVE;
        } else if (fiveOfKind) {
            System.out.println("ファイブ！AP/DPは両方5倍！");
            handRank = HandRank.FIVE_OF_KIND;
        } else if (fourOfKind) {
            System.out.println("フォー！AP/DPは両方4倍！");
            handRank = HandRank.FOUR_OF_KIND;
        } else if (threeOfKind && pairs == 1) {
            System.out.println("フルハウス！AP/DPは両方3倍");
            handRank = HandRank.FULL_HOUSE;
        } else if (threeOfKind) {
            System.out.println("スリーカード！AP/DPはそれぞれ3倍と2倍");
            handRank = HandRank.THREE_OF_KIND;
        } else if (pairs == 2) {
            System.out.println("ツーペア！AP/DPは両方2倍");
            handRank = HandRank.TWO_PAIR;
        } else if (pairs == 1) {
            System.out.println("ワンペア！AP/DPは両方1/2倍");
            handRank = HandRank.ONE_PAIR;
        }
        Thread.sleep(1000);

        calculatePoint(handRank);
    }

    public void calculatePoint(HandRank handRank) {
        for (Map.Entry<Monster, Integer> entry : handMap.entrySet()) {
            Monster monster = entry.getKey();
            int count = entry.getValue();

            this.attackPoint += monster.ap * count;
            this.defensePoint += monster.dp * count;
        }
        this.attackPoint *= handRank.attackMultiplier;
        this.defensePoint *= handRank.defenseMultiplier;
    }

    public void attack(Player opponentPlayer) throws InterruptedException {
        System.out.printf("%sのDrawした", this.name);
        for (Map.Entry<Monster, Integer> entry : this.handMap.entrySet()) {
            System.out.print(entry.getKey().name + " ");
            Thread.sleep(500);
        }
        System.out.print("の攻撃！");
        Thread.sleep(1000);
        System.out.printf("%sのモンスターによるガード！\n", opponentPlayer.name);
        if (opponentPlayer.defensePoint >= this.attackPoint) {
            System.out.printf("%sはノーダメージ！\n", opponentPlayer.name);
        } else {
            double damage = this.attackPoint - opponentPlayer.defensePoint;
            System.out.printf("%sは%.0fのダメージを受けた！\n", opponentPlayer.name, damage);
            opponentPlayer.hitPoint = opponentPlayer.hitPoint - damage;
        }
    }

    public void printCard() {
        System.out.print("[" + this.name + "]");
        IntStream.range(0, this.deck.length).forEach(i -> {
            System.out.printf("%s ", this.deck[i].name);
        });
        System.out.println();
    }
}
