package chen.servlet;

import chen.dao.BlockChain;
import chen.utils.MongoDBUtil;
import chen.wallet.Wallet;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import lombok.Data;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author AChen
 * @Data: 2020/5/11 12:02 下午
 */

//该Servlet用于运行工作算法的证明来获得下一个证明，也就是所谓的挖矿
@WebServlet("/mine")
public class Mine extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlockChain blockChain = BlockChain.getInstance();
        Map<String, Object> lastBlock = blockChain.lastBlock();
    // 给工作量证明的节点提供奖励，发送者为 "0" 表明是新挖出的币
        String uuid = (String) this.getServletContext().getAttribute("uuid");
        blockChain.newTransactions("0", uuid, 1);
        //挖矿人地址加入钱包
        Wallet wallet = new Wallet();

        if (!blockChain.nodes.contains(uuid)){
         blockChain.nodes.add(uuid);
            wallet.addMoneyToP(uuid,1);
        }else {
            wallet.addMoneyToP(uuid,1);

        }


        // 构建新的区块
        Map<String, Object> newBlock = blockChain.newBlock(2);

 Map<String, Object> response = new HashMap<String, Object>();
        response.put("message", "New Block Forged");
        response.put("index", newBlock.get("index"));
        response.put("transactions", newBlock.get("transactions"));
        response.put("previous_hash", newBlock.get("previous_hash"));


        // 返回新区块的数据给客户端
        resp.setContentType("application/json");
        PrintWriter printWriter = resp.getWriter();
        printWriter.println(new JSONObject(newBlock));
        printWriter.close();

        DBCollection collection = MongoDBUtil.
                getConnect().getCollection("Blockchain");

        DBObject dbo  = (DBObject) JSON.parse(com.alibaba.fastjson.JSONObject.
                toJSONString(newBlock));

        collection.insert(dbo);

    }

}
