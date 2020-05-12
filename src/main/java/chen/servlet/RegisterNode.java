package chen.servlet;

import chen.dao.BlockChain;
import lombok.SneakyThrows;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Set;

/**
 * @Author AChen
 * @Data: 2020/5/11 9:48 下午
 */
@WebServlet("/register")

public class RegisterNode extends HttpServlet {

    @SneakyThrows
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BlockChain blockChain = BlockChain.getInstance();
        Set<String> returnNodes = blockChain.registerNode(req.getRequestURL().toString());


        // 返回json格式的数据给客户端
        resp.setContentType("application/json");
        PrintWriter printWriter = resp.getWriter();
        printWriter.println(returnNodes.toString());
        printWriter.close();
}
}
