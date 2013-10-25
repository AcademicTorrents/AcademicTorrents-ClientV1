/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2014, FrostWire(R). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.frostwire.search.vertor;

import com.frostwire.search.torrent.ComparableTorrentJsonItem;

/*
{ "results":[
   {"name":"...",
   "cdate":"8 Jun 11",
   "seeds":"733",
   "leechers":"287",
   "size":"105166808",
   "url":"...",
   "download":"...",
   "category":"Music"},
*/
/**
 * @author gubatron
 * @author aldenml
 *
 */
public class VertorItem implements ComparableTorrentJsonItem {

    public String name;

    public String cdate;

    public String seeds;

    public String leechers;

    public String size;

    public String url;

    public String download;

    public String category;

    @Override
    public int getSeeds() {
        int result = 0;
        try {
            result = Integer.valueOf(seeds);
        } catch (Exception e) {
        }
        return result;
    }
}
