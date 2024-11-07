import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.HashMap;

/**
 * Cpu
 */
public class Cpu extends Player {
    String changeCard = new String();
    int exchangeCards[] = new int[5];

    public Cpu(String name) {
        super(name);
    }

    public void draw(List<Monster> cards) throws InterruptedException {
        System.out.println("CPUのDraw！");
        Random random = new Random();
        IntStream.range(0, this.deck.length)
                .forEach(i -> this.deck[i] = cards.get(random.nextInt(cards.size())));
        this.printCard();

        // 交換するカードの決定
        this.decideExchangeCard();

        // 交換するカード番号の表示
        this.printExchangeCardNumber();

        // カードの交換
        this.exchangeCard(cards);

        // 交換するカードの決定
        this.decideExchangeCard();

        // 交換するカード番号の表示
        this.printExchangeCardNumber();

        // カードの交換
        this.exchangeCard(cards);
    }

    private void exchangeCard(List<Monster> cards) {
        if (this.changeCard.charAt(0) != '0') {
            Random random = new Random();
            for (int i = 0; i < this.changeCard.length(); i++) {
                this.deck[Character.getNumericValue(this.changeCard.charAt(i)) - 1] =
                        cards.get(random.nextInt(cards.size()));
            }
            this.printCard();
        }
    }

    private void decideExchangeCard() throws InterruptedException {
        System.out.println("CPUが交換するカードを考えています・・・・・・");
        Thread.sleep(2000);

        // カードとそれぞれの枚数
        // 例) スライム:2, サハギン:1, ドラゴン:2
        Map<Monster, Integer> cardCount = new HashMap<>();

        for (Monster card : this.deck) {
            cardCount.put(card, cardCount.getOrDefault(card, 0) + 1);
        }

        Random random = new Random();

        // deckを走査して，重複するカード以外のカードをランダムに交換する
        // 0,1,0,2,3 といったdeckの場合，2枚目，4枚目，5枚目のカードをそれぞれ交換するかどうか決定し，例えば24といった形で決定する
        // 何番目のカードを交換するかを0,1で持つ配列の初期化
        // 例えばexchangeCards[]が{0,1,1,0,0}の場合は2,3枚目を交換の候補にする
        for (int i = 0; i < this.deck.length; i++) {
            // 重複があれば交換候補から除外する
            if (cardCount.get(this.deck[i]) > 1) {
                this.exchangeCards[i] = 0; // 交換しない
            } else {
                // 重複がない場合、1/2の確率で交換する
                this.exchangeCards[i] = random.nextInt(2);
            }
        }
    }

    private void printExchangeCardNumber() {
        this.changeCard = "";
        IntStream.range(0, this.exchangeCards.length).filter(i -> this.exchangeCards[i] == 1)
                .forEach(i -> this.changeCard = this.changeCard + (i + 1));
        if (this.changeCard.length() == 0) {
            this.changeCard = "0";
        }
        System.out.println(this.changeCard);
    }
}
