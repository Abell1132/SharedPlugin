package dev.shared.do_gamer.behaviour;

import com.github.manolo8.darkbot.core.manager.DispatchManager;
import com.github.manolo8.darkbot.core.objects.gui.DispatchPopupRewardGui;
import dev.shared.do_gamer.config.AutoDispatchConfig;
import eu.darkbot.api.PluginAPI;
import eu.darkbot.api.config.ConfigSetting;
import eu.darkbot.api.extensions.Behavior;
import eu.darkbot.api.extensions.Configurable;
import eu.darkbot.api.extensions.Feature;
import eu.darkbot.api.managers.DispatchAPI;

/** Paced UI actions; never purchases slots or uses instant completion. */
@Feature(name = "Auto Dispatch", description = "Collects completed retrievers and fills unlocked slots with the selected dispatch.")
public class AutoDispatch implements Behavior, Configurable<AutoDispatchConfig> {
    private enum Step { IDLE, OPEN, COLLECT, AVAILABLE, SELECT, HIRE, CONFIRM, VERIFY }
    private final DispatchManager dispatch;
    private final DispatchPopupRewardGui rewards;
    private AutoDispatchConfig config;
    private Step step = Step.IDLE;
    private long nextCheck, nextAction, deadline;
    private int slotsBefore;
    private String selectedName;

    public AutoDispatch(PluginAPI api) {
        dispatch = api.requireAPI(DispatchManager.class);
        rewards = api.requireAPI(DispatchPopupRewardGui.class);
    }

    @Override
    public void setConfig(ConfigSetting<AutoDispatchConfig> setting) {
        config = setting.getValue();
        nextCheck = 0;
    }

    @Override
    public void onTickBehavior() {
        long now = System.currentTimeMillis();
        if (config == null || !config.enabled) {
            cancel();
            nextCheck = 0;
            return;
        }
        if (step == Step.IDLE) {
            if (now < nextCheck) return;
            selectedName = config.retriever;
            deadline = now + 60_000;
            step = Step.OPEN;
        }
        if (now >= deadline || !java.util.Objects.equals(selectedName, config.retriever)) {
            finish(now);
            return;
        }
        if (now < nextAction) return;
        nextAction = now + 1_000;
        advance(now);
    }

    private void advance(long now) {
        switch (step) {
            case OPEN:
                if (dispatch.openRetrieverTab()) step = Step.COLLECT;
                break;
            case COLLECT: collect(now); break;
            case AVAILABLE:
                if (dispatch.openAvailableTab()) step = Step.SELECT;
                break;
            case SELECT: select(now); break;
            case HIRE: hire(now); break;
            case CONFIRM:
                dispatch.clickAcceptPopup();
                step = Step.VERIFY;
                break;
            case VERIFY: verify(now); break;
            default: break;
        }
    }

    private void collect(long now) {
        rewards.show(false);
        dispatch.clickCloseOkPopup();
        java.util.List<? extends DispatchAPI.Retriever> running = dispatch.getInProgressRetrievers();
        for (int i = 0; i < running.size(); i++) {
            double remaining = running.get(i).getDuration();
            if (Double.isFinite(remaining) && remaining >= 0 && remaining <= 0.001) {
                dispatch.clickCollect(i);
                return;
            }
        }
        if (dispatch.getAvailableSlots() > 0) step = Step.AVAILABLE;
        else finish(now);
    }

    private DispatchAPI.Retriever findSelected() {
        return dispatch.getAvailableRetrievers().stream()
                .filter(r -> selectedName != null && selectedName.equals(r.getName()))
                .filter(DispatchAPI.Retriever::isAvailable).findFirst().orElse(null);
    }

    private void select(long now) {
        DispatchAPI.Retriever retriever = findSelected();
        if (retriever == null || dispatch.getAvailableSlots() <= 0) {
            finish(now);
            return;
        }
        dispatch.overrideSelectedRetriever(retriever);
        step = Step.HIRE;
    }

    private void hire(long now) {
        DispatchAPI.Retriever selected = dispatch.getSelectedRetriever();
        if (selected == null || !selectedName.equals(selected.getName())
                || findSelected() == null || dispatch.getAvailableSlots() <= 0) {
            finish(now);
            return;
        }
        slotsBefore = dispatch.getAvailableSlots();
        if (dispatch.clickHire()) step = Step.CONFIRM;
    }

    private void verify(long now) {
        dispatch.clickCloseOkPopup();
        // Never repeat an unacknowledged hire: failed/late requests wait until next check.
        if (dispatch.getAvailableSlots() >= slotsBefore) {
            finish(now);
            return;
        }
        step = Step.COLLECT;
    }

    private void finish(long now) {
        cancel();
        nextCheck = now + Math.max(1, Math.min(1440, config.intervalMinutes)) * 60_000L;
    }

    private void cancel() {
        if (step != Step.IDLE) {
            dispatch.clickDeclinePopup();
            rewards.show(false);
            dispatch.show(false);
        }
        step = Step.IDLE;
        nextAction = 0;
    }

    @Override
    public void onStoppedBehavior() {
        cancel();
    }
}
