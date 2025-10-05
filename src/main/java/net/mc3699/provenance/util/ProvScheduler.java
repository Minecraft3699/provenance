package net.mc3699.provenance.util;

import net.mc3699.provenance.Provenance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@EventBusSubscriber(modid = Provenance.MODID)
public class ProvScheduler {

    private static final List<ScheduledTask> tasks = new LinkedList<>();

    public static void schedule(int delayTicks, Runnable action) {
        tasks.add(new ScheduledTask(delayTicks, action));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {

        Iterator<ScheduledTask> it = tasks.iterator();
        while (it.hasNext()) {
            ScheduledTask task = it.next();
            task.ticks--;
            if (task.ticks <= 0) {
                try {
                    task.action.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                it.remove();
            }
        }
    }

   private static class ScheduledTask {
        int ticks;
        Runnable action;

        ScheduledTask(int ticks, Runnable action) {
            this.ticks = ticks;
            this.action = action;
        }
    }
}
