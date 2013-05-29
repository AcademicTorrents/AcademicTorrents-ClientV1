/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011, 2012, FrostWire(R). All rights reserved.
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

package com.frostwire.search.isohunt;

import com.frostwire.search.torrent.ComparableTorrentJsonItem;

/**
 * @author gubatron
 * @author aldenml
 *
 */
public class ISOHuntItem implements ComparableTorrentJsonItem {

    public String title;

    public String link; //isohunt torrent details.

    public String guid;

    public String enclosure_url; //actual .torrent url

    public String length; //size in bytes

    public String type;

    public String tracker;

    public String tracker_url;

    public String original_link; //html page where torrent was crawled from

    public String size; //human readable size

    public String files; //total files inside torrent

    public String Seeds;

    public String leechers;

    public String pubDate; //In this format Thu, 29 Apr 2010 16:32:44 GMT

    public String hash;
    
    @Override
    public int getSeeds() {
        int result = 0;
        try {
            result = Integer.valueOf(Seeds);
        } catch (Exception e) {

        }
        return result;
    }
}