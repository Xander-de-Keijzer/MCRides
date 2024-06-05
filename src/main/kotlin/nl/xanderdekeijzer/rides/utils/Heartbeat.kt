package nl.xanderdekeijzer.rides.utils

import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import nl.xanderdekeijzer.rides.Main

abstract class Heartbeat {

    lateinit var cancel: () -> ScheduledTask.CancelledState

    init {
        Main.globalScheduler.runAtFixedRate(Main.instance, {
            cancel = { it.cancel() }
            beat()
        }, 1L, 1L)
    }

    open fun beat() {}

}