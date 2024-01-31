import java.util.List;
import java.util.stream.IntStream;

public class VirtualThreadDemo {

    public static void main(String[] args) throws InterruptedException {
//        Thread.ofPlatform()
//                .start(() -> System.out.println("Platform Thread: " + Thread.currentThread()));
//
//        System.out.println("-----------");
//
//        Thread.ofVirtual()
//                .start(() -> System.out.println("Virtual Thread: " + Thread.currentThread()));
//
//        Thread.sleep(1000);

//        var start = System.currentTimeMillis();
//        var totalThread = 1000;
//        List<Thread> threads = IntStream.range(0, 10)
//                .mapToObj(thCount -> Thread.ofVirtual().unstarted(() -> {
//                    if (thCount == 0) {
//                        // VirtualThread[#20]/runnable@ForkJoinPool-1-worker-1
//                        System.out.println(Thread.currentThread());
//                    }
//                    try {
//                        Thread.sleep(10); // block되기 때문에 vth는 pth를 release 되고..
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                    if (thCount == 0) {
//                        // VirtualThread[#20]/runnable@ForkJoinPool-1-worker-6
//                        System.out.println(Thread.currentThread()); // 다른 pth에 어사인
//                    }
//                })).toList();
//
//        threads.forEach(Thread::start);
//        for (Thread thread : threads) {
//            thread.join();
//        }

        var start = System.currentTimeMillis();
        var totalThread = 100000;
        List<Thread> threads = IntStream.range(0, totalThread)
                .mapToObj(thCount -> Thread.ofVirtual().unstarted(() -> {
//                    .mapToObj(thCount -> Thread.ofPlatform().unstarted(() -> {

                })).toList();

        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }

        var end = System.currentTimeMillis();
        // Platform Thread: 4795ms, Virtual Thread: 134ms => lightweight
        System.out.println("millis used to launch " + totalThread + "vthread: " + (end - start) + "ms");
    }

}
