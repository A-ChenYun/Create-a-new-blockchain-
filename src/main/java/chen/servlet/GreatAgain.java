package chen.servlet;

import chen.dao.BlockChain;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * 共识，取最长链
 * @Author AChen
 * @Data: 2020/5/11 10:20 下午
 */
@WebServlet("/brotherchain")
public class GreatAgain extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BlockChain blockChain = BlockChain.getInstance();
        boolean b = blockChain.resolveConflicts();


        // 返回json格式的数据给客户端
        resp.setContentType("application/json");
        PrintWriter printWriter = resp.getWriter();
        printWriter.println(b);
        printWriter.close();
    }
}
