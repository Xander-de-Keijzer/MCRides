package nl.xanderdekeijzer.rides.utils

import nl.xanderdekeijzer.rides.Main
import java.util.concurrent.TimeUnit

fun runSync(task: () -> Unit) {
    Main.globalScheduler.run(Main.instance) {
        task()
    }
}

fun runAsync(task: () -> Unit) {
    Main.asyncScheduler.runNow(Main.instance) {
        task()
    }
}

fun runSyncLater(delay: Int = 1, task: () -> Unit) {
    Main.globalScheduler.runDelayed(Main.instance, {
        task()
    }, delay.toLong())
}

fun runAsyncLater(delay: Int = 1, task: () -> Unit, timeUnit: TimeUnit? = null) {
    if (timeUnit != null) {
        Main.asyncScheduler.runDelayed(Main.instance, {
            task()
        }, delay.toLong(), timeUnit)
    } else {
        Main.asyncScheduler.runDelayed(Main.instance, {
            task()
        }, delay.toLong() * 50L, TimeUnit.MILLISECONDS)
    }
}

fun runSyncTimer(period: Int = 1, task: (cancel: (() -> Unit)) -> Unit) {
    Main.globalScheduler.runAtFixedRate(Main.instance, {
        task { it.cancel() }
    }, 1L, period.toLong())
}

fun runAsyncTimer(period: Int = 1, task: (cancel: (() -> Unit)) -> Unit, timeUnit: TimeUnit? = null) {
    if (timeUnit != null) {
        Main.asyncScheduler.runAtFixedRate(Main.instance, {
            task { it.cancel() }
        }, 0L, period.toLong(), timeUnit)
    } else {
        Main.asyncScheduler.runAtFixedRate(Main.instance, {
            task { it.cancel() }
        }, 0L, period.toLong() * 50, TimeUnit.MILLISECONDS)
    }
}