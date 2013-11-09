package com.frostwire.search.tests;

import com.frostwire.search.SearchManager;
import com.frostwire.search.SearchManagerImpl;
import com.frostwire.search.SearchManagerListener;
import com.frostwire.search.SearchPerformer;
import com.frostwire.search.SearchResult;
import com.frostwire.search.WebSearchPerformer;
import com.frostwire.search.domainalias.DomainAliasManager;
import com.frostwire.search.domainalias.DomainAliasManagerBroker;
import com.limegroup.gnutella.gui.search.SearchEngine;
import java.util.List;

public class DomainAliasManagerTest{
    public static void main(String[] args) throws InterruptedException {
        DomainAliasManager domainAliasManager = DomainAliasManagerBroker.getDomainAliasManager("kickass.to");//www.kat.ph");
        
        //assert(domainAliasManager.getDefaultDomain().equals("www.kat.ph"));
        

        SearchEngine kat = SearchEngine.KAT;
        SearchManager manager = new SearchManagerImpl();
        manager.registerListener(new SearchManagerListener() {
            
            @Override
            public void onResults(SearchPerformer performer, List<? extends SearchResult> results) {                
                System.out.println(performer.getToken() + " got results -> " + results.size() + " (from "+ ((WebSearchPerformer) performer).getDomainName() +")");
                for (SearchResult r : results) {
                    System.out.println(r.getDisplayName());
                }
            }
            
            @Override
            public void onFinished(long token) {
                System.out.println("search done - token:" + token);
            }
        });
        long tokenId = 2312389382l;
        manager.perform(kat.getPerformer(2312389382l, "frostwire"));

        while (true) {
            System.out.println("Waiting 10 secs for next search...");
            Thread.sleep(10000);
            tokenId += 2312389382l;
            manager.perform(kat.getPerformer(tokenId, "frostwire"));
            
        }
    }
}