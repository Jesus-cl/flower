import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class RandomTest {

    /**
     * 测试random
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("threadNum,Random,ThreadLocalRandom");
        for (int threadNum = 50; threadNum <= 500; threadNum += 50) {
            ExecutorService poolR = Executors.newFixedThreadPool(threadNum);
            long RStartTime = System.currentTimeMillis();
            for (int i = 0; i < threadNum; i++) {
                poolR.execute(new UtilRandom());
            }
            try {
                poolR.shutdown();
                poolR.awaitTermination(100, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String str = "" + threadNum +"," + (System.currentTimeMillis() - RStartTime)+",";

            ExecutorService poolTLR = Executors.newFixedThreadPool(threadNum);
            long TLRStartTime = System.currentTimeMillis();
            for (int i = 0; i < threadNum; i++) {
                poolTLR.execute(new LocalThreadRandom());
            }
            try {
                poolTLR.shutdown();
                poolTLR.awaitTermination(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(str + (System.currentTimeMillis() - TLRStartTime));
        }
    }

    private static final int NUMBER = 10000;
    // from java.util.concurrent.
    private static class LocalThreadRandom implements Runnable {
        @Override
        public void run() {
            long index = 0;
            for (int i = 0; i < NUMBER; i++) {
                index += ThreadLocalRandom.current().nextInt(400);
            }
        }
    }

    // from java.util
    private static class UtilRandom implements Runnable {
        @Override
        public void run() {
            long index = 0;
            for (int i = 0; i < NUMBER; i++) {
                index += (int) (Math.random() * 400);
            }
        }
    }
}