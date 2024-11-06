import java.util.stream.IntStream;

/**
 * Cpu
 */
public class Cpu extends Player {
    public int exchangeCards[] = new int[5];

    public Cpu(String name) {
        super(name);
    }

    public void draw() throws InterruptedException {
        System.out.println("CPUのDraw！");
        IntStream.range(0, this.deck.length).forEach(i -> this.deck[i] = card.nextInt(5));
        this.printCard();

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
        IntStream.range(0, this.exchangeCards.length).filter(i -> this.exchangeCards[i] == 1)
                .forEach(i -> this.changeCard = this.changeCard + (i + 1));
        if (this.changeCard.length() == 0) {
            this.changeCard = "0";
        }
        System.out.println(this.changeCard);
    }

    private void exchangeCard() {
        if (this.changeCard.charAt(0) != '0') {
            IntStream.range(0, this.changeCard.length()).forEach(
                    i -> this.deck[Character.getNumericValue(this.changeCard.charAt(i)) - 1] = this.card.nextInt(5));
            this.printCard();
        }
    }

    private void decideExchangeCard() throws InterruptedException {
        System.out.println("CPUが交換するカードを考えています・・・・・・");
        Thread.sleep(2000);
        // cpuDeckを走査して，重複するカード以外のカードをランダムに交換する
        // 0,1,0,2,3 といったcpuDeckの場合，2枚目，4枚目，5枚目のカードをそれぞれ交換するかどうか決定し，例えば24といった形で決定する
        // 何番目のカードを交換するかを0,1で持つ配列の初期化
        // 例えばexchangeCards[]が{0,1,1,0,0}の場合は2,3枚目を交換の候補にする
        IntStream.range(0, this.exchangeCards.length).forEach(i -> this.exchangeCards[i] = -1);
        IntStream.range(0, this.deck.length).filter(i -> this.exchangeCards[i] == -1).forEach(i -> {
            IntStream.range(i + 1, this.deck.length).filter(j -> this.deck[i] == this.deck[j]).forEach(j -> {
                this.exchangeCards[i] = 0;
                this.exchangeCards[j] = 0;
            });
            if (this.exchangeCards[i] != 0) {
                this.exchangeCards[i] = this.card.nextInt(2);// 交換するかどうかをランダムに最終決定する
                // this.exchangeCards[i] = 1;
            }
        });// 本当にstreamにする意味ある？

        // for (int i = 0; i < this.deck.length; i++) {
        // if (this.exchangeCards[i] == -1) {
        // for (int j = i + 1; j < this.deck.length; j++) {
        // if (this.deck[i] == this.deck[j]) {
        // this.exchangeCards[i] = 0;
        // this.exchangeCards[j] = 0;
        // }
        // }
        // if (this.exchangeCards[i] != 0) {
        // this.exchangeCards[i] = this.card.nextInt(2);// 交換するかどうかをランダムに最終決定する
        // // this.exchangeCards[i] = 1;
        // }
        // }
        // }
    }
}
