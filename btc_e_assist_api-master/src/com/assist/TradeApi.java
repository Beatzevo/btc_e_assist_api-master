package com.assist;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

final public class TradeApi {
    public final String EXCEPTION_INFO_NOT_SUCCESS = "Info is not success";
    public final String EXCEPTION_INVALID_PAIR_NAME = "Nonexistent pair name";
    public final String EXCEPTION_INVALID_RATE = "Invalid rate value";
    public final String EXCEPTION_TOO_HIGH_RATE = "Too high rate";
    public final String EXCEPTION_TOO_LOW_RATE = "Too low rate";
    public final String EXCEPTION_INVALID_AMOUNT = "Invalid amount value";
    public final String EXCEPTION_TOO_LOW_AMOUNT = "Too low amount";
    public final String EXCEPTION_TRADE_NOT_SUCCESS = "Trade is not success";
    public final String EXCEPTION_CANCEL_ORDER_NOT_SUCCESS = "CancelOrder is not success";
    public final String EXCEPTION_MULTIPLE_FAILURE = "Multiple failure";

    private static final double TRYMAXBUY_STOCK_RATIO = 0.97;
    private static final double TRYMAXSELL_RATE_RATIO = 0.8;
    private static final long INFO_REUSE_AGE_MILLIS = 3600000;

    public Info info;
    public Ticker ticker;
    public Depth depth;
    public Trades trades;
    public GetInfo getInfo;
    public TransHistory transHistory;
    public TradeHistory tradeHistory;
    public ActiveOrders activeOrders;
    public OrderInfo orderInfo;
    public Trade trade;
    public CancelOrder cancelOrder;
    private PrivateNetwork privateNetwork;

    private String diffPrevPair = "";
    private Ticker privateTicker;
    private Depth privateDepth;
    private GetInfo privateGetInfo;
    private Trade privateTrade;
    private Info privateInfo;
    private CancelOrder privateCancelOrder;

    public TradeApi() {
        privateNetwork = new PrivateNetwork();
        init();
    }

    public TradeApi(String aKey, String aSecret) throws Exception {
        privateNetwork = new PrivateNetwork();
        privateNetwork.setKeys(aKey, aSecret);
        init();
    }

    public TradeApi(String encodedKey, String encodedSecret, String decodePass)
            throws Exception {
        privateNetwork = new PrivateNetwork();
        privateNetwork.setKeys(encodedKey, encodedSecret, decodePass);
        init();
    }

    private void init() {
        info = new Info();
        ticker = new Ticker();
        depth = new Depth();
        trades = new Trades();
        getInfo = new GetInfo(privateNetwork);
        transHistory = new TransHistory(privateNetwork);
        tradeHistory = new TradeHistory(privateNetwork);
        activeOrders = new ActiveOrders(privateNetwork);
        orderInfo = new OrderInfo(privateNetwork);
        trade = new Trade(privateNetwork);
        cancelOrder = new CancelOrder(privateNetwork);

        privateTicker = new Ticker();
        privateDepth = new Depth();
        privateInfo = new Info();
        privateGetInfo = new GetInfo(privateNetwork);
        privateTrade = new Trade(privateNetwork);
        privateCancelOrder = new CancelOrder(privateNetwork);
    }

    public void setKeys(String aKey, String aSecret) throws Exception {
        privateNetwork.setKeys(aKey, aSecret);
    }

    public void setKeys(String encodedKey, String encodedSecret,
                        String decodePass) throws Exception {
        privateNetwork.setKeys(encodedKey, encodedSecret, decodePass);
    }

    /**
     * Is installed api-key and secret or not
     *
     * @return true if installed, otherwise false
     */
    public boolean isKeysInstalled() {
        return privateNetwork.isKeysInstalled();
    }

    /**
     * Reset installed keys, after that private methods not working
     */
    public void resetInstalledKeys() {
        privateNetwork.resetInstalledKeys();
    }

    /**
     * Set timeouts for all public methods
     *
     * @param connectMillis
     * @param readMillis
     */
    public void setTimeouts(int connectMillis, int readMillis) {
        info.setTimeouts(connectMillis, readMillis);
        ticker.setTimeouts(connectMillis, readMillis);
        depth.setTimeouts(connectMillis, readMillis);
        trades.setTimeouts(connectMillis, readMillis);
        getInfo.setTimeouts(connectMillis, readMillis);
        transHistory.setTimeouts(connectMillis, readMillis);
        tradeHistory.setTimeouts(connectMillis, readMillis);
        activeOrders.setTimeouts(connectMillis, readMillis);
        orderInfo.setTimeouts(connectMillis, readMillis);
        trade.setTimeouts(connectMillis, readMillis);
        cancelOrder.setTimeouts(connectMillis, readMillis);
    }

    /**
     * Reset to default timeouts for all methods
     */
    public void resetTimeouts() {
        info.resetTimeouts();
        ticker.resetTimeouts();
        depth.resetTimeouts();
        trades.resetTimeouts();
        getInfo.resetTimeouts();
        transHistory.resetTimeouts();
        tradeHistory.resetTimeouts();
        activeOrders.resetTimeouts();
        orderInfo.resetTimeouts();
        trade.resetTimeouts();
        cancelOrder.resetTimeouts();
    }

    /**
     * Encode string to DESede with SHA-1 from 'key' (key->SHA-1->DESede)
     *
     * @param str original string
     * @param key key for encoding
     * @return encoded string
     * @throws Exception
     */
    public String encodeString(String str, String key) throws Exception {
        return privateNetwork.encodeStr(str, key);
    }

    /**
     * Decode string from DESede
     *
     * @param str encoded string
     * @param key key for decoding
     * @return decoded string
     * @throws Exception
     */
    public String decodeString(String str, String key) throws Exception {
        return privateNetwork.decodeStr(str, key);
    }

    /**
     * Set name for private and public requests, default name is "Assist TradeApi"
     *
     * @param name
     */
    public void setClientName(String name) {
        privateNetwork.setClientName(name);
        info.setClientName(name);
        ticker.setClientName(name);
        depth.setClientName(name);
        trades.setClientName(name);
    }

    /**
     * Format input double number for using with API, if placesCount > 8, it
     * will be replaced to 8.
     *
     * @return Formatted number
     */
    public static String formatDouble(double source, int placesCount) {
        DecimalFormatSymbols separator = new DecimalFormatSymbols();
        separator.setDecimalSeparator('.');
        NumberFormat formatter = new DecimalFormat(CommonClass.FORMAT_STR,
                separator);
        if (placesCount >= 0 && placesCount < 8) {
            if (placesCount != 0) {
                formatter = new DecimalFormat(
                        CommonClass.FORMAT_STR.substring(0,
                                CommonClass.FORMAT_STR_POINT_POSITION
                                        + placesCount), separator);
            } else {
                formatter = new DecimalFormat("#", separator);
            }
        }
        return formatter.format(source);
    }

    /**
     * Extended version, over native Trade method, checks input parameters and
     * sets correct decimal places for 'rate'
     *
     * @param pairName pair name
     * @param type     buy - true, sell - false
     * @param rate     rate
     * @param amount   amount
     * @return New Trade Object with response from server, not intended for
     * trading.
     * @throws Exception with messages constants: EXCEPTION_INFO_NOT_SUCCESS,
     *                   EXCEPTION_INVALID_PAIR_NAME, EXCEPTION_INVALID_RATE,
     *                   EXCEPTION_TOO_HIGH_RATE, EXCEPTION_TOO_LOW_RATE,
     *                   EXCEPTION_INVALID_AMOUNT, EXCEPTION_TOO_LOW_AMOUNT
     */
    public synchronized Trade extendedTrade(String pairName, boolean type, String rate,
                                            String amount) throws Exception {
        String mPairName = CommonClass.convertPairName(pairName);
        Double mRate;
        Double mAmount;
        String rateStr;
        String amountStr;
        String mType;
        long infoAge = System.currentTimeMillis() - info.getLocalTimestamp();
        long privateInfoAge = System.currentTimeMillis()
                - privateInfo.getLocalTimestamp();

        if (infoAge < privateInfoAge && infoAge <= INFO_REUSE_AGE_MILLIS) {
            privateInfo.setData(info.toString());
        } else {
            if (privateInfoAge > INFO_REUSE_AGE_MILLIS) {
                if (!privateInfo.runMethod()) {
                    privateInfo.runMethod();
                }
            }
        }
        if (!privateInfo.isSuccess()) {
            throw new Exception(EXCEPTION_INFO_NOT_SUCCESS);
        }
        if (!privateInfo.getPairsList().contains(mPairName)) {
            throw new Exception(EXCEPTION_INVALID_PAIR_NAME);
        }
        privateInfo.setCurrentPair(mPairName);

        try {
            mRate = Double.parseDouble(rate);
        } catch (Exception e) {
            throw new Exception(EXCEPTION_INVALID_RATE);
        }

        if (mRate > privateInfo.getCurrentMaxPrice()) {
            throw new Exception(EXCEPTION_TOO_HIGH_RATE);
        }

        if (mRate < privateInfo.getCurrentMinPrice()) {
            throw new Exception(EXCEPTION_TOO_LOW_RATE);
        }

        try {
            mAmount = Double.parseDouble(amount);
        } catch (Exception e) {
            throw new Exception(EXCEPTION_INVALID_AMOUNT);
        }

        if (mAmount < privateInfo.getCurrentMinAmount()) {
            throw new Exception(EXCEPTION_TOO_LOW_AMOUNT);
        }

        rateStr = formatDouble(mRate, privateInfo.getCurrentDecimalPlaces());
        amountStr = formatDouble(mAmount, 8);

        mType = type ? "buy" : "sell";
        privateTrade.setPair(mPairName);
        privateTrade.setType(mType);
        privateTrade.setRate(rateStr);
        privateTrade.setAmount(amountStr);
        if (!privateTrade.runMethod()) {
            privateTrade.runMethod();
        }
        Trade result = new Trade(null);
        result.setData(privateTrade.toString());
        return result;
    }

    /**
     * Return the difference between the targetPrice and current last price. May
     * be used for the price alarm. Return Double.MIN_VALUE if has ANY trouble
     * Note: btc-e.com server update data every 2 seconds.
     *
     * @param pairName    pair name
     * @param targetPrice target price
     * @param pauseMillis - pause in milliseconds, may be used "in loop"
     * @return Difference or Double.MIN_VALUE if has ANY trouble
     */
    public synchronized double priceDifference(String pairName, Double targetPrice,
                                               int pauseMillis) {
        synchronized (this) {
            double difference = Double.MIN_VALUE;
            try {
                if (!diffPrevPair.equals(pairName)) {
                    diffPrevPair = pairName;
                    privateTicker.resetParams();
                    privateTicker.addPair(pairName);
                }
                if (privateTicker.runMethod()) {
                    privateTicker.setCurrentPair(pairName);
                    difference = targetPrice
                            - Double.parseDouble(privateTicker.getCurrentLast());
                }
                Thread.sleep(pauseMillis);
            } catch (Exception e) {
                return Double.MIN_VALUE;
            }
            return difference;
        }
    }

    /**
     * Trying to buy maximum for input pair quickly.
     *
     * @param pairName       pair name
     * @param reuseAgeMillis - limit for reusing the data, contains in public and private
     *                       Depth objects. If data inside these objects is younger than
     *                       this limit, it will be reused, otherwise method will try to
     *                       get new data. Anyway, public Depth inner states is not
     *                       changing.
     * @return Remains. For example, for pair BTC-USD, remains may be 0.012 USD
     * on your balance after this operation.
     * @throws Exception with messages constants: EXCEPTION_MULTIPLE_FAILURE,
     *                   EXCEPTION_TRADE_NOT_SUCCESS,
     *                   EXCEPTION_CANCEL_ORDER_NOT_SUCCESS and other from
     *                   extendedTrade.
     */
    public synchronized double tryMaximumBuy(String pairName, long reuseAgeMillis)
            throws Exception {
        long privateDepthAge, depthAge;
        double funds = 0, realFunds = Double.MIN_VALUE, initialFunds = 0;
        try {
            String[] names = CommonClass.convertPairName(pairName).split("-");
            Thread getInfoThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!privateGetInfo.runMethod()) {
                        privateGetInfo.runMethod();
                    }
                }
            });
            getInfoThread.start();
            privateDepthAge = System.currentTimeMillis()
                    - privateDepth.getLocalTimestamp();
            depthAge = System.currentTimeMillis() - depth.getLocalTimestamp();
            if (depthAge < privateDepthAge && depthAge <= reuseAgeMillis) {
                privateDepth.setData(depth.toString());
            } else {
                if (privateDepthAge > reuseAgeMillis) {
                    privateDepth.resetParams();
                    privateDepth.addPair(pairName);
                    if (!privateDepth.runMethod()) {
                        privateDepth.runMethod();
                    }
                }
            }
            getInfoThread.join();

            if (!privateDepth.isSuccess() || !privateGetInfo.isSuccess()) {
                throw new Exception(EXCEPTION_MULTIPLE_FAILURE);
            }
            realFunds = initialFunds = Double.parseDouble(privateGetInfo
                    .getBalance(names[1]));
            funds = initialFunds * TRYMAXBUY_STOCK_RATIO;
            String priceStr, amountStr;
            double price, amount, oldWorth, newWorth, totalAmount = 0;

            for (int i = 0; i < 5; i++) {
                privateDepth.setCurrentPair(pairName);
                while (privateDepth.hasNextAsk()) {
                    privateDepth.switchNextAsk();
                    priceStr = privateDepth.getCurrentAskPrice();
                    price = Double.parseDouble(priceStr);
                    amountStr = privateDepth.getCurrentAskAmount();
                    amount = Double.parseDouble(amountStr);
                    oldWorth = price * totalAmount;
                    newWorth = price * (amount + totalAmount);
                    if (newWorth < funds) {
                        totalAmount += amount;
                    } else {
                        totalAmount += (funds - oldWorth) / price;
                        extendedTrade(pairName, true, priceStr,
                                String.valueOf(totalAmount));
                        if (!privateTrade.isSuccess()) {
                            throw new Exception(EXCEPTION_TRADE_NOT_SUCCESS);
                        }
                        String orderId = privateTrade.getOrder_id();
                        if (!orderId.equals("0")) {
                            privateCancelOrder.setOrder_id(orderId);
                            if (!privateCancelOrder.runMethod()) {
                                privateCancelOrder.runMethod();
                            }
                            if (privateCancelOrder.isSuccess()) {
                                realFunds = Double
                                        .parseDouble(privateCancelOrder
                                                .getBalance(names[1]));
                                funds = realFunds * TRYMAXBUY_STOCK_RATIO;
                            } else {
                                throw new Exception(
                                        EXCEPTION_CANCEL_ORDER_NOT_SUCCESS);
                            }
                        } else {
                            realFunds = Double.parseDouble(privateTrade
                                    .getBalance(names[1]));
                            funds = realFunds * TRYMAXBUY_STOCK_RATIO;
                        }
                        totalAmount = 0;
                        break;
                    }
                }
                privateDepth.switchResetAsk();
                privateDepth.switchResetPair();
            }
        } catch (Exception e) {
            if (e.getMessage().equals(EXCEPTION_TOO_LOW_AMOUNT)) {
                if (realFunds != initialFunds && realFunds != Double.MIN_VALUE) {
                    return realFunds;
                }
            }
            throw e;
        }
        return realFunds;
    }

    /**
     * Trying to sell all for input pair quickly.
     *
     * @param pairName pair name
     * @return Remains. For example, for pair BTC-USD, remains may be 0.0001 BTC
     * on your balance after this operation.
     * @throws Exception with messages constants: EXCEPTION_MULTIPLE_FAILURE,
     *                   EXCEPTION_TRADE_NOT_SUCCESS,
     *                   EXCEPTION_CANCEL_ORDER_NOT_SUCCESS and other from
     *                   extendedTrade.
     */
    public synchronized double tryMaximumSell(String pairName) throws Exception {
        double resultRemains = Double.MIN_VALUE;
        String[] names = CommonClass.convertPairName(pairName).split("-");
        double ratio = 0;
        String amountStr, ratioStr;
        Thread getInfoThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!privateGetInfo.runMethod()) {
                    privateGetInfo.runMethod();
                }
            }
        });
        getInfoThread.start();
        privateTicker.resetParams();
        privateTicker.addPair(pairName);
        if (!privateTicker.runMethod()) {
            privateTicker.runMethod();
        }
        getInfoThread.join();

        if (!privateTicker.isSuccess() || !privateGetInfo.isSuccess()) {
            throw new Exception(EXCEPTION_MULTIPLE_FAILURE);
        }
        privateTicker.setCurrentPair(pairName);
        ratio = Double.parseDouble(privateTicker.getCurrentLast())
                * TRYMAXSELL_RATE_RATIO;
        ratioStr = String.valueOf(ratio);
        amountStr = privateGetInfo.getBalance(names[0]);
        extendedTrade(pairName, false, ratioStr, amountStr);
        if (!privateTrade.isSuccess()) {
            throw new Exception(EXCEPTION_TRADE_NOT_SUCCESS);
        }
        String orderId = privateTrade.getOrder_id();
        if (!orderId.equals("0")) {
            privateCancelOrder.setOrder_id(orderId);
            if (!privateCancelOrder.runMethod()) {
                privateCancelOrder.runMethod();
            }
            if (privateCancelOrder.isSuccess()) {
                ratio *= TRYMAXSELL_RATE_RATIO;
                ratioStr = String.valueOf(ratio);
                extendedTrade(pairName, false, ratioStr, amountStr);
                if (!privateTrade.isSuccess()) {
                    throw new Exception(EXCEPTION_TRADE_NOT_SUCCESS);
                }
            } else {
                throw new Exception(EXCEPTION_CANCEL_ORDER_NOT_SUCCESS);
            }
        }
        try {
            resultRemains = Double.parseDouble(privateTrade
                    .getBalance(names[0]));
        } catch (Exception e) {
            throw new Exception(EXCEPTION_TRADE_NOT_SUCCESS);
        }
        return resultRemains;
    }

    /**
     * Cancel few orders, try 3 times per order
     *
     * @param array array with ID's
     * @return the number of not cancelled orders
     */
    public synchronized int cancelFewOrders(Iterable<String> array) {
        int notCancelled = 0;
        for (String id : array) {
            privateCancelOrder.setOrder_id(id);
            if (!privateCancelOrder.runMethod()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                if (!privateCancelOrder.runMethod()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    if (!privateCancelOrder.runMethod()) {
                        notCancelled++;
                    }
                }
            }
        }
        return notCancelled;
    }
}
