import java.time.Duration;
import java.util.Random;
import java.util.concurrent.StructuredTaskScope;

public class E_StructuredConcurrency {

  // --enable-preview
  public static void main(String[] args) throws InterruptedException {
    Quotation quotation = Quotation.readQuotation();
    System.out.println(quotation);
  }

  public record Quotation(String agency, int amount) {

    private final static Random random = new Random();

    public static Quotation readQuotation() throws InterruptedException {
      try (var scope = new StructuredTaskScope<Quotation>()) {
        StructuredTaskScope.Subtask<Quotation> future1 = scope.fork(Quotation::readFromA);
        StructuredTaskScope.Subtask<Quotation> future2 = scope.fork(Quotation::readFromB);
        StructuredTaskScope.Subtask<Quotation> future3 = scope.fork(Quotation::readFromC);
        scope.join();

        return future1.get();
      }
    }

    private static Quotation readFromA() throws InterruptedException {
      Thread.sleep(Duration.ofMillis(random.nextInt(30, 110)));
      return new Quotation("Agency A", random.nextInt());
    }

    private static Quotation readFromB() throws InterruptedException {
      Thread.sleep(Duration.ofMillis(random.nextInt(30, 110)));
      return new Quotation("Agency B", random.nextInt());
    }

    private static Quotation readFromC() throws InterruptedException {
      Thread.sleep(Duration.ofMillis(random.nextInt(30, 110)));
      return new Quotation("Agency C", random.nextInt());
    }
  }
}
