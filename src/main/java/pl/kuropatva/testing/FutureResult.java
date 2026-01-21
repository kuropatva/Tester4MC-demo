package pl.kuropatva.testing;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class FutureResult {

    private Timeout timeout;
    private CompletableFuture<TestResult> completableFuture;

    public FutureResult(Timeout timeout, CompletableFuture<TestResult> completableFuture) {
        this.timeout = timeout;
        this.completableFuture = completableFuture;
    }

    public TestResult get() {
        TestResult testResult;
        try {
            testResult = completableFuture.get(timeout.timeout(), timeout.unit());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            testResult = new TestResult(false, e.toString());
        }
        return testResult;
    }


    public record TestResult(boolean value, String cause) {}
}
