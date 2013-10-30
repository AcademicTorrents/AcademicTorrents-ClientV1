/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2014,, FrostWire(R). All rights reserved.
 
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

package com.frostwire.search.torrent;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.frostwire.search.CrawlRegexSearchPerformer;
import com.frostwire.search.CrawlableSearchResult;
import com.frostwire.search.MaxIterCharSequence;
import com.frostwire.search.PerformersHelper;
import com.frostwire.search.SearchResult;

/**
 * 
 * @author gubatron
 * @author aldenml
 *
 */
public abstract class TorrentRegexSearchPerformer<T extends CrawlableSearchResult> extends CrawlRegexSearchPerformer<CrawlableSearchResult> {

    private final Pattern pattern;
    private final Pattern htmlPattern;

    public TorrentRegexSearchPerformer(long token, String keywords, int timeout, int pages, int numCrawls, int regexMaxResults, String regex, String htmlRegex) {
        super(token, keywords, timeout, pages, numCrawls, regexMaxResults);
        this.pattern = Pattern.compile(regex);
        this.htmlPattern = Pattern.compile(htmlRegex);
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    protected String getCrawlUrl(CrawlableSearchResult sr) {
        String crawlUrl = null;

        if (sr instanceof TorrentCrawlableSearchResult) {
            crawlUrl = ((TorrentCrawlableSearchResult) sr).getTorrentUrl();
        } else {
            crawlUrl = sr.getDetailsUrl();
        }

        return crawlUrl;
    }

    @Override
    protected List<? extends SearchResult> crawlResult(CrawlableSearchResult sr, byte[] data) throws Exception {
        List<SearchResult> list = new LinkedList<SearchResult>();

        if (sr instanceof TorrentCrawlableSearchResult) {
            list.addAll(PerformersHelper.crawlTorrent(this, (TorrentCrawlableSearchResult) sr, data));
        } else {
            String html = new String(data, "UTF-8");

            Matcher matcher = htmlPattern.matcher(new MaxIterCharSequence(html, 2 * html.length()));

            if (matcher.find()) {
                list.add(fromHtmlMatcher(sr, matcher));
            }
        }

        return list;
    }

    protected abstract T fromHtmlMatcher(CrawlableSearchResult sr, Matcher matcher);
}
