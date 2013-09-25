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

import java.util.UUID;

/**
 * @author gubatron
 * @author aldenml
 *
 */
public final class UXStatsContext {

    public final String guid;
    public final String os;
    public final String fwversion;

    public UXStatsContext(String os, String fwversion) {
        this.guid = UUID.randomUUID().toString();
        this.os = os;
        this.fwversion = fwversion;
    }
}
