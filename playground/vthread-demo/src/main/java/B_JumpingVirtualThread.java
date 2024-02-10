import java.util.List;
import java.util.stream.IntStream;

public class B_JumpingVirtualThread {

  public static void main(String[] args) throws InterruptedException {
    // 4번의 blocking
    // Virtual Thread는 Task(Runnable)을 랩핑하고 있고 스레드가 블락되는 동안 여러 Carrier Thread를 옮겨다니면서 수행되게 된다.
//    VirtualThread[#20]/runnable@ForkJoinPool-1-worker-1
//    VirtualThread[#20]/runnable@ForkJoinPool-1-worker-4
//    VirtualThread[#20]/runnable@ForkJoinPool-1-worker-7
//    VirtualThread[#20]/runnable@ForkJoinPool-1-worker-1
    List<Thread> threads = IntStream.range(0, 1_000_000)
        .mapToObj(index -> Thread.ofVirtual().unstarted(() -> {
          if (index == 0) {
            // VirtualThread[#20]/runnable@ForkJoinPool-1-worker-1
            System.out.println(Thread.currentThread());
          }
          try {
            Thread.sleep(10); // block, vth는 cth로 부터 detached(unmount)
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
          if (index == 0) {
            // VirtualThread[#20]/runnable@ForkJoinPool-1-worker-4
            System.out.println(Thread.currentThread()); // fork/join pool 안에 carrier thread에 다시 mount되어 실행..
          }
          try {
            Thread.sleep(10); // block, vth는 cth로 부터 detached
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
          if (index == 0) {
            // VirtualThread[#20]/runnable@ForkJoinPool-1-worker-7
            System.out.println(Thread.currentThread());
          }
          try {
            Thread.sleep(10); // block, vth는 cth로 부터 detached
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
          if (index == 0) {
            // VirtualThread[#20]/runnable@ForkJoinPool-1-worker-1
            System.out.println(Thread.currentThread());
          }
        })).toList();

    threads.forEach(Thread::start);
    for (Thread thread : threads) {
      thread.join();
    }
  }
}
