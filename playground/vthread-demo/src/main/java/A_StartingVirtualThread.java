import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class A_StartingVirtualThread {

    public static void main(String[] args) throws InterruptedException {
        // platform thread
        Thread t1 = Thread.ofPlatform()
                .unstarted(() -> System.out.println("Platform Thread: " + Thread.currentThread()));
        t1.start();
        t1.join();

        // virtual thread
        Thread t2 = Thread.ofVirtual()
                .unstarted(() -> System.out.println("Virtual Thread: " + Thread.currentThread()));
        t2.start();
        t2.join();

        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        executorService.submit(() -> System.out.println("Virtual Thread: " + Thread.currentThread()));
        Thread.sleep(1000);
    }
}
