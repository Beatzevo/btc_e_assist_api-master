Assist TradeApi for BTC-E. Version 1.4
====================================

###Java library for  simple and comfortable use of BTC-e API.

Private BTC-E API documentation https://btc-e.com/tapi/docs

Public BTC-E API documentation https://btc-e.com/api/3/docs

NOTE: after version 1.3 previous API-key and Secret not working (new method of generating nonce parameter)

NOTE: added proxy for blocked countries. Thx to http://noblockme.ru

NOTE: if you use Oracle JDK 7, you may face the problem of certificates. Read more here http://stackoverflow.com/questions/18746565/godaddy-ssl-cert-not-working-with-java

To fix it, you can download **gdroot-g2.crt** and **gdig2.crt** from here https://certs.godaddy.com/repository
and then run in the terminal:
```bash
sudo $JAVA_HOME/bin/keytool -import -file gdroot-g2.crt -alias gdrootg2 -storepass changeit -trustcacerts -keystore $JAVA_HOME/jre/lib/security/cacerts
```

```bash
sudo $JAVA_HOME/bin/keytool -import -file gdig2.crt -alias gdig2 -storepass changeit -trustcacerts -keystore $JAVA_HOME/jre/lib/security/cacerts
```

If you want to build the library yourself, you will need:
####Jackson library http://wiki.fasterxml.com/Home
####Apache Commons Codec (TM) http://commons.apache.org/proper/commons-codec/


Common algorithm:

- set parametres for BTC-E API method
- run method
- if server responce consist of few parts (few pairs, few trades, etc.) swith to next or specific
- get current item value (current last price, current last fee, etc.)
- switch to next element
- ???
- PROFIT

If you use it in loop, do not forget to call resetParams, otherwise parametres string will be as: "...btc_usd-btc-usd" and server return error. 

Note: by default, if you add nonexistent pair (but not duplicate), library set "ignore invalid" to "true" and you can get valid answer, but without nonexistent pair. You can change this behavior, if call method setReverseIgnoreInvalid(), it reverse API method state, or you can get valid pairs list from Info API method.

###Examples:

1) Get last price for BTC-USD and LTC-USD

```java

TradeApi ta = new TradeApi(); 
ta.ticker.addPair("btc_usd"); // add btc_usd pair to ticker method
ta.ticker.addPair("ltc_usd");
ta.ticker.runMethod();// run ticker API method with added parametres
while(ta.ticker.hasNextPair()){
	ta.ticker.switchNextPair();// in this case, switch to first pairs, i.e. btc_usd, for the next iteration it will be ltc_usd
	System.out.println(ta.ticker.getCurrentLast()); // print last price for current pair
}
```

2) Get active orders for BTC-USD (depth API method)

```java

TradeApi ta = new TradeApi();
ta.depth.addPair("btc_usd");
ta.depth.setLimit(100);
ta.depth.runMethod();
ta.depth.setCurrentPair("btc_usd");// now instead switchNextPair we can use this method, but switchNextPair works too
while (ta.depth.hasNextAsk()) {
	ta.depth.switchNextAsk();// switch to next ask. In this case we can switch pairs and can switch asks and bids for each pair
	System.out.println(ta.depth.getCurrentAskPrice());
}
```

3) Get your balance

```java

String aKey = "TK7HD88A-ENLZZBCW-P4L5JYVR-18K9NZZP-CYQ7WKKH"; //API-key
String aSecret = "b7738a9f4d62da1b6ebbcd7ff2d7b5ddb93de88b71f017ae600b7ab3ed5b7015"; //SECRET-key
TradeApi t = new TradeApi(aKey,aSecret);// we can use constructor, we can use empty constructor and after that t.setKeys(...) and so on.
t.getInfo.runMethod();
System.out.println(t.getInfo.getBalance("usd")); // get USD balance. We can get currency list with getCurrencyList()
```

TradeApi is the Main class, that contains other objects with public modifiers and private objects for some usefull methods as tryMaximumBuy(...) and other.

Example of using this library you also can see in BTC-e Assist app, TradeControl class: https://github.com/alexandersjn/btc-e-assist/blob/master/src/com/btc_e_assist/TradeControl.java

###Donation:


BTC 1GQJ5iH84EVcsr5qxLCgoopLb3ttDtrQVa


LTC LQ2M5qSmD4QVMRAWw5o6cipQm8F8hAMosJ
