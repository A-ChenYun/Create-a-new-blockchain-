package chen.dao;

import chen.utils.EncryptUtil;
import chen.utils.MongoDBUtil;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import lombok.Data;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * 包含两个数组，分别是:
 * 1.区块链，整个区块链数据
 * 2.交易列表，最新状态
 * @Author AChen
 * @Data: 2020/5/11 11:31 上午
 */
@Data
public class BlockChain  {

    // 存储区块链,整个区块数据
    private List<Map<String, Object>> chain;
    // 该实例变量用于当前的交易信息列表
    private List<Map<String, Object>> currentTransactions;
    private static BlockChain blockChain = null;
    public Set<String> nodes;
    public  HashMap<String, Integer> hashMap ;

/*    public BlockChain() {
        // 初始化区块链以及当前的交易信息列表
        this.chain = new ArrayList<Object>();
        this.currentTransactions= new ArrayList<Object>();
    }*/

    public BlockChain() {
        // 初始化区块链以及当前的交易信息列表
        chain = new ArrayList<Map<String, Object>>();
        currentTransactions = new ArrayList<Map<String, Object>>();
        // 用于存储网络中其他节点的集合
        nodes = new HashSet<String>();
        hashMap = new HashMap();
        nodes.add("ouyangyunchen");
        // 创建创世区块

        DBCollection collection = MongoDBUtil.
                getConnect().getCollection("Blockchain");
        DBObject dbo  = (DBObject) JSON.parse(com.alibaba.fastjson.JSONObject.
                toJSONString(geneousBlock(5)));
       collection.insert(dbo);
    }

    private Map<String, Object>  geneousBlock(int difficulty) {


            Map<String, Object> block = new HashMap<String, Object>();
            block.put("index", 0);
            block.put("transactions", "getCurrentTransactions()");
            //创世块前置hash为0
            block.put("previous_hash", 0);
            /*String target = new String(new char[difficulty]).replace('\0', '0');
            int nonce = 0;
            String hash = new String();
            hash = "";
            long time = 0;
            while (!hash.substring(0, difficulty).equals(target)) {
                nonce++;
                time = System.currentTimeMillis();
                hash = calculateHash(getChain().size() + 1, time,
                        getCurrentTransactions(), hash(getChain().get(getChain().size() - 1)).toString(),
                        nonce);
            }
            block.put("hash", hash);
            block.put("timestamp", time);*/

            // 重置当前的交易信息列表
            setCurrentTransactions(new ArrayList<Map<String, Object>>());

            getChain().add(block);

            return block;

    }

    // 创建单例对象
    public static BlockChain getInstance() {
        if (blockChain == null) {
            synchronized (BlockChain.class) {
                if (blockChain == null) {
                    blockChain = new BlockChain();
                }
            }
        }
        return blockChain;
    }


    public Set<String> getNodes() {
        return nodes;
    }

    /**
     * 注册节点
     *
     * @param address
     *            节点地址
     * @throws MalformedURLException
     */
    public Set<String> registerNode(String address) throws MalformedURLException {
        URL url = new URL(address);
        String node = url.getHost() + ":" + (url.getPort() == -1 ? url.getDefaultPort() : url.getPort());
        nodes.add(node);
        nodes.add(url.toString());
        return nodes;

    }

    public List<Map<String, Object>> getChain() {
        return chain;
    }

    public void setChain(List<Map<String, Object>> chain) {
        this.chain = chain;
    }

    public List<Map<String, Object>> getCurrentTransactions() {
        return currentTransactions;
    }

    public void setCurrentTransactions(List<Map<String, Object>> currentTransactions) {
        this.currentTransactions = currentTransactions;
    }

    /**
     * @return 得到区块链中的最后一个区块
     */
    public Map<String, Object> lastBlock() {
        return getChain().get(getChain().size() - 1);
    }

    /**
     * 在区块链上新建一个区块
     *
     * @param
     *
     * @return 返回新建的区块
     */
    public Map<String, Object> newBlock(int difficulty) {

        Map<String, Object> block = new HashMap<String, Object>();
        block.put("index", getChain().size());
        block.put("transactions", getCurrentTransactions());

        block.put("previous_hash", hash(getChain().get(getChain().size() - 1)));
        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
        int nonce = 0;
        long time = 0;
        String thishash = "wqwqqwqwqwqwq";
        while (!thishash.substring( 0,difficulty).equals(target)) {
            nonce++;
            time = System.currentTimeMillis();
             thishash = calculateHash(getChain().size() + 1, time,
                    getCurrentTransactions(), hash(getChain().get(getChain().size() - 1)).toString(),
                    nonce);
        }
        block.put("hash", thishash);
        block.put("timestamp", time);

        // 重置当前的交易信息列表
        setCurrentTransactions(new ArrayList<Map<String, Object>>());

        getChain().add(block);

        return block;
    }


    /**
     * 生成新交易信息，信息将加入到下一个待挖的区块中
     *
     * @param sender
     *            发送方的地址
     * @param recipient
     *            接收方的地址
     * @param amount
     *            交易数量
     * @return 返回存储该交易事务的块的索引
     */
    public int newTransactions(String sender, String recipient, long amount) {

        Map<String,Object> transaction = new HashMap<String, Object>();
        EncryptUtil encryptUtil = new EncryptUtil();
        String TransactionId = encryptUtil.getSHA256(sender.trim() + recipient.trim() + String.valueOf(amount).trim());
        transaction.put("TransactionId", TransactionId);

        transaction.put( "sender", sender);
        transaction.put("recipient", recipient);

        transaction.put("amount", amount);
        getCurrentTransactions().add(transaction);

        return (Integer) lastBlock().get("index") + 1;
    }

    /**
     * 生成区块的 SHA-256格式的 hash值
     *
     * @param block
     *            区块
     * @return 返回该区块的hash
     */
    public static Object hash(Map<String, Object> block) {
        return new EncryptUtil().getSHA256(new JSONObject(block).toString());
    }



    //计算当前块的hash
    public String calculateHash(Integer index, long timeStamp,
                                List<Map<String, Object>> currentTransactions,String previoushash
                               ,Integer nonce ) {

        String calculatedhash = new EncryptUtil().getSHA256(
                index.toString()+
                        currentTransactions.toString()+
                        Long.toString(timeStamp) +
                        previoushash +
                        nonce.toString()
        )
                ;
        return calculatedhash;
    }



    /**
     * 检查是否是有效链，遍历每个区块验证hash，来确定一个给定的区块链是否有效
     *
     * @param chain
     * @return
     */
    public boolean validChain(List<Map<String, Object>> chain) {
        Map<String, Object> lastBlock = chain.get(0);
        int currentIndex = 1;
        while (currentIndex < chain.size()) {
            Map<String, Object> block = chain.get(currentIndex);
            System.out.println(lastBlock.toString());
            System.out.println(block.toString());
            System.out.println("\n-------------------------\n");

            // 检查block的hash是否正确
            if (!block.get("previous_hash").equals(hash(lastBlock))) {
                return false;
            }

            lastBlock = block;
            currentIndex++;
        }
        return true;
    }

    /**
     * 共识算法解决冲突，使用网络中最长的链. 遍历所有的邻居节点，并用上一个方法检查链的有效性，
     * 如果发现有效更长链，就替换掉自己的链
     *
     * @return 如果链被取代返回true, 否则返回false
     * @throws IOException
     */
    public boolean resolveConflicts() throws IOException {
        Set<String> neighbours = this.nodes;
        List<Map<String, Object>> newChain = null;

        // 寻找最长的区块链
        long maxLength = this.chain.size();

        // 获取并验证网络中的所有节点的区块链
        for (String node : neighbours) {

            URL url = new URL("http://" + node + "/block2_war_exploded/chain");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8"));
                StringBuffer responseData = new StringBuffer();
                String response = null;
                while ((response = bufferedReader.readLine()) != null) {
                    responseData.append(response);
                }
                bufferedReader.close();

                JSONObject jsonData = new JSONObject(bufferedReader.toString());
                long length = jsonData.getLong("length");
                List<Map<String, Object>> chain = (List) jsonData.getJSONArray("chain");

                // 检查长度是否长，链是否有效
                if (length > maxLength && validChain(chain)) {
                    maxLength = length;
                    newChain = chain;
                }
            }

        }
        // 如果发现一个新的有效链比我们的长，就替换当前的链
        if (newChain != null) {
            this.chain = newChain;
            return true;
        }
        return false;
    }




}
