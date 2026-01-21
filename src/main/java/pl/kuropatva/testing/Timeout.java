package pl.kuropatva.testing;

import java.util.concurrent.TimeUnit;

public record Timeout(int nOfMessages, long timeout, TimeUnit unit) {

}
