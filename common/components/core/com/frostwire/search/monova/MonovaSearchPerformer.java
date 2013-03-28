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

package com.frostwire.search.monova;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.frostwire.search.CrawlPagedWebSearchPerformer;
import com.frostwire.search.SearchResult;

/**
 * 
 * @author gubatron
 * @author aldenml
 *
 */
public class MonovaSearchPerformer extends CrawlPagedWebSearchPerformer<MonovaTempSearchResult> {

    private static final int MAX_RESULTS = 10;

    public MonovaSearchPerformer(long token, String keywords, int timeout) {
        super(token, keywords, timeout, 1, MAX_RESULTS);
    }

    private static final String REGEX = "(?is)<a href=\"http://www.mnova.eu/torrent/([0-9]*)/";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    private static final String HTML_REGEX = "(?is).*<div id=\"downloadbox\"><h2><a href=\"(.*)\" rel=\"nofollow\"><img src=\"http://www.mnova.eu/images/download.png\".*<a href=\"magnet:\\?xt=urn:btih:(.*)\"><b>Magnet</b></a>.*<font color=\"[A-Za-z]*\">(.*)</font> seeds,.*<strong>Total size:</strong>(.*)<br /><strong>Pieces:.*";
    private static final Pattern HTML_PATTERN = Pattern.compile(HTML_REGEX);

    @Override
    protected String getUrl(int page, String encodedKeywords) {
        return "http://www.mnova.eu/search.php?sort=5&term=" + encodedKeywords;
    }

    @Override
    protected List<? extends SearchResult> searchPage(String page) {
        List<SearchResult> result = new LinkedList<SearchResult>();

        Matcher matcher = PATTERN.matcher(page);

        int max = MAX_RESULTS;

        int i = 0;

        while (matcher.find() && i < max && !isStopped()) {
            try {
                String itemId = matcher.group(1);
                SearchResult sr = new MonovaTempSearchResult(itemId);
                if (sr != null) {
                    result.add(sr);
                    i++;
                }
            } catch (Throwable e) {
                // do nothing
            }
        }

        return result;
    }

    @Override
    protected String getCrawlUrl(MonovaTempSearchResult sr) {
        return sr.getDetailsUrl();
    }

    @Override
    protected List<? extends SearchResult> crawlResult(MonovaTempSearchResult sr, byte[] data) throws Exception {
        List<MonovaSearchResult> list = new LinkedList<MonovaSearchResult>();

        String html = new String(data, "UTF-8");

        Matcher matcher = HTML_PATTERN.matcher(html);

        if (matcher.find()) {
            list.add(new MonovaSearchResult(sr.getDetailsUrl(), matcher));
        }

        return list;
    }
}
