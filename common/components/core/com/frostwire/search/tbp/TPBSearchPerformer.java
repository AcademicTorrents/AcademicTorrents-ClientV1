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

package com.frostwire.search.tbp;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.frostwire.search.RegexSearchPerformer;
import com.frostwire.search.SearchResult;

/**
 * 
 * @author gubatron
 * @author aldenml
 *
 */
public class TPBSearchPerformer extends RegexSearchPerformer<TPBSearchResult> {

    private static final int MAX_RESULTS = 20;

    private static final String REGEX = "(?is)<td class=\"vertTh\">.*?<a href=\"[^\"]*?\" title=\"More from this category\">(.*?)</a>.*?</td>.*?<a href=\"([^\"]*?)\" class=\"detLink\" title=\"Details for ([^\"]*?)\">.*?</a>.*?<a href=\\\"(magnet:\\?xt=urn:btih:.*?)\\\" title=\\\"Download this torrent using magnet\\\">.*?</a>.*?<font class=\"detDesc\">Uploaded ([^,]*?), Size (.*?), ULed.*?<td align=\"right\">(.*?)</td>\\s*<td align=\"right\">(.*?)</td>";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public TPBSearchPerformer(long token, String keywords, int timeout) {
        super(token, keywords, timeout, 1, MAX_RESULTS, MAX_RESULTS);
    }

    @Override
    protected String getUrl(int page, String encodedKeywords) {
        return "http://thepiratebay.se/search/" + encodedKeywords + "/0/7/0";
    }

    @Override
    protected Pattern getPattern() {
        return PATTERN;
    }

    @Override
    protected TPBSearchResult fromMatcher(Matcher matcher) {
        return new TPBSearchResult(matcher);
    }

    @Override
    protected String getCrawlUrl(TPBSearchResult sr) {
        return sr.getDetailsUrl();
    }

    @Override
    protected List<? extends SearchResult> crawlResult(TPBSearchResult sr, byte[] data) throws Exception {
        return null;
    }
}
