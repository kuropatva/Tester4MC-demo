package pl.kuropatva.client.simpleClients;

import pl.kuropatva.testing.FutureResult;
import pl.kuropatva.testing.Timeout;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pl.kuropatva.testing.FutureResult.TestResult;

public class TextLogger {

    private final List<String> log = Collections.synchronizedList(new LinkedList<>());
    private final List<Function<String, Boolean>> conditions = Collections.synchronizedList(new LinkedList<>());

    public void add(String s) {
        System.out.println(parseString(s));
        if (!conditions.isEmpty()) {
            conditions.removeIf(condition -> condition.apply(parseString(s)));
        }
        log.add(s);
    }

    public FutureResult testFor(Timeout timeout, Function<String, Boolean> condition) {
        var atomicInteger = new AtomicInteger(0);
        var cfuture = new CompletableFuture<TestResult>();
        conditions.add(createCondition(timeout.nOfMessages(), condition, cfuture, atomicInteger));

        return new FutureResult(timeout, cfuture);
    }

    private static Function<String, Boolean> createCondition(int nOfMessages, Function<String, Boolean> condition, CompletableFuture<TestResult> cfuture, AtomicInteger atomicInteger) {
        return string -> { // return value - true = removal from condition list
            if (cfuture.isDone()) {
                return true;
            }
            if (atomicInteger.incrementAndGet() >= nOfMessages) {
                cfuture.complete(new TestResult(false, "Message timeout"));
                return true;
            }
            if (condition.apply(string)) {
                cfuture.complete(new TestResult(true, string));
                return true;
            }
            return false;
        };
    }

    public static String parseString(String s) {
        Pattern pattern = Pattern.compile("content=\"(.*?)\"");
        Matcher matcher = pattern.matcher(s);

        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            result.append(matcher.group(1));
        }
        return result.toString();
    }

}
