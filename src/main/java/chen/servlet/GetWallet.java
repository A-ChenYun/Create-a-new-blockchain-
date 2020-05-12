package chen.servlet;

import chen.dao.BlockChain;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 得到本人余额
 * @Author AChen
 * @Data: 2020/5/12 11:30 上午
 */
@WebServlet("/getBalance")
public class GetWallet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BlockChain blockChain = BlockChain.getInstance();
        req.setCharacterEncoding("utf-8");
        // 读取客户端传递过来的数据并转换成JSON格式
        BufferedReader reader = req.getReader();
        String input = null;
        StringBuffer requestBody = new StringBuffer();
        while ((input = reader.readLine()) != null) {
            requestBody.append(input);
        }
        JSONObject jsonValues = new JSONObject(requestBody.toString());

        String walletOwnAddress = jsonValues.getString("yourAddress").trim();

        Integer balance = blockChain.hashMap.get(walletOwnAddress);

        // 返回json格式的数据给客户端
        resp.setContentType("application/json");
        PrintWriter printWriter = resp.getWriter();
        printWriter.println(balance);
        printWriter.close();
    }
}
