import java.util.stream.IntStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/*
 * Player
 */
public class Player {
    private String name;
    protected List<Monster> deck;
    final protected int DECK_LENGTH = 5;
    private HandRank handRank;
    private Scanner scanner;
    // 手札のカードとそれぞれの枚数
    // 例) スライム:2, サハギン:1, ドラゴン:2
    Map<Monster, Integer> handMap;

    private double hitPoint;
    private double totalAttackPoint;
    private double totalDefensePoint;

    public Player(String name, Scanner scanner) {
        this.name = name;
        this.hitPoint = 1000;
        this.scanner = scanner;
        this.handMap = new HashMap<>();
    }

    public void draw(List<Monster> cards) throws InterruptedException {
        this.deck = new java.util.ArrayList<>();

        Random random = new Random();
        System.out.println("PlayerのDraw！");
        IntStream.range(0, DECK_LENGTH).forEach(i -> {
            this.deck.add(cards.get(random.nextInt(DECK_LENGTH)));
        });
        this.printCard();

        this.promptCardExchange(cards);
    }

    public void promptCardExchange(List<Monster> cards) {
        for (int i = 0; i < 2; i++) {
            System.out.println(i == 0
                    ? "カードを交換する場合は1から5の数字（左から数えた位置を表す）を続けて入力してください．交換しない場合は0と入力してください"
                    : "もう一度カードを交換する場合は1からDECK_LENGTHの数字（左から数えた位置を表す）を続けて入力してください．交換しない場合は0と入力してください");
            String exchangePositions = this.scanner.nextLine();

            if (exchangePositions.charAt(0) == '0') {
                break;
            }

            this.exchangeCards(exchangePositions, cards);
        }
    }

    public void exchangeCards(String exchangePositions, List<Monster> cards) {
        Random random = new Random();
        IntStream.range(0, exchangePositions.length())
                .forEach(i -> this.deck.set(
                        Character.getNumericValue(exchangePositions.charAt(i)) - 1,
                        cards.get(random.nextInt(cards.size()))));
        this.printCard();
    }

    public void handCheck() throws InterruptedException {
        this.handMap.clear();
        deck.forEach(card -> handMap.merge(card, 1, Integer::sum));

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

        this.playerAttackDefensePointCalcurate();
    }

    private void playerAttackDefensePointCalcurate() {
        for (Map.Entry<Monster, Integer> entry : handMap.entrySet()) {
            Monster monster = entry.getKey();
            int count = entry.getValue();

            this.totalAttackPoint += monster.getAttackPoint() * count;
            this.totalDefensePoint += monster.getDefensePoint() * count;
        }
        this.totalAttackPoint *= handRank.attackMultiplier;
        this.totalDefensePoint *= handRank.defenseMultiplier;
        System.out.println("PlayerのAPは" + this.totalAttackPoint);
        System.out.println("PlayerのDPは" + this.totalDefensePoint);
    }

    protected void printCard() {
        System.out.print("[" + this.name + "]");
        deck.forEach(card -> System.out.printf("%s ", card.getName()));
        System.out.println();
    }

    public String getName() {
        return this.name;
    }

    public List<Monster> getDeck() {
        return this.deck;
    }

    public double getHitPoint() {
        return this.hitPoint;
    }

    public void setHitPoint(double hitPoint) {
        this.hitPoint = hitPoint;
    }

    public double getAttackPoint() {
        return this.totalAttackPoint;
    }

    public double getDefensePoint() {
        return this.totalDefensePoint;
    }
}
