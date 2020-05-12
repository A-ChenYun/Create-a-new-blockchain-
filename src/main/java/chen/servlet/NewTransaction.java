package chen.servlet;

import chen.dao.BlockChain;
import chen.wallet.Wallet;
import lombok.Data;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author AChen
 * @Data: 2020/5/11 12:02 下午
 */
// 该Servlet用于接收并处理新的交易信息
@Data
@WebServlet("/transactions/new")
public class NewTransaction extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {

        req.setCharacterEncoding("utf-8");
        // 读取客户端传递过来的数据并转换成JSON格式
        BufferedReader reader = req.getReader();
        String input = null;
        StringBuffer requestBody = new StringBuffer();
        while ((input = reader.readLine()) != null) {
            requestBody.append(input);
        }
        JSONObject jsonValues = new JSONObject(requestBody.toString());

        // 检查所需要的字段是否位于POST的data中
        String[] required = { "sender", "recipient", "amount" };
        for (String string : required) {
            if (!jsonValues.has(string)) {
                // 如果没有需要的字段就返回错误信息
                resp.sendError(400, "Missing values");
            }
        }
        // 获取sender资质
        String senderAddress = jsonValues.getString("sender").trim();
        String receiveAddress = jsonValues.getString("recipient").trim();

        Integer amount = Integer.valueOf(jsonValues.getString("amount").trim());


        BlockChain blockChain = BlockChain.getInstance();
        Wallet wallet = new Wallet();
        //节点中有该地址存在，而且余额要大,接受者地址存在
        if ( blockChain.nodes.contains(senderAddress) &&
                wallet.checkMoney(senderAddress,amount)&&blockChain.nodes.contains(receiveAddress)){
        // 新建交易信息
        int index = blockChain.newTransactions(jsonValues.getString("sender"), jsonValues.getString("recipient"),
                jsonValues.getLong("amount"));

        // 返回json格式的数据给客户端
        resp.setContentType("application/json");
        PrintWriter printWriter = resp.getWriter();
        printWriter.println(new JSONObject().append("message", "Transaction will be added to Block " + index));
        printWriter.close();
        //减少余额
        wallet.delMoney(senderAddress,amount);
        wallet.addMoneyToP(receiveAddress,amount);
    }else {
            resp.setContentType("application/json");
            PrintWriter printWriter = resp.getWriter();
            printWriter.println(new JSONObject().append("message", " The sending address does not exist or the surplus is insufficient receiver not exitor "));
            printWriter.close();
        }
    }
}
