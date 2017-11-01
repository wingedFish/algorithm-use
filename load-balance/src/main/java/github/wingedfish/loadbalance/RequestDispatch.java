package github.wingedfish.loadbalance;

import java.util.*;

/**
 * Created by lixiuhai on 2017/10/26.
 */
public class RequestDispatch {

    private static final String server1 = "";
    private static final String server2 = "";
    private static final String server3 = "";
    private static final String server4 = "";
    private static Map<String, Integer> serverMap = new HashMap<String, Integer>();

    static {
        serverMap.put(server1, 1);
        serverMap.put(server2, 2);
        serverMap.put(server3, 3);
        serverMap.put(server4, 4);
    }

    private int position = 0;

    /**
     * 随机分配流量
     * @return
     */
    public String getServerByRandom() {
        Map<String, Integer> currentServerMap = Collections.unmodifiableMap(serverMap);

        List<String> serverList = new ArrayList<String>(currentServerMap.keySet());

        int serverIndex = new Random().nextInt(serverList.size());
        return serverList.get(serverIndex);
    }

    /**
     * 随机获取指定权重的流量
     * @return
     */
    public String getServerByAssiginProportionRandom() {
        Map<String, Integer> currentServerMap = serverMap;


        List<String> serverList = new ArrayList<String>();

        Set<Map.Entry<String, Integer>> entrySet = currentServerMap.entrySet();
        for (Map.Entry<String, Integer> entry : entrySet) {
            for (int i = 0; i < entry.getValue(); i++) {
                serverList.add(entry.getKey());
            }
        }

        //随机取出
        int serverIndex = new Random().nextInt(serverList.size());
        return serverList.get(serverIndex);

    }

    /**
     * 轮询获取指定权重的流量
     * @return
     */
    public String getServerByAssiginProportionPoll() {
        Map<String, Integer> currentServerMap = serverMap;

        List<String> serverList = new ArrayList<String>();

        Set<Map.Entry<String, Integer>> entrySet = currentServerMap.entrySet();
        for (Map.Entry<String, Integer> entry : entrySet) {
            for (int i = 0; i < entry.getValue(); i++) {
                serverList.add(entry.getKey());
            }
        }
        String server;
        synchronized (this) {
            server = serverList.get(position);
            position++;
        }
        return server;
    }

    /**
     * 随机获取指定权重的流量
     * @return
     */
    public String getServerByAssiginProportion() {
        Map<String, Integer> currentServerMap = serverMap;

        Map<Integer, String> resultMap = new HashMap<Integer, String>();

        Set<Map.Entry<String, Integer>> entrySet = currentServerMap.entrySet();
        for (Map.Entry<String, Integer> entry : entrySet) {
            int startIndex = entry.getValue();
            for (int i = 1; i < entry.getValue() + 1; i++) {
                resultMap.put(startIndex, entry.getKey());
                startIndex++;
            }
        }

        int serverIndex = new Random().nextInt(resultMap.size());
        return resultMap.get(serverIndex);
    }
}
