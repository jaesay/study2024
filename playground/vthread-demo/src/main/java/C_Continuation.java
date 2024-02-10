import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

public class C_Continuation {

  // --add-exports java.base/jdk.internal.vm=ALL-UNNAMED
  public static void main(String[] args) {
    ContinuationScope scope = new ContinuationScope("hello");

    Continuation continuation = new Continuation(
        scope,
        () -> {
          System.out.println("I'm running"); // 1번
          Continuation.yield(scope); // suspended..
          System.out.println("I'm still running"); // 3번
        }
    );

//    I'm running
//    Back from run
//    I'm still running
//    Back from run again
    continuation.run(); // yield 이전 코드(1번) 실행
    System.out.println("Back from run"); // 2번. yield 다음 여기로..
    continuation.run(); // yield 다음 코드(2번) 실행
    System.out.println("Back from run again"); // 4번. 여기로..
  }
}
