package io.github.stuff_stuffs.tbcexutil.common;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;

import java.lang.ref.WeakReference;

public final class CacheEvictionMap<T> {
    private final Long2ReferenceMap<WeakReference<T>> weakMap;
    private final Long2LongMap lastAccess;
    private final long evictionTime;
    private long tickCount = 0;

    public CacheEvictionMap(final long evictionTime) {
        if (evictionTime < 1) {
            throw new IllegalArgumentException();
        }
        this.evictionTime = evictionTime;
        weakMap = new Long2ReferenceOpenHashMap<>();
        lastAccess = new Long2LongOpenHashMap();
    }

    public void put(final long handle, final T val) {
        weakMap.put(handle, new WeakReference<>(val));
        lastAccess.put(handle, tickCount);
    }

    public void access(final long handle) {
        lastAccess.put(handle, tickCount);
    }

    public boolean shouldEvict(final long handle) {
        if (tickCount - lastAccess.get(handle) <= evictionTime) {
            return false;
        }
        final WeakReference<T> reference = weakMap.get(handle);
        return reference == null || reference.get() == null;
    }

    public void evict(final long handle) {
        lastAccess.remove(handle);
        weakMap.remove(handle);
    }

    public void tick() {
        tickCount++;
    }
}
