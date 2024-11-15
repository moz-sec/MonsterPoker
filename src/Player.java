import java.util.stream.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;

/*
 * Player
 */
public class Player {
    String name;
    Monster[] deck = new Monster[5];
    Random random;
    HandRank handRank;
    Scanner scanner;
    CalculatePoints calculatePoints = new CalculatePoints();
    // 手札のカードとそれぞれの枚数
    // 例) スライム:2, サハギン:1, ドラゴン:2
    Map<Monster, Integer> handMap = new HashMap<>();

    double hitPoint;
    double attackPoint;
    double defensePoint;

    public Player(double attackPoint, double defensePoint, String name, Scanner scanner) {
        this.attackPoint = attackPoint;
        this.defensePoint = defensePoint;
        this.name = name;
        this.hitPoint = 1000;
        this.random = new Random();
        this.scanner = scanner;
    }

    public void draw(List<Monster> cards) throws InterruptedException {
        System.out.println("PlayerのDraw！");
        IntStream.range(0, this.deck.length).forEach(i -> {
            deck[i] = cards.get(this.random.nextInt(cards.size()));
            // テストコード
            //
            deck[i] = cards.get(2);
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

    public void handCheck() throws InterruptedException {
        this.handMap.clear();
        Arrays.stream(deck).forEach(card -> handMap.merge(card, 1, Integer::sum));

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

        if (handMap.size() == 5) {
            System.out.println("スペシャルファイブ! AP/DPは両方10倍!");
            handRank = HandRank.SPECIAL_FIVE;
        } else if (fiveOfKind) {
            System.out.println("ファイブ! AP/DPは両方5倍!");
            handRank = HandRank.FIVE_OF_KIND;
        } else if (fourOfKind) {
            System.out.println("フォー! AP/DPは両方4倍!");
            handRank = HandRank.FOUR_OF_KIND;
        } else if (threeOfKind && pairs == 1) {
            System.out.println("フルハウス! AP/DPは両方3倍");
            handRank = HandRank.FULL_HOUSE;
        } else if (threeOfKind) {
            System.out.println("スリーカード! AP/DPはそれぞれ3倍と2倍");
            handRank = HandRank.THREE_OF_KIND;
        } else if (pairs == 2) {
            System.out.println("ツーペア! AP/DPは両方2倍");
            handRank = HandRank.TWO_PAIR;
        } else if (pairs == 1) {
            System.out.println("ワンペア！AP/DPは両方1/2倍");
            handRank = HandRank.ONE_PAIR;
        }
        Thread.sleep(1000);

        calculatePoints.calculatePoint(handRank, handMap);
        this.attackPoint = calculatePoints.attackPoint;
        this.defensePoint = calculatePoints.defensePoint;
        System.out.println("====================");
        System.out.println(this.attackPoint);
        System.out.println(this.defensePoint);
        System.out.println("====================");
    }

    public void printCard() {
        System.out.print("[" + this.name + "]");
        IntStream.range(0, this.deck.length).forEach(i -> System.out.printf("%s ", this.deck[i].name));
        System.out.println();
    }
}
