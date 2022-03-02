package io.github.stuff_stuffs.tbcexutil.common.event;

public class EventListenerKey {
    private final AbstractEvent<?, ?> parent;
    private boolean isAlive = true;

    public EventListenerKey(final AbstractEvent<?, ?> parent) {
        this.parent = parent;
    }

    public Event<?, ?> getParent() {
        return parent;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void destroy() {
        isAlive = false;
        parent.remove(this);
    }
}
