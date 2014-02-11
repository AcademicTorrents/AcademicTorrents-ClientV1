/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2014, FrostWire(R). All rights reserved.
 
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

package com.frostwire.torrent;

import java.util.HashMap;
import java.util.Map;


public class PaymentOptions implements Mappable<String, Map<String,String>>{
    /** BitCoin URI, see BIP-0021 - https://github.com/bitcoin/bips/blob/master/bip-0021.mediawiki 
     * bitcoinurn     = "bitcoin:" bitcoinaddress [ "?" bitcoinparams ]
     * bitcoinaddress = base58 *base58
     * Example: bitcoin:175tWpb8K1S7NmH4Zx6rewF9WQrcZv245W
     * 
     * To be serialized as dictionary in the .torrent as follows
     *     paymentOptions: {
     *        bitcoin: "bitcoin:14F6JPXK2fR5b4gZp3134qLRGgYtvabMWL",
     *        litecoin: "litecoin:LiYp3Dg11N5BgV8qKW42ubSZXFmjDByjoV",
     *        donationUrl: "http://frostwire.com/donate"
     *     }
     * 
     */
    public final String bitcoin;
    
    public final String litecoin;
    
    /** Simply a valid email address for creating a paypal payment form */
    public final String donationUrl;
    
    public PaymentOptions(String bitcoin, String litecoin, String paypal) {
        this.bitcoin = bitcoin;
        this.litecoin = litecoin;
        this.donationUrl = paypal;
    }
    
    public PaymentOptions(Map<String,Map<String,String>> paymentOptionsMap) {
        Map<String, String> paymentOptions = paymentOptionsMap.get("paymentOptions");
        this.bitcoin = paymentOptions.get("bitcoin");
        this.litecoin = paymentOptions.get("litecoin");
        this.donationUrl = paymentOptions.get("donationUrl");
    }

    public Map<String, Map<String, String>> asMap() {
        Map<String, String> innerMap = new HashMap<>();
        if (bitcoin != null) {
            innerMap.put("bitcoin", this.bitcoin);
        }
        if (litecoin != null) {
            innerMap.put("litecoin", this.litecoin);
        }

        if (donationUrl != null) {
            innerMap.put("donationUrl", this.donationUrl);
        }

        Map<String, Map<String, String>> paymentOptions = new HashMap<String, Map<String, String>>();

        if (!innerMap.isEmpty()) {
            paymentOptions.put("paymentOptions", innerMap);
        }
        return paymentOptions;
    }
}