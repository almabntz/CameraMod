package com.catface.cameramod.camera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Owns the ordered keyframes for one camera sequence.
 *
 * Storage concerns live here rather than inside playback.
 */
public final class CameraPath {

    private final String name;
    private final List<CameraKeyframe> keyframes = new ArrayList<>();

    public CameraPath(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Camera path name cannot be blank.");
        }

        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Adds a keyframe while keeping the path ordered by tick.
     *
     * @param keyframe keyframe to add
     */
    public void addKeyframe(CameraKeyframe keyframe) {
        if (keyframe == null) {
            throw new IllegalArgumentException("Keyframe cannot be null.");
        }

        boolean tickAlreadyExists = keyframes.stream()
                .anyMatch(existing -> existing.getTick() == keyframe.getTick());

        if (tickAlreadyExists) {
            throw new IllegalArgumentException(
                    "A keyframe already exists at tick " + keyframe.getTick() + "."
            );
        }

        keyframes.add(keyframe);
        keyframes.sort((first, second) ->
                Integer.compare(first.getTick(), second.getTick()));
    }

    public boolean removeKeyframe(int index) {
        if (index < 0 || index >= keyframes.size()) {
            return false;
        }

        keyframes.remove(index);
        return true;
    }

    public void clear() {
        keyframes.clear();
    }

    public int size() {
        return keyframes.size();
    }

    public boolean isEmpty() {
        return keyframes.isEmpty();
    }

    /**
     * Returns a read-only view so callers cannot mutate our internal list.
     */
    public List<CameraKeyframe> getKeyframes() {
        return Collections.unmodifiableList(keyframes);
    }

    public CameraKeyframe getKeyframe(int index) {
        return keyframes.get(index);
    }

    public int getDurationTicks() {
        if (keyframes.isEmpty()) {
            return 0;
        }

        return keyframes.get(keyframes.size() - 1).getTick();
    }
}
