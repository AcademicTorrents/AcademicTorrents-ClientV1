/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2013, FrostWire(R). All rights reserved.
 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frostwire.uxstats;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author gubatron
 * @author aldenml
 *
 */
public final class UXStats {

    private static final long HOUR_MILLIS = 1000 * 60 * 60;
    private static final int MAX_LOG_SIZE = 10000;

    private final String guid;
    private final long time;
    private final List<UXAction> actions;

    private boolean enabled;

    private static final UXStats instance = new UXStats();

    public static UXStats instance() {
        return instance;
    }

    private UXStats() {
        this.guid = UUID.randomUUID().toString();
        this.time = System.currentTimeMillis();
        this.actions = new LinkedList<UXAction>();
        this.enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enable) {
        this.enabled = enable;
    }

    /**
     * Important: This method is not thread-safe. That means it's only
     * meant to be used on the UI thread.
     * 
     * @param action
     */
    public void log(int action) {
        if (actions.size() < MAX_LOG_SIZE) {
            actions.add(new UXAction(action, System.currentTimeMillis()));
        }

        sendData();
    }

    private void sendData() {
        if (time - System.currentTimeMillis() > HOUR_MILLIS) {
            // send data
        }
    }
}
